package com.tbane.solitaire.Views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tbane.solitaire.GUI.Button;
import com.tbane.solitaire.GUI.Font;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;
import com.tbane.solitaire.AssetsManager;

public class About extends Layout {

    private final Button _backBtn;
    private int panelWdt, panelHgh;
    private int padding;
    private int innerWdt, innerHgh;
    private String _text;
    public About() {
        super();

        _backBtn = new Button(
            AssetsManager.getTexture("tex/backButtonNormal.png"),
            AssetsManager.getTexture("tex/backButtonHover.png"),
            AssetsManager.getTexture("tex/backButtonPressed.png"),
            32, Renderer.VIRTUAL_HEIGHT - 96 - 32, 96, 96
        );

        _backBtn.onclick_func = () -> { LayoutsManager.pop_back(); };

        panelWdt = 720;
        panelHgh = 1100;
        padding = 32;
        innerWdt = panelWdt - 2 * padding;
        innerHgh = panelHgh - 2 * padding;

        _text = "Solitaire v1.0\n"
            + "\n"
            + "Game programmed by tBane\n"
            + "\n"
            + "Graphics:\n"
            + "Cards - Puzzle Galactica\n"
            + "Award Cup - Ninoshik\n"
            + "Sad Emoji - Arlan Trindade\n";

    }

    @Override
    public void handleEvents() {
        _backBtn.handleEvents();

        if(MyInput.processor.isBackPressed()){
            LayoutsManager.pop_back();
            MyInput.processor.reset();
        }


    }

    @Override
    public void update() {
        _backBtn.update();
    }

    @Override
    public void draw() {
        Texture backgroundTexture = AssetsManager.getTexture("tex/mainBoard.png");
        if(backgroundTexture != null){
            Sprite background = new Sprite(backgroundTexture);
            background.setPosition(0,0 );
            background.draw(Renderer.spriteBatch);
        }

        _backBtn.draw();

        // draw text "Instructions"
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.titleFont, "About");

        float textWidth = layout.width;
        float textHeight = Font.titleFont.getCapHeight();

        float x = Renderer.VIRTUAL_WIDTH/2.0f - textWidth / 2f + 32;
        float y = _backBtn._rect.y + _backBtn._rect.height/2.0f;

        Texture titleFrameTexture = AssetsManager.getTexture("tex/titleFrame.png");
        if(titleFrameTexture!=null){
            Sprite frame = new Sprite(titleFrameTexture);
            frame.setOriginCenter();
            frame.setCenter(x + textWidth/2.0f, y);
            frame.draw(Renderer.spriteBatch);

            Font.titleFont.draw(Renderer.spriteBatch, "About", x, y+textHeight/2.0f);
        }


        // draw a panel
        Texture panelTexture = AssetsManager.getTexture("tex/panel.png");
        if(panelTexture != null){
            Sprite panel = new Sprite(panelTexture);
            float panelX = padding;
            float panelY = Renderer.VIRTUAL_HEIGHT - panel.getTexture().getHeight() - (96 + 2 * padding);
            panel.setPosition(panelX, panelY);
            panel.draw(Renderer.spriteBatch);

            Font.descriptionFont.draw(Renderer.spriteBatch, _text, panelX + padding, Renderer.VIRTUAL_HEIGHT - (96 + 2 * padding) - padding);
        }


        // ads panel
        Texture bottomPanelTexture = AssetsManager.getTexture("tex/bottomPanel.png");
        if(bottomPanelTexture != null){
            Sprite adsPanel = new Sprite(bottomPanelTexture);
            adsPanel.setPosition(0, 0);
            adsPanel.draw(Renderer.spriteBatch);
        }

    }
}
