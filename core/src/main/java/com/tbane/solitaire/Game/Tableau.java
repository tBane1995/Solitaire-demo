package com.tbane.solitaire.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tbane.solitaire.AssetsManager;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;

import java.util.ArrayList;


public class Tableau {

    public final int _id;
    public Rectangle _rect;
    public ArrayList<Card> _cards;
    public static float _dy;
    public static float _dx;

    Tableau(int id){
        _id = id;
        _cards = new ArrayList<>();

        _rect = new Rectangle(
            _dx + _id*(Card._size.x + 2*Card._padding) + Card._padding,
            -_dy + Renderer.VIRTUAL_HEIGHT - Card._size.y - Card._padding,
            Card._size.x,
            Card._size.y
        );
    }
    public Card getLastCard(){
        if(_cards.isEmpty())
            return null;

        return _cards.get(_cards.size()-1);
    }

    boolean cardIsAcceptable(Card card) {

        if(card == null)
            return false;

        Card lastCard = getLastCard();

        if (lastCard == null) {
            return (card._value == 13);
        }

        if (lastCard._value - 1 == card._value) {
            if (lastCard._color == Card.cardColor.Heart || lastCard._color == Card.cardColor.Tile) {
                return (card._color == Card.cardColor.Pike || card._color == Card.cardColor.Clover);
            }

            if (lastCard._color == Card.cardColor.Pike || lastCard._color == Card.cardColor.Clover) {
                return (card._color == Card.cardColor.Heart || card._color == Card.cardColor.Tile);
            }
        } else {
            return false;
        }

        return false;
    }

    void addCard(Card card) {
        _cards.add(card);

        Vector2 position = new Vector2(
            _dx + _id * (Card._size.x + 2*Card._padding) + Card._padding,
            -_dy + Renderer.VIRTUAL_HEIGHT - Card._size.y  - Card._padding - (_cards.size() - 1) * Card._dy
        );
        card.setPosition(position);
    }

    void removeCard(int id){
        _cards.remove(id);
    }
    void handleEvents(){

        if(!CardsInHand._cards.isEmpty())
            return;

        if(MyInput.processor.isTouchDown()){
            for(int i = _cards.size() - 1; i >= 0; i--) {
                Card card = _cards.get(i);


                if( (i == _cards.size()-1 && card.cursorHover()) || card.topCursorHover()) {
                    if(card._isOpen){

                        if(Animations.cardIsAnimated(card)){
                            return;
                        }

                        Vector2 curPos = MyInput.processor.getTouchPosition();
                        CardsInHand.from = CardsInHand.fromWhere.Tableau;
                        CardsInHand.fromID = _id;
                        CardsInHand.setOffset(new Vector2(curPos.x - card.getPosition().x, curPos.y - card.getPosition().y));

                        for(int j=i; j<_cards.size(); ){
                            CardsInHand.addCard(_cards.get(j));
                            _cards.remove(j);
                        }
                    }else{
                        if(card == getLastCard()){
                            card._isOpen = true;
                            Game._moves += 1;
                        }
                    }
                    break;
                }
            }
        }

    }

    void update() {

    }
    void draw(){

        Texture slotTexture = AssetsManager.getTexture("tex/slot.png");
        if(slotTexture != null){
            Sprite sprite = new Sprite(slotTexture);
            Vector2 position = new Vector2(_dx + _id * (Card._size.x + 2*Card._padding) + Card._padding, Renderer.VIRTUAL_HEIGHT - _dy - Card._size.y - Card._padding);
            sprite.setPosition(position.x, position.y);
            sprite.setSize(Card._size.x, Card._size.y);
            sprite.draw(Renderer.spriteBatch);
        }


        for(Card card : _cards){
            card.draw();
        }
    }
}

