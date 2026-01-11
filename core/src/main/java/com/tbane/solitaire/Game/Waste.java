package com.tbane.solitaire.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;
import com.tbane.solitaire.AssetsManager;

import java.util.ArrayList;

public class Waste {
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

    public static Card getLastCard() {
        if (_cards.isEmpty())
            return null;

        return _cards.get(_cards.size() - 1);
    }

    public static void addCard(Card card) {
        _cards.add(card);
        card._rect = new Rectangle(_rect);
    }

    public static void handleEvents() {

        if(!CardsInHand._cards.isEmpty())
            return;

        if(MyInput.processor.isTouchDown()){

            Vector2 curPos = MyInput.processor.getTouchPosition();
            if(_rect.contains(curPos)){

                if(Animations.cardIsAnimated(getLastCard())) {
                    return;
                }

                if(!_cards.isEmpty()){
                    CardsInHand.from = CardsInHand.fromWhere.Waste;
                    CardsInHand.fromID = -1;
                    Card card = getLastCard();
                    _cards.remove(_cards.size()-1);
                    CardsInHand.setOffset(new Vector2(curPos.x - card.getPosition().x, curPos.y - card.getPosition().y));
                    CardsInHand.addCard(card);
                }
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
