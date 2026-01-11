package com.tbane.solitaire.Game;

import java.util.ArrayList;

public class Tableaus {

    public static ArrayList<Tableau> tableaus;

    public static void create() {

        tableaus = new ArrayList<Tableau>();

        for(int i=0;i<7;i++)
            tableaus.add(new Tableau(i));
    }

    public static void setTheCardsFromTheStock() {
        for(int i = 0; i < tableaus.size(); i++){
            for(int j = 1; j <= i+1; j++){
                Card card = Stock.getLastCard();
                if(card == null) break;

                tableaus.get(i).addCard(card);
                Stock._cards.remove(Stock._cards.size()-1);
                if(j == i+1)
                    card._isOpen = true;
            }
        }
    }

}
