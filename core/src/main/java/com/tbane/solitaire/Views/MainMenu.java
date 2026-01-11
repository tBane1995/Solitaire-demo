package com.tbane.solitaire.Views;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tbane.solitaire.GUI.Font;
import com.tbane.solitaire.Game.Game;
import com.tbane.solitaire.Game.NoiseShader;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;
import com.tbane.solitaire.AssetsManager;
import com.tbane.solitaire.GUI.Button;
import com.tbane.solitaire.GUI.ButtonWithText;

public class MainMenu extends Layout {
    private final ArrayList<ButtonWithText> _buttons;


    public MainMenu() {
        super();

        ArrayList<String> btnTexts = new ArrayList<>();

        btnTexts.add("new game");
        btnTexts.add("highscores");
        btnTexts.add("instructions");
        btnTexts.add("about");
        btnTexts.add("exit");

        int btnWdt = 512;
        int btnHgh = 96;
        int padding = 32;

        _buttons = new ArrayList<>();
        int logoSize = 320;
        int logoPadding = 32;
        int hgh = -logoSize + logoPadding + btnTexts.size()*btnHgh - (btnTexts.size()-1)*padding;
        int start_y = Renderer.VIRTUAL_HEIGHT/2 + hgh/2;

        for(int i=0;i<btnTexts.size();i+=1) {
            ButtonWithText btn = new ButtonWithText (
                btnTexts.get(i),
                AssetsManager.getTexture("tex/menuButtonNormal.png"),
                AssetsManager.getTexture("tex/menuButtonHover.png"),
                AssetsManager.getTexture("tex/menuButtonPressed.png"),
                (Renderer.VIRTUAL_WIDTH-btnWdt)/2,
                start_y - i*btnHgh - i*padding,
                btnWdt,
                btnHgh
		    );
            _buttons.add(btn);
        }

        _buttons.get(0).onclick_func = () -> {
            MyInput.processor.reset();
            LayoutsManager.array.add(new Game());
        };

        _buttons.get(1).onclick_func = () -> LayoutsManager.array.add(new Highscores());

        _buttons.get(2).onclick_func = () -> LayoutsManager.array.add(new Instructions());

        _buttons.get(3).onclick_func = () -> LayoutsManager.array.add(new About());

        _buttons.get(4).onclick_func = () -> {
            Gdx.app.exit();
            System.exit(0);
        };

    }
    @Override
    public void handleEvents() {
        for(Button btn : _buttons){
            btn.handleEvents();
        }
    }

    @Override
    public void update() {

        for(Button btn : _buttons){
            btn.update();
        }
    }
    @Override
    public void draw() {

        // draw the wooden background
        Texture backgroundTexture = AssetsManager.getTexture("tex/mainBoard.png");
        if(backgroundTexture != null){
            Sprite background = new Sprite(backgroundTexture);
            background.setPosition(0,0 );
            background.draw(Renderer.spriteBatch);
        }

        // draw a Logo
        Texture logoTexture = AssetsManager.getTexture("tex/logo.png");
        if(logoTexture != null){
            Sprite logo = new Sprite(logoTexture);
            logo.setPosition(
                (float)Renderer.VIRTUAL_WIDTH/2 - logo.getWidth()/2,
                (float)Renderer.VIRTUAL_HEIGHT/2 - logo.getHeight()/2 + 256+128+32
            );

            Texture mask = AssetsManager.getTexture("tex/cards/noise.png");
            if(mask!= null){
                Renderer.spriteBatch.setShader(NoiseShader.shader);
                NoiseShader.shader.bind();
                NoiseShader.shader.setUniformi("u_mask", 1);
                mask.bind(1);
                logo.getTexture().bind(0);
                logo.draw(Renderer.spriteBatch);
            }

            Renderer.spriteBatch.setShader(null);
        }

        // draw "Main Menu"
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.titleFont, "Main Menu");

        float textWidth = layout.width;
        float textHeight = Font.titleFont.getCapHeight();

        float x = Renderer.VIRTUAL_WIDTH/2.0f - textWidth / 2f;
        float y = _buttons.get(0)._rect.y + _buttons.get(0)._rect.height + 112;

        Texture titleFrameTexture = AssetsManager.getTexture("tex/titleFrame.png");
        if(titleFrameTexture != null){
            Sprite frame = new Sprite(titleFrameTexture);
            frame.setOriginCenter();
            frame.setCenter(x + textWidth/2.0f, y-textHeight/2.0f);
            frame.draw(Renderer.spriteBatch);

            Font.titleFont.draw(Renderer.spriteBatch, "Main Menu", x, y);
        }

        Texture leftSignTexture = AssetsManager.getTexture("tex/mainMenuLeftSign.png");
        if(leftSignTexture != null){
            Sprite leftSign = new Sprite(leftSignTexture);
            leftSign.setOriginCenter();
            leftSign.setCenter(x - 80, y-textHeight/2.0f);
            leftSign.draw(Renderer.spriteBatch);
        }

        Texture rightSignTexture = AssetsManager.getTexture("tex/mainMenuRightSign.png");
        if(rightSignTexture!=null){
            Sprite rightSign = new Sprite(rightSignTexture);
            rightSign.setOriginCenter();
            rightSign.setCenter(x + textWidth + 80, y-textHeight/2.0f);
            rightSign.draw(Renderer.spriteBatch);
        }

        // draw the buttons
        for(Button btn : _buttons){
            btn.draw();
        }

        // ads panel
        Texture adsPanelTexture = AssetsManager.getTexture("tex/bottomPanel.png");
        if(adsPanelTexture != null){
            Sprite adsPanel = new Sprite(adsPanelTexture);
            adsPanel.setPosition(0, 0);
            adsPanel.draw(Renderer.spriteBatch);
        }
    }
}
