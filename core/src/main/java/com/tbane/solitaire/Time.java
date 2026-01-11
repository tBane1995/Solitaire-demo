package com.tbane.solitaire;

import com.badlogic.gdx.Gdx;

public class Time {

    public static float previousTime;
    public static float currentTime;

    Time(){
        previousTime = 0.0f;
        currentTime = 0.0f;
    }

    public static void update() {
        previousTime = currentTime;
        currentTime += Gdx.graphics.getDeltaTime();
    }

    public static String generateTimeString(float time) {
        int minutes = (int)(time)/60;
        int seconds = (int)(time)%60;

        if(minutes>99){
            minutes = 99;
            seconds = 59;
        }

        String text = "";

        if(minutes < 10)
            text = text + "0";
        text = text + minutes + ":";

        if(seconds < 10)
            text = text + "0";
        text = text + seconds;

        return text;
    }
}
