package com.tbane.solitaire.Game;

import com.tbane.solitaire.Time;

import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Animations {
    public static ArrayList<Animation> animations;
    public static float animationTime = 0.25f;


    public static void create() {
        animations = new ArrayList<>();
    }

    public static void addAnimation(Card card, Rectangle startRect, Rectangle endRect) {
        animations.add(new Animation(card, startRect, endRect));
    }

    public static boolean cardIsAnimated(Card card){
        for(Animation animation : animations){
            if(animation._card == card)
                return true;
        }

        return false;
    }

    public static void update() {
        for(int i=animations.size()-1; i>=0; i--){
            animations.get(i).update();

            if(Time.currentTime - animations.get(i)._startAnimationTime > Animations.animationTime){
                animations.remove(i);
            }
        }
    }

    public static void draw() {
        for(Animation animation : animations){
            animation.draw();
        }
    }
}
