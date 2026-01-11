package com.tbane.solitaire.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;
import com.tbane.solitaire.Time;

public class TextInput {

    String _text;            // is only text
    String _defaultText;    // if _text is empty then return _defaultText
    String _messageText;     // if text is empty then show this message
    Rectangle _rect;
    int _limitCharacters;
    enum TextInputState { Idle, TextEntered }
    TextInputState _state;
    int _cursorPosition;
    public Runnable onedit_func;

    public TextInput(String messageText, String text, String defaultText, Rectangle rect, int limitCharacters){
        _messageText = messageText;
        _text = text;
        _defaultText = defaultText;
        _rect = new Rectangle(rect);
        _limitCharacters = limitCharacters;
        _state = TextInputState.Idle;
        _cursorPosition = text.length();

        onedit_func = () -> {};
    }

    public String getText() {
        if(!_text.isEmpty())
            return _text;
        else
            return _defaultText;
    }

    public void handleEvents() {
        if(MyInput.processor.isTouchDown()){
            if(_rect.contains(MyInput.processor.getTouchPosition())){
                _state = TextInputState.TextEntered;
                Gdx.input.setOnscreenKeyboardVisible(true);
            }else{

                if(_state == TextInputState.TextEntered){
                    onedit_func.run();
                }

                _state = TextInputState.Idle;
                Gdx.input.setOnscreenKeyboardVisible(false);
            }
        }

        if(_state == TextInputState.TextEntered){
            while(MyInput.processor.isKeyTyped()){

                char key = MyInput.processor.getLastChar();

                if(key == '\r' || key == '\n') {
                    // enter
                    if(!_text.isEmpty() && !_text.trim().isEmpty()) {
                        System.out.println("Enter pressed: " + _text);
                        Gdx.input.setOnscreenKeyboardVisible(false);
                        _state = TextInputState.Idle;
                        onedit_func.run();
                    }
                }
                else if(key == '\b' && !_text.isEmpty() && _cursorPosition > 0){
                    // backspace
                    _text = _text.substring(0, _cursorPosition - 1) + _text.substring(_cursorPosition);
                    _cursorPosition--;
                }else {

                    if(key == ' ') {
                        if(_cursorPosition != 0 && _text.charAt(_cursorPosition - 1) != ' ') {
                            if(_cursorPosition < _limitCharacters){
                                _text = _text.substring(0, _cursorPosition) + key + _text.substring(_cursorPosition);
                                _cursorPosition++;
                            }
                        }
                    } else {

                        if(_cursorPosition<_limitCharacters){
                            _text = _text.substring(0, _cursorPosition) + key + _text.substring(_cursorPosition);
                            _cursorPosition++;
                        }

                    }

                }

            }
        }




    }

    public void update(){

    }

    public void draw() {

        // draw rect
        Renderer.spriteBatch.end();
        Renderer.shapeRenderer.setProjectionMatrix(Renderer.spriteBatch.getProjectionMatrix());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float c = 31.0f/255.0f;
        Renderer.shapeRenderer.setColor(c, c, c, 1.0f);
        Renderer.shapeRenderer.rect(_rect.x, _rect.y, _rect.width, _rect.height);
        Renderer.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        Renderer.spriteBatch.begin();

        // draw text
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.descriptionFont, _text);

        float textWidth = layout.width;
        float textHeight = Font.descriptionFont.getCapHeight();

        float x = _rect.x + (_rect.height - textHeight)/2;
        float y = _rect.y + (_rect.height + textHeight)/2;
        if(!_text.isEmpty())
            Font.descriptionFont.draw(Renderer.spriteBatch, _text, x, y);
        else if(_state == TextInputState.Idle)
            Font.descriptionFont.draw(Renderer.spriteBatch, _messageText, x, y);

        // draw cursor
        if(_state == TextInputState.TextEntered){
            if((int)(Time.currentTime*3) % 2 == 0 ) {
                Renderer.spriteBatch.end();
                Renderer.shapeRenderer.setProjectionMatrix(Renderer.spriteBatch.getProjectionMatrix());
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                Renderer.shapeRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
                int cursorHgh = (int)_rect.height * 3 / 4;
                Renderer.shapeRenderer.rect(x+textWidth, _rect.y+(_rect.height-cursorHgh)/2, 6, cursorHgh);
                Renderer.shapeRenderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                Renderer.spriteBatch.begin();
            }
        }
    }
}
