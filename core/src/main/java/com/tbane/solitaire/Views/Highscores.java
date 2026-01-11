package com.tbane.solitaire.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tbane.solitaire.AssetsManager;
import com.tbane.solitaire.GUI.Button;
import com.tbane.solitaire.GUI.Font;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;
import com.tbane.solitaire.Time;

import java.util.ArrayList;

public class Highscores extends Layout {

    private Button _backBtn;
    private int panelWdt, panelHgh;
    private int padding;
    private int innerWdt, innerHgh;

    private ArrayList<String> _names;
    private ArrayList<Integer> _times;
    private ArrayList<Integer> _moves;

    public Highscores() {
        super();

        _backBtn = new Button(
            AssetsManager.getTexture("tex/backButtonNormal.png"),
            AssetsManager.getTexture("tex/backButtonHover.png"),
            AssetsManager.getTexture("tex/backButtonPressed.png"),
            32, Renderer.VIRTUAL_HEIGHT - 96 - 32, 96, 96
        );

        _backBtn.onclick_func = LayoutsManager::pop_back;

        panelWdt = 720;
        panelHgh = 1100;
        padding = 32;
        innerWdt = panelWdt - 2 * padding;
        innerHgh = panelHgh - 2 * padding;

        Preferences prefs = Gdx.app.getPreferences("highscores");

        _names = new ArrayList<>();
        _times = new ArrayList<>();
        _moves = new ArrayList<>();

        for(int i=0;i<15;i+=1){
            _names.add(prefs.getString("name" + i, "-"));
            _times.add(prefs.getInteger("time" + i, -1));
            _moves.add(prefs.getInteger("moves" + i, -1));
        }

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
        layout.setText(Font.titleFont, "Highscores");

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

            Font.titleFont.draw(Renderer.spriteBatch, "Highscores", x, y+textHeight/2.0f);

        }

        // draw a panel
        Texture panelTexture = AssetsManager.getTexture("tex/panel.png");
        if(panelTexture != null){
            Sprite panel = new Sprite(panelTexture);
            float panelX = padding;
            float panelY = Renderer.VIRTUAL_HEIGHT - panel.getTexture().getHeight() - (96 + 2 * padding);
            panel.setPosition(panelX, panelY);
            panel.draw(Renderer.spriteBatch);
        }

        // ads panel
        Texture adsPanelTexture = AssetsManager.getTexture("tex/bottomPanel.png");
        if(adsPanelTexture != null){
            Sprite adsPanel = new Sprite(adsPanelTexture);
            adsPanel.setPosition(0, 0);
            adsPanel.draw(Renderer.spriteBatch);
        }

        // draw a award cup
        Texture awardCupTexture = AssetsManager.getTexture("tex/awardCup.png");
        if(awardCupTexture != null){
            Sprite awardCup = new Sprite(awardCupTexture);
            awardCup.setOriginCenter();
            awardCup.setScale(0.35f);
            awardCup.setCenter(Renderer.VIRTUAL_WIDTH/2.0f, Renderer.VIRTUAL_HEIGHT - titleFrameTexture.getHeight() - 2*32 - 256);
            awardCup.draw(Renderer.spriteBatch);
        }

        // parameters for draw a list
        float dy = 24;
        float line_height = 36;
        float x1 = 64;
        float x2 = 256+16;
        float x3 = 480;
        float start_y1 = adsPanelTexture.getHeight() + 32 + 15*line_height + dy + 64 + 20 + Font.rankingTitleFont.getCapHeight() + Font.rankingHighscoreFont.getCapHeight();
        float start_y2 = adsPanelTexture.getHeight() + 32 + 15*line_height + dy + 64;

        // draw a shadow rectangles
        Renderer.spriteBatch.end();
        Renderer.shapeRenderer.setProjectionMatrix(Renderer.spriteBatch.getProjectionMatrix());

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float c = 31.0f/255.0f;
        Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Renderer.shapeRenderer.setColor(c, c, c, 0.9f);
        float wdt = panelWdt - 2 * x1 + 2 * 16;
        float hgh = Font.rankingHighscoreFont.getCapHeight() + Font.rankingTitleFont.getCapHeight() + 32 + 15 * line_height;
        Renderer.shapeRenderer.rect(x1 - 16, start_y1 -Font.rankingHighscoreFont.getLineHeight() , wdt, Font.rankingHighscoreFont.getLineHeight());
        Renderer.shapeRenderer.rect(x1 - 16, start_y2 - hgh + 16, wdt, hgh);
        Renderer.shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        Renderer.spriteBatch.begin();

        // draw a highscore list
        float wdt1, wdt2, wdt3;
        layout = new GlyphLayout();
        layout.setText(Font.rankingHighscoreFont, "HIGHSCORES");
        wdt = layout.width;
        layout.setText(Font.rankingTitleFont, "NAME");
        wdt1 = layout.width;
        layout.setText(Font.rankingTitleFont, "TIME");
        wdt2 = layout.width;
        layout.setText(Font.rankingTitleFont, "MOVES");
        wdt3 = layout.width;

        Font.rankingHighscoreFont.draw(Renderer.spriteBatch, "HIGHSCORES", (Renderer.VIRTUAL_WIDTH-wdt)/2.0f, start_y1 - Font.rankingHighscoreFont.getLineHeight()/4);
        Font.rankingTitleFont.draw(Renderer.spriteBatch, "NAME", x1, start_y2);
        Font.rankingTitleFont.draw(Renderer.spriteBatch, "TIME", x2, start_y2);
        Font.rankingTitleFont.draw(Renderer.spriteBatch, "MOVES", x3, start_y2);

        for(int i=0;i<15;i+=1){

            layout = new GlyphLayout();
            //textHeight = Font.rankingFont.getCapHeight();

            layout.setText(Font.rankingFont, _names.get(i));
            textWidth = layout.width;
            Font.rankingFont.draw(Renderer.spriteBatch, _names.get(i), x1+(wdt1-textWidth)/2, start_y2 - dy - line_height*(i+1));

            String value;
            if(_times.get(i) == -1)
                value = "-";
            else{
                value = Time.generateTimeString(_times.get(i));
            }


            layout.setText(Font.rankingFont, value);
            textWidth = layout.width;
            Font.rankingFont.draw(Renderer.spriteBatch, value, x2+(wdt2-textWidth)/2, start_y2 - dy - line_height*(i+1));

            if(_times.get(i) == -1)
                value = "-";
            else
                value = Integer.toString(_moves.get(i));

            layout.setText(Font.rankingFont, value);
            textWidth = layout.width;
            Font.rankingFont.draw(Renderer.spriteBatch, value, x3+(wdt3-textWidth)/2, start_y2 - dy  - line_height*(i+1));
        }

    }
}
