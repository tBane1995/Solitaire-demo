package com.tbane.solitaire.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tbane.solitaire.AssetsManager;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;

import java.util.ArrayList;

public class Stock {
    public static Rectangle _rect;
    public static ArrayList<Card> _cards;

    public static void create(Vector2 position) {
        _rect = new Rectangle(
            position.x,
            position.y,
            Card._size.x,
            Card._size.y
        );

        _cards = new ArrayList<>();
    }

    public static void addCard(Card card) {
        _cards.add(card);
        Vector2 position = new Vector2(
            _rect.x,
            _rect.y
        );
        card.setPosition(position);
    }

    public static void addCards(ArrayList<Card> cards){
        while(!cards.isEmpty()){
            Stock.addCard(cards.get(0));
            cards.remove(0);
        }
    }
    public static Card getLastCard() {
        if (_cards.isEmpty())
            return null;

        return _cards.get(_cards.size() - 1);
    }

    public static void handleEvents() {

        if(!CardsInHand._cards.isEmpty())
            return;

        if(MyInput.processor.isTouchDown()){
            if(_rect.contains(MyInput.processor.getTouchPosition())){
                Card lastCard = getLastCard();

                if(Animations.cardIsAnimated(lastCard)){
                    return;
                }

                if(lastCard != null){
                    Rectangle startRect = _rect;
                    Waste.addCard(lastCard);
                    Rectangle endRect = Waste._rect;
                    _cards.remove(_cards.size() - 1);
                    Animations.addAnimation(lastCard, startRect, endRect);
                    lastCard._isOpen = true;
                }
                else{
                    while (!Waste._cards.isEmpty()) {
                        Rectangle startRect = Waste._rect;
                        Card card = Waste._cards.get(Waste._cards.size() - 1);
                        Waste._cards.remove(Waste._cards.size() - 1);
                        addCard(card);
                        Rectangle endRect = _rect;
                        Animations.addAnimation(card, startRect, endRect);
                        card._isOpen = false;
                    }
                }
                Game._moves += 1;
            }
        }
    }

    public static void update() {

    }

    public static void draw() {

        Texture slotTexture = AssetsManager.getTexture("tex/slot.png");
        if(slotTexture != null){
            Sprite sprite = new Sprite(slotTexture);
            sprite.setPosition(_rect.x, _rect.y);
            sprite.setSize(Card._size.x, Card._size.y);
            sprite.draw(Renderer.spriteBatch);
        }


        for(Card card : _cards){
            card.draw();
        }


    }
}
