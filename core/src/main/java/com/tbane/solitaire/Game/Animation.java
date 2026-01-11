package com.tbane.solitaire.Game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tbane.solitaire.Time;

public class Animation {

    Card _card;
    Rectangle _startRect;
    Rectangle _endRect;
    public float _startAnimationTime;

    Animation(Card card, Rectangle startRect, Rectangle endRect ){
        _card = card;
        _startRect = startRect;
        _endRect = endRect;
        _startAnimationTime = Time.currentTime;
    }

    public void update() {
        float dt = Time.currentTime - _startAnimationTime;
        float t = dt / Animations.animationTime;
        if (t > 1f) t = 1f;

        Vector2 newPosition = new Vector2(
            _startRect.x + (_endRect.x - _startRect.x) * t,
            _startRect.y + (_endRect.y - _startRect.y) * t
            );

        _card.setPosition(newPosition);
    }

    public void draw() {
        _card.draw();
    }
}
