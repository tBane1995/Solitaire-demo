package com.tbane.solitaire.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;
import com.tbane.solitaire.AssetsManager;

import java.util.ArrayList;

public class Foundation {

    public int _id;
    public Rectangle _rect;
    public ArrayList<Card> _cards;
    public static float _dx;
    public static float _dy;

    Foundation(int id){
        _id = id;
        _rect = new Rectangle(
            _dx + _id*(Card._size.x + 2*Card._padding) + Card._padding,
            -_dy + Renderer.VIRTUAL_HEIGHT - Card._size.y - Card._padding,
            Card._size.x,
            Card._size.y
        );
        _cards = new ArrayList<>();
    }

    public Card getLastCard(){
        if(_cards.isEmpty())
            return null;

        return _cards.get(_cards.size()-1);
    }

    public Card getOneBeforeLastCard(){
        if(_cards.isEmpty())
            return null;

        if(_cards.size() < 2)
            return null;

        return _cards.get(_cards.size()-2);
    }

    public Card popLastCard() {
        if (_cards.isEmpty())
            return null;

        Card card = _cards.get(_cards.size() - 1);
        _cards.remove(_cards.size() - 1);
        return card;
    }
    boolean cardIsAcceptable(Card card){

        Card lastCard = getLastCard();

        if(lastCard == null){
            return (card._value == 1);
        }

        return (lastCard._color == card._color && lastCard._value + 1 == card._value );
    }

    void addCard(Card card){
        _cards.add(card);
        Vector2 position = new Vector2(_rect.x, _rect.y);
        card.setPosition(position);
    }

    void handleEvents(){

        if(_cards.isEmpty())
            return;

        if(!CardsInHand._cards.isEmpty())
            return;

        if(MyInput.processor.isTouchDown()){

            Vector2 curPos = MyInput.processor.getTouchPosition();
            if(_rect.contains(curPos)){
                CardsInHand.from = CardsInHand.fromWhere.Foundation;
                CardsInHand.fromID = _id;
                Card card = popLastCard();
                CardsInHand.addCard(card);
                CardsInHand.setOffset(new Vector2(curPos.x - card.getPosition().x, curPos.y - card.getPosition().y));

            }
        }

    }

    void update() {

    }
    void draw() {

        Texture texture = AssetsManager.getTexture("tex/slot.png");
        if(texture!=null){
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(_rect.x, _rect.y);
            sprite.setSize(_rect.width, _rect.height);
            sprite.draw(Renderer.spriteBatch);
        }

        if (getOneBeforeLastCard() != null)
            getOneBeforeLastCard().draw();

        if (getLastCard()!= null)
            getLastCard().draw();

    }
}
