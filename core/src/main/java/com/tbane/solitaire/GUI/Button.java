package com.tbane.solitaire.GUI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Time;
import com.tbane.solitaire.Renderer;

public class Button extends ElementGUI {

    public Rectangle _rect;
    protected Texture _normalTexture, _hoverTexture, _pressedTexture;
    public enum ButtonStates { Idle, Hover, Pressed }
    public ButtonStates _state;
    float _clickTime;
    public Runnable onclick_func;

    public Button(Texture normalTexture, Texture hoverTexture, Texture pressedTexture, int x, int y, int width, int height) {
        super();
        _rect = new Rectangle(x, y, width, height);
        _normalTexture = normalTexture;
        _hoverTexture = hoverTexture;
        _pressedTexture = pressedTexture;


        _state = ButtonStates.Idle;
        _clickTime = 0.0f;

        onclick_func = () -> {};
    }
    private void unclick() {
        _state = ButtonStates.Idle;
    }

    private void hover() {
        _state = ButtonStates.Hover;
    }

    private void click() {
        _state = ButtonStates.Pressed;
        _clickTime = Time.currentTime;
    }

    @Override
    public void handleEvents() {



        if(_rect.contains(MyInput.processor.getTouchX(), MyInput.processor.getTouchY())) {
            if(MyInput.processor.isTouchDown()) {
                GUI.ElementGUI_pressed = this;
            }
		else if(MyInput.processor.isTouchUp()) {
                if (GUI.ElementGUI_pressed == this) {
                    click();
                    return;
                }
            }
        }

        if(_state!= ButtonStates.Pressed) {
            if(MyInput.processor.isTouchMoved() || MyInput.processor.isTouchDown()){

                if(_rect.contains(MyInput.processor.getTouchX(), MyInput.processor.getTouchY())){
                    GUI.ElementGUI_hovered = this;
                }else {

                    if(GUI.ElementGUI_hovered == this){
                        GUI.ElementGUI_hovered = null;
                    }
                }
            }
        }

    }

    @Override
    public void update() {
        if (_state == ButtonStates.Pressed) {
            if ((Time.currentTime - _clickTime) > 0.05f) {
                onclick_func.run();

                if(GUI.ElementGUI_pressed == this)
                    GUI.ElementGUI_pressed = null;
                unclick();
            }
        }
        else if (GUI.ElementGUI_hovered == this) {
            hover();
        }
        else
            unclick();
    }

    @Override
    public void draw() {
        Texture tex;

        switch (_state) {
            case Hover:
                tex = _hoverTexture;
                break;
            case Pressed:
                tex = _pressedTexture;
                break;
            //case Idle:
                //tex = _normalTexture;
                //break;
            default:
                tex = _normalTexture;
        }

        Sprite sprite = new Sprite(tex);
        sprite.setBounds(_rect.x, _rect.y, _rect.width, _rect.height);
        sprite.draw(Renderer.spriteBatch);
    }
}
