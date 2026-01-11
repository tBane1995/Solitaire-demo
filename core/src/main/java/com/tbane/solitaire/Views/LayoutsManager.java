package com.tbane.solitaire.Views;

import java.util.ArrayList;

public class LayoutsManager {
    public static ArrayList<Layout> array = new ArrayList<>();

    public static void add(Layout layout){
        array.add(layout);
    }

    public static Layout back() {
        if (array.isEmpty()) return null;
        return array.get(array.size() - 1);
    }

    public static void pop_back(){
        if(array.isEmpty())
            return;

        LayoutsManager.array.remove(array.size()-1);

    }
}
