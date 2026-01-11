package com.tbane.solitaire.Game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tbane.solitaire.MyInput.MyInput;

import java.util.ArrayList;

public class CardsInHand {
    public static ArrayList<Card> _cards;
    public enum fromWhere { Tableau, Foundation, Waste }
    public static fromWhere from;
    public static int fromID;
    private static Vector2 _offset;

    static {
        _cards = new ArrayList<>();
        _offset = new Vector2();
    }

    static void setOffset(Vector2 offset){
        _offset = offset;
    }
    static void addCard(Card card) {
        _cards.add(card);
    }

    static boolean addingCardToFoundations() {
        ArrayList<Foundation> hovered_foundations = new ArrayList<>();

        for (Foundation foundation : Foundations.foundations) {
            if (foundation._rect.overlaps(_cards.get(0)._rect)) {
                hovered_foundations.add(foundation);
            }
        }

        if (!hovered_foundations.isEmpty()) {

            Vector2 cardCenter = new Vector2(
                _cards.get(0)._rect.x + _cards.get(0)._rect.width / 2f,
                _cards.get(0)._rect.y + _cards.get(0)._rect.height / 2f
            );

            hovered_foundations.sort((a, b) -> {

                Vector2 aCenter = new Vector2(
                    a._rect.x + a._rect.width / 2f,
                    a._rect.y + a._rect.height / 2f
                );

                Vector2 bCenter = new Vector2(
                    b._rect.x + b._rect.width / 2f,
                    b._rect.y + b._rect.height / 2f
                );

                return Float.compare(cardCenter.dst2(aCenter), cardCenter.dst2(bCenter));
            });

            if(!(from == fromWhere.Foundation && hovered_foundations.get(0)._id == fromID)){
                if (hovered_foundations.get(0).cardIsAcceptable(_cards.get(0))) {
                    Rectangle startRect = _cards.get(0)._rect;
                    hovered_foundations.get(0).addCard(_cards.get(0));
                    Rectangle endRect = _cards.get(0)._rect;
                    Animations.addAnimation(_cards.get(0), startRect, endRect);
                    _cards.remove(0);
                    Game._moves += 1;
                    return true;
                }
            }


        }

        return false;
    }

    static boolean addingCardsToTableaus(){
        ArrayList<Tableau> hovered_tableaus = new ArrayList<>();
        for(Tableau tableau : Tableaus.tableaus){
            if(tableau.getLastCard() != null && tableau.getLastCard()._rect.overlaps((_cards.get(0)._rect)))
                hovered_tableaus.add(tableau);
            else if(tableau.getLastCard() == null && tableau._rect.overlaps(_cards.get(0)._rect)){
                hovered_tableaus.add(tableau);
            }
        }

        if (!hovered_tableaus.isEmpty()) {

            Vector2 cardCenter = new Vector2(
                _cards.get(0)._rect.x + _cards.get(0)._rect.width / 2f,
                _cards.get(0)._rect.y + _cards.get(0)._rect.height / 2f
            );

            hovered_tableaus.sort((a, b) -> {

                Vector2 aCenter = new Vector2(
                    a._rect.x + a._rect.width / 2f,
                    a._rect.y + a._rect.height / 2f
                );

                Vector2 bCenter = new Vector2(
                    b._rect.x + b._rect.width / 2f,
                    b._rect.y + b._rect.height / 2f
                );

                return Float.compare(cardCenter.dst2(aCenter), cardCenter.dst2(bCenter));
            });

            if(hovered_tableaus.get(0).cardIsAcceptable(_cards.get(0))){
                while(!_cards.isEmpty()){
                    Rectangle startRect = new Rectangle(_cards.get(0)._rect);
                    hovered_tableaus.get(0).addCard(_cards.get(0));
                    Rectangle endRect = new Rectangle(_cards.get(0)._rect);
                    Animations.addAnimation(_cards.get(0), startRect, endRect);
                    _cards.remove(0);
                }

                if(!(CardsInHand.from == fromWhere.Tableau && Tableaus.tableaus.get(CardsInHand.fromID) == hovered_tableaus.get(0)))
                    Game._moves += 1;
                return true;
            }


        }

        return false;
    }
    static void handleEvents() {

        if(_cards.isEmpty())
            return;


        if(MyInput.processor.isTouchUp()){

            if(_cards.size() == 1) {
                if (addingCardToFoundations()){
                    return;
                }

            }

            if(addingCardsToTableaus()){
                return;
            }

            if(from == fromWhere.Waste){
                Rectangle startRect = _cards.get(0)._rect;
                Waste.addCard(_cards.get(0));
                Rectangle endRect = Waste._rect;
                Animations.addAnimation(_cards.get(0), startRect, endRect);
                _cards.remove(0);
                return;
            }

            if(from == fromWhere.Foundation){
                Rectangle startRect = new Rectangle(_cards.get(0)._rect);
                Foundations.foundations.get(fromID).addCard(_cards.get(0));
                Rectangle endRect = Foundations.foundations.get(fromID)._rect;
                Animations.addAnimation(_cards.get(0), startRect, endRect);
                _cards.remove(0);
                return;
            }

            if(from == fromWhere.Tableau){
                while(!_cards.isEmpty()){
                    Rectangle startRect = new Rectangle(_cards.get(0)._rect);
                    Tableaus.tableaus.get(fromID).addCard(_cards.get(0));
                    Rectangle endRect = new Rectangle(_cards.get(0)._rect);
                    Animations.addAnimation(_cards.get(0), startRect, endRect);
                    _cards.remove(0);
                }

            }




        }
    }
    static void update() {

        if(_cards.isEmpty())
            return;

        Vector2 curPos = MyInput.processor.getTouchPosition();
        int i = 0;
        for(Card card : _cards){
            Vector2 newPosition = new Vector2(curPos.x - _offset.x, curPos.y - _offset.y - (Card._dy * i));
            card.setPosition(newPosition);
            i+=1;
        }
    }

    static void draw() {

        if(_cards.isEmpty())
            return;

        for(Card card : _cards) {
            card.draw();
        }
    }
}
