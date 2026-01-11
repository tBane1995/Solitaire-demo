package com.tbane.solitaire.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class NoiseShader {

    public static ShaderProgram shader;

    static {
        shader = new ShaderProgram(
            Gdx.files.internal("shs/vertex.glsl"),
            Gdx.files.internal("shs/mask.glsl")
        );

        if (!shader.isCompiled())
            System.out.println(shader.getLog());
    }

}
