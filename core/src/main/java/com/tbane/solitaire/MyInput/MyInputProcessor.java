package com.tbane.solitaire.MyInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tbane.solitaire.Renderer;

public class MyInputProcessor extends InputAdapter {
    private final OrthographicCamera camera;
    enum InputType { None, TouchDown, TouchMoved, TouchUp, BackPressed }
    InputType type;
    public Vector3 lastTouchPos = new Vector3();

    char lastChar;
    boolean keyTyped;
    public MyInputProcessor(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            type = InputType.BackPressed;
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //System.out.println("Touch Down: " + Integer.toString(pointer) + "\n");
        lastTouchPos.set(screenX, screenY, 0);
        camera.unproject(lastTouchPos);
        type = InputType.TouchDown;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //System.out.println("Touch Up: " + Integer.toString(pointer) + "\n");
        lastTouchPos.set(screenX, screenY, 0);
        camera.unproject(lastTouchPos);
        type = InputType.TouchUp;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        camera.unproject(lastTouchPos.set(screenX, screenY, 0));
        type = InputType.TouchMoved;
        return true;
    }

    public boolean isTouchDown() {
        return type == InputType.TouchDown;
    }
    public boolean isTouchUp() {
        return type == InputType.TouchUp;
    }
    public boolean isTouchMoved() {
        return type == InputType.TouchMoved;
    }
    public boolean isBackPressed() {
        return type == InputType.BackPressed;
    }

    public Vector2 getTouchPosition() { return new Vector2(getTouchX(), getTouchY()); }

    public float getTouchX() {
        float normalizedX = (lastTouchPos.x + 1f) * 0.5f; // 0..1
        float scale = Math.min(
            (float)Gdx.graphics.getWidth() / Renderer.VIRTUAL_WIDTH,
            (float)Gdx.graphics.getHeight() / Renderer.VIRTUAL_HEIGHT
        );
        float offsetX = (Gdx.graphics.getWidth() - Renderer.VIRTUAL_WIDTH * scale) * 0.5f;
        return (normalizedX * Gdx.graphics.getWidth() - offsetX) / scale;
    }

    public float getTouchY() {
        float normalizedY = (lastTouchPos.y + 1f) * 0.5f; // 0..1
        float scale = Math.min(
            (float)Gdx.graphics.getWidth() / Renderer.VIRTUAL_WIDTH,
            (float)Gdx.graphics.getHeight() / Renderer.VIRTUAL_HEIGHT
        );
        float offsetY = (Gdx.graphics.getHeight() - Renderer.VIRTUAL_HEIGHT * scale) * 0.5f;
        return (normalizedY * Gdx.graphics.getHeight() - offsetY) / scale;
    }

    public void reset() {
        type = InputType.None;
    }
    @Override
    public boolean keyTyped(char character) {

        // ignoruj śmieci
        if (character == 0 || character == 65535)
            return false;

        lastChar = character;
        keyTyped = true;
        return true;
    }

    public boolean isKeyTyped() {
        return keyTyped;
    }

    public char getLastChar() {
        char c = lastChar;
        lastChar = '\0';
        keyTyped = false;
        return c;
    }
}
