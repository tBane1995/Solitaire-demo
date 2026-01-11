package com.tbane.solitaire.GUI;

import com.badlogic.gdx.graphics.Texture;
import com.tbane.solitaire.Time;

import java.util.ArrayList;

public class AnimatedButtonWithText extends ButtonWithText {

    public ArrayList<Texture> _frames = new ArrayList<>();
    public int _currentFrame;
    public float _timer;

    public AnimatedButtonWithText(String text, Texture frame1Texture, Texture frame2Texture, Texture hoverTexture, Texture pressedTexture, int x, int y, int width, int height) {
        super(text, frame1Texture, hoverTexture, pressedTexture, x, y, width, height);

        _currentFrame = 0;
        _frames.add(frame1Texture);
        _frames.add(frame2Texture);
        _timer = Time.currentTime;
    }

    @Override
    public void update() {
        super.update();

        if(Time.currentTime - _timer > 0.25f){
            _currentFrame += 1;
            if(_currentFrame > _frames.size()-1){
                _currentFrame = 0;
            }
            _timer = Time.currentTime;
            _normalTexture = _frames.get(_currentFrame);
        }
    }
}
