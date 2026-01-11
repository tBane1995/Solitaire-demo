package com.tbane.solitaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Renderer {

    public static int VIRTUAL_WIDTH = 720;
    public static int VIRTUAL_HEIGHT = 1612;
    public static OrthographicCamera camera2D;
    public static PerspectiveCamera camera3D;
    public static SpriteBatch spriteBatch = new SpriteBatch();    // for draw all stuff
    public static ModelBatch modelBatch = new ModelBatch();
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();    // for draw only the colored shapes (dark shape of bubbles)

    public static Viewport viewport;

    static {
        camera2D = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera2D);

        camera2D.position.set( (float)Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2, 0);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera3D = new PerspectiveCamera(67, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

    }

    public static void resize(int width, int height) {
        viewport.update(width, height, true);
        camera2D.update();
        spriteBatch.setProjectionMatrix(camera2D.combined);
    }

    public static void begin2D() {
        spriteBatch.begin();
    }

    public static void end2D() {
        spriteBatch.end();
    }

    public static void begin3D() {modelBatch.begin(camera3D);}
    public static void end3D() {modelBatch.end();}

}
