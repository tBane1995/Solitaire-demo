package com.tbane.solitaire.Game;

import java.util.ArrayList;

public class Foundations {

    public static ArrayList<Foundation> foundations;

    public static void create() {
        foundations = new ArrayList<>();
        foundations.add(new Foundation(0));
        foundations.add(new Foundation(1));
        foundations.add(new Foundation(2));
        foundations.add(new Foundation(3));
    }

}
