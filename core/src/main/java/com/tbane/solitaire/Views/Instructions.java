package com.tbane.solitaire.Views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tbane.solitaire.AssetsManager;
import com.tbane.solitaire.GUI.Button;
import com.tbane.solitaire.GUI.Font;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;

public class Instructions extends Layout {

    private final Button _backBtn;
    private int panelWdt, panelHgh;
    private int padding;
    private int innerWdt, innerHgh;

    private String _text;

    public Instructions() {
        super();

        _backBtn = new Button(
            AssetsManager.getTexture("tex/backButtonNormal.png"),
            AssetsManager.getTexture("tex/backButtonHover.png"),
            AssetsManager.getTexture("tex/backButtonPressed.png"),
            32, Renderer.VIRTUAL_HEIGHT - 96 - 32, 96, 96
        );

        _backBtn.onclick_func = LayoutsManager::pop_back;

        panelWdt = 720;
        panelHgh = 1164;
        padding = 32;
        innerWdt = panelWdt - 2 * padding;
        innerHgh = panelHgh - 2 * padding;


        _text =
            "The goal of the game is to move\n"
            + "all cards to the Foundations,\n"
            +"building each pile by suit from\n"
            + "Ace to King.\n"
            + "\n"
            +"Cards can be moved\n"
            +"between the Tableaus in\n"
            +"descending order and alternating\n"
            +"colors.\n"
            +"\n"
            +"Only a King can be placed on an\n"
            +"empty tableau. Draw cards from\n"
            +"the Stock to the Waste, and use\n"
            +"the top card of the Waste when\n"
            +"possible.\n"
            +"\n"
            +"You can place cards\n"
            +"onto the Foundations starting\n"
            +"with an Ace and continuing in\n"
            +"ascending order.\n"
            +"\n"
            +"The game is won when all cards\n"
            +"are placed in the Foundations.";
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
        layout.setText(Font.titleFont, "Instructions");

        float textWidth = layout.width;
        float textHeight = Font.titleFont.getCapHeight();

        float x = Renderer.VIRTUAL_WIDTH/2.0f - textWidth / 2f + 32;
        float y = _backBtn._rect.y + _backBtn._rect.height/2.0f;

        Texture titleFrameTexture = AssetsManager.getTexture("tex/titleFrame.png");
        if(titleFrameTexture != null){
            Sprite frame = new Sprite(titleFrameTexture);
            frame.setOriginCenter();
            frame.setCenter(x + textWidth/2.0f, y);
            frame.draw(Renderer.spriteBatch);

            Font.titleFont.draw(Renderer.spriteBatch, "Instructions", x, y+textHeight/2.0f);
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
        Texture adsPanelTexture = AssetsManager.getTexture("tex/bottomPanel.png");
        if(adsPanelTexture != null){
            Sprite adsPanel = new Sprite(adsPanelTexture);
            adsPanel.setPosition(0, 0);
            adsPanel.draw(Renderer.spriteBatch);
        }
    }
}
