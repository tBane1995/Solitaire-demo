package com.tbane.solitaire.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tbane.solitaire.AssetsManager;
import com.tbane.solitaire.GUI.AnimatedButtonWithText;
import com.tbane.solitaire.GUI.Button;
import com.tbane.solitaire.GUI.ButtonWithText;
import com.tbane.solitaire.GUI.Font;
import com.tbane.solitaire.GUI.TextInput;
import com.tbane.solitaire.MyInput.MyInput;
import com.tbane.solitaire.Renderer;
import com.tbane.solitaire.Time;
import com.tbane.solitaire.Views.Layout;
import com.tbane.solitaire.Views.LayoutsManager;

import java.util.ArrayList;
import java.util.Random;

import jdk.javadoc.internal.doclets.formats.html.Table;

public class Game extends Layout {

    enum GameStates { Start, Game, AutoComplete, UpdateHighscores, GameFailed, GameComplete, End }
    GameStates _state;
    private final Button _backBtn;
    enum showedBtnType { None, AutoComplete, NoMove }
    private showedBtnType showedBtn = showedBtnType.None;
    private final AnimatedButtonWithText _autoCompleteBtn, _noMovesBtn;
    private final ButtonWithText _restartBtn;
    public static int _moves = 0;
    public static float _startTime = 0;
    public static float _gameTime = 0;
    private final TextInput _playerNameInput;

    /// ///////////////////////////
    ArrayList<String> _rankingNames = new ArrayList<>();
    ArrayList<Integer> _rankingTimes = new ArrayList<>();
    ArrayList<Integer> _rankingMoves = new ArrayList<>();
    private int _ranking = 15;
    /// ///////////////

    public Game() {
        super();

        _backBtn = new Button(
            AssetsManager.getTexture("tex/backButtonNormal.png"),
            AssetsManager.getTexture("tex/backButtonHover.png"),
            AssetsManager.getTexture("tex/backButtonPressed.png"),
            32, Renderer.VIRTUAL_HEIGHT - 96 - 32, 96, 96
        );

        _backBtn.onclick_func = LayoutsManager::pop_back;

        _autoCompleteBtn = new AnimatedButtonWithText(
            "auto-complete",
            AssetsManager.getTexture("tex/menuButtonNormal.png"),
            AssetsManager.getTexture("tex/menuButtonHover.png"),
            AssetsManager.getTexture("tex/menuButtonHover.png"),
            AssetsManager.getTexture("tex/menuButtonPressed.png"),
            Renderer.VIRTUAL_WIDTH/2 - 256,
            Renderer.VIRTUAL_HEIGHT/2 - 512 + 32,
            512,
            96
        );

        _noMovesBtn = new AnimatedButtonWithText(
            "no more moves",
            AssetsManager.getTexture("tex/menuButtonNormal.png"),
            AssetsManager.getTexture("tex/menuButtonHover.png"),
            AssetsManager.getTexture("tex/menuButtonHover.png"),
            AssetsManager.getTexture("tex/menuButtonPressed.png"),
            Renderer.VIRTUAL_WIDTH/2 - 256,
            Renderer.VIRTUAL_HEIGHT/2 - 512 + 32,
            512,
            96
        );

        _restartBtn = new ButtonWithText (
            "restart",
            AssetsManager.getTexture("tex/panelButtonNormal.png"),
            AssetsManager.getTexture("tex/panelButtonHover.png"),
            AssetsManager.getTexture("tex/panelButtonPressed.png"),
            (Renderer.VIRTUAL_WIDTH)/2  - 224/2,
            (Renderer.VIRTUAL_HEIGHT-96)/2 - 64 + 16,
            224,
            96
        );

        Rectangle rect = new Rectangle(
            (Renderer.VIRTUAL_WIDTH)/2.0f - 416.0f/2,
            (Renderer.VIRTUAL_HEIGHT)/2.0f + 160,
            416,
            60
        );

        _playerNameInput = new TextInput(
            "type player name...",
            "",
            "player",
            rect,
            6);

        _restartBtn.onclick_func = () -> _state = GameStates.Start;


        _autoCompleteBtn.onclick_func = () -> _state = GameStates.AutoComplete;

        _noMovesBtn.onclick_func = () -> _state = GameStates.GameFailed;

        _playerNameInput.onedit_func = () -> {
            editNameInHighscores();
            saveHighscores();
        };

        // calculate the card size, dy and card padding
        int cardWdt = (Renderer.VIRTUAL_WIDTH-24)/7;

        Foundation._dx = (Renderer.VIRTUAL_WIDTH - (cardWdt * 7)) / 2f;
        Foundation._dy = 160 + 16;

        Tableau._dx = Foundation._dx;
        Tableau._dy = 320 + 16;

        Card._padding = 4;
        cardWdt = (int)((float)cardWdt - 2 * Card._padding);
        Card._size = new Vector2((float)cardWdt, 356.0f*(cardWdt/256.0f));
        Card._dy = 36;

        _state = GameStates.Start;

    }

    private ArrayList<Card> generateCards() {
        ArrayList<Card> cards = new ArrayList<>();

        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(Card.cardColor.Clover, i));
            //cards.add(new Card(Card.cardColor.Tile, i));
            //cards.add(new Card(Card.cardColor.Pike, i));
            //cards.add(new Card(Card.cardColor.Heart, i));
        }

        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(Card.cardColor.Pike, i));
        }

        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(Card.cardColor.Tile, i));
        }

        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(Card.cardColor.Heart, i));
        }
        return cards;
    }

    private void shuffleTheCards(ArrayList<Card> cards){
        // shuffle the cards
        Random random = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1); // 0..i

            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    private void startGame() {
        ArrayList<Card> cards = generateCards();
        shuffleTheCards(cards);

        Foundations.create();
        Tableaus.create();

        Stock.create(new Vector2(
            Foundation._dx + (Tableaus.tableaus.size()-1) * (Card._size.x + 2*Card._padding) + Card._padding,
            -Foundation._dy + Renderer.VIRTUAL_HEIGHT - Card._size.y - Card._padding
        ));

        Stock.addCards(cards);
        Tableaus.setTheCardsFromTheStock();
/*
        Tableaus.tableaus.get(0).addCard(cards.get(12));
        Tableaus.tableaus.get(0).addCard(cards.get(11+26));
        Tableaus.tableaus.get(0).addCard(cards.get(10));
        Tableaus.tableaus.get(0).addCard(cards.get(9+26));
        Tableaus.tableaus.get(0).addCard(cards.get(8));

        Tableaus.tableaus.get(1).addCard(cards.get(12+13));
        Tableaus.tableaus.get(1).addCard(cards.get(11+39));
        Tableaus.tableaus.get(1).addCard(cards.get(10+13));
        Tableaus.tableaus.get(1).addCard(cards.get(9+39));
        Tableaus.tableaus.get(1).addCard(cards.get(8+13));
        Tableaus.tableaus.get(1).addCard(cards.get(7+39));

        for(Tableau t : Tableaus.tableaus){
            for(Card c : Tableaus.tableaus.get(0)._cards){
                c._isOpen = true;
            }
        }
*/

        // Create the Waste on the Top Right (left)
        Waste.create(new Vector2(
            Foundation._dx + (Tableaus.tableaus.size()-2) * (Card._size.x + 2*Card._padding) + Card._padding,
            -Foundation._dy + Renderer.VIRTUAL_HEIGHT - Card._size.y - Card._padding
        ));

        Animations.create();

        _startTime = Time.currentTime;
        _gameTime = 0;
        _moves = 0;

        showedBtn = showedBtnType.None;
    }

    private boolean readyToAutoComplete() {
        for(Tableau tableau : Tableaus.tableaus){
            for(Card card : tableau._cards){
                if(!card._isOpen)
                    return false;
            }
        }

        return true;
    }

    private boolean isNextMove() {

        // open the card from Tableaus
        for(Tableau tableau : Tableaus.tableaus){
            Card card = tableau.getLastCard();
            if(card != null){
                if(!card._isOpen){
                    System.out.println("open the card from Tableaus");
                    return true;
                }
            }
        }

        // move the cards from Tableaus to Foundations
        for(Tableau tableau : Tableaus.tableaus){
           Card card = tableau.getLastCard();
           if(card != null){
               for(Foundation foundation : Foundations.foundations){
                   if(foundation.cardIsAcceptable(card)){
                       System.out.println("move the cards from Tableaus to Foundations");
                       return true;
                   }

               }
           }
        }

        // move the cards from Stock to Foundations or Tableaus
        for(Card card : Stock._cards){
            for(Foundation foundation : Foundations.foundations){
                if(foundation.cardIsAcceptable(card)){
                    System.out.println("move the cards from Stock to Foundations");
                    return true;
                }

            }

            for(Tableau tableau : Tableaus.tableaus){
                if(tableau.cardIsAcceptable(card)){
                    System.out.println("move the cards from Stock to Tableaus");
                    return true;
                }
            }
        }

        // move the cards from Waste to Foundations or Tableaus
        for(Card card : Waste._cards){
            for(Foundation foundation : Foundations.foundations){
                if(foundation.cardIsAcceptable(card)){
                    System.out.println("move the cards from Waste to Foundations");
                    return true;
                }

            }

            for(Tableau tableau : Tableaus.tableaus){
                if(tableau.cardIsAcceptable(card)){
                    System.out.println("move the cards from Waste to Tableaus");
                    return true;
                }
            }
        }

        // move the cards from Tableau to Tableau (ANTI-LOOP - how?)
        for (Tableau from : Tableaus.tableaus) {

            if (from._cards.isEmpty())
                continue;

            if(from._cards.get(0)._isOpen && from._cards.get(0)._value == 13)
                continue;

            for(int i=0;i<from._cards.size();i++){
                for(Tableau to : Tableaus.tableaus){
                    if(from == to){
                        continue;
                    }

                    if(to._cards.isEmpty())
                        continue;

                    Card prevCard;
                    if(i==0)
                        prevCard = null;
                    else
                        prevCard = from._cards.get(i-1);

                    boolean prevCardIsOpen;
                    if(prevCard == null)
                        prevCardIsOpen = false;
                    else
                        prevCardIsOpen = prevCard._isOpen;

                    boolean cardsAreEqual;
                    if(prevCard == null)
                        cardsAreEqual = false;
                    else
                        cardsAreEqual = prevCard._value == to.getLastCard()._value;

                    boolean cardsAreOtherColor = false;

                    if(prevCard == null){
                        cardsAreOtherColor = true;
                    }
                    else{
                        if (prevCard._color == Card.cardColor.Heart || prevCard._color == Card.cardColor.Tile) {
                            cardsAreOtherColor =  (to.getLastCard()._color == Card.cardColor.Pike || to.getLastCard()._color == Card.cardColor.Clover);
                        }

                        if (prevCard._color == Card.cardColor.Pike || prevCard._color == Card.cardColor.Clover) {
                            cardsAreOtherColor =  (to.getLastCard()._color == Card.cardColor.Heart || to.getLastCard()._color == Card.cardColor.Tile);
                        }
                    }

                    String color1, color2;
                    color1 = color2 = "";

                    switch(from._cards.get(i)._color){
                        case Heart: color1 = "Heart"; break;
                        case Tile: color1 = "Tile"; break;
                        case Pike: color1 = "Pike"; break;
                        case Clover: color1 = "Clover"; break;
                    }

                    switch(to.getLastCard()._color){
                        case Heart: color2 = "Heart"; break;
                        case Tile: color2 = "Tile"; break;
                        case Pike: color2 = "Pike"; break;
                        case Clover: color2 = "Clover"; break;
                    }

                    if( from._cards.get(i)._isOpen && !prevCardIsOpen && to.cardIsAcceptable(from._cards.get(i))){
                        System.out.println("move the cards from Tableau to Tableau1");
                        System.out.println(from._cards.get(i)._value + color1 + from._id + " to " + to.getLastCard()._value + color2 + to._id);
                        return true;
                    }

                    if(prevCardIsOpen && cardsAreEqual && cardsAreOtherColor && to.cardIsAcceptable(from._cards.get(i))){
                        System.out.println("move the cards from Tableau to Tableau2");
                        System.out.println(from._cards.get(i)._value + color1 + from._id + " to " + to.getLastCard()._value + color2 + to._id);
                        return true;
                    }
                }
            }
        }

        // move the cards from Hands to Foundations or Tableaus
        if(!CardsInHand._cards.isEmpty()){
            Card card = CardsInHand._cards.get(0);

            if(card != null){
                for(Foundation foundation : Foundations.foundations){
                    if(foundation.cardIsAcceptable(card))
                        return true;
                }

                for(Tableau tableau : Tableaus.tableaus){
                    if(tableau.cardIsAcceptable(card)){
                        return true;
                    }
                }
            }
        }

        return false;

    }

    private void autoComplete() {



        // Open the hidden Cards
        for(Tableau tableau : Tableaus.tableaus){
            Card lastCard = tableau.getLastCard();
            if(lastCard != null){
                if(!lastCard._isOpen){
                    lastCard._isOpen = true;
                    Rectangle startRect = lastCard._rect;
                    Rectangle endRect = lastCard._rect;
                    Animations.addAnimation(lastCard, startRect, endRect);
                    _moves += 1;
                    return;
                }
            }
        }

        // Add the card from Tableaus to Foundation
        ArrayList<Card> acceptableCards = new ArrayList<>();
        ArrayList<Tableau> tabs = new ArrayList<>();

        for(Tableau tableau : Tableaus.tableaus){
            Card lastCard = tableau.getLastCard();
            if(lastCard != null){
                for(Foundation foundation : Foundations.foundations){
                    if(foundation.cardIsAcceptable(lastCard)){
                        acceptableCards.add(lastCard);
                        tabs.add(tableau);
                    }
                }
            }
        }

        if(!acceptableCards.isEmpty()){
            int id = (int)(Math.random() * acceptableCards.size());
            Card card = acceptableCards.get(id);
            for(Foundation foundation : Foundations.foundations){
                if(foundation.cardIsAcceptable(card)){
                    Rectangle startRect = new Rectangle(card._rect);
                    tabs.get(id).removeCard(tabs.get(id)._cards.size()-1);
                    foundation.addCard(card);
                    Rectangle endRect = new Rectangle(card._rect);
                    Animations.addAnimation(card, startRect, endRect);
                    _moves += 1;
                    return;
                }
            }
        }


        Card lastCard = Waste.getLastCard();

        if(lastCard != null){
            for(Foundation foundation : Foundations.foundations) {
                if(foundation.cardIsAcceptable(lastCard)){
                    Rectangle startRect = new Rectangle(Waste._rect);
                    Waste._cards.remove(Waste._cards.size()-1);
                    foundation.addCard(lastCard);
                    Rectangle endRect = new Rectangle(lastCard._rect);
                    Animations.addAnimation(lastCard, startRect, endRect);
                    _moves += 1;
                    return;
                }
            }
        }

        lastCard = Stock.getLastCard();

        if(lastCard != null){
            Rectangle startRect = Stock._rect;
            Waste.addCard(lastCard);
            Rectangle endRect = Waste._rect;
            Stock._cards.remove(Stock._cards.size() - 1);
            Animations.addAnimation(lastCard, startRect, endRect);
            lastCard._isOpen = true;
        }
        else{
            while (!Waste._cards.isEmpty()) {
                Rectangle startRect = Waste._rect;
                Card card = Waste._cards.get(Waste._cards.size() - 1);
                Waste._cards.remove(Waste._cards.size() - 1);
                Stock.addCard(card);
                Rectangle endRect = Stock._rect;
                Animations.addAnimation(card, startRect, endRect);
                _moves += 1;
                card._isOpen = false;
            }
        }

    }

    private boolean endGame() {
        for(Foundation foundation : Foundations.foundations){
            if(foundation._cards.size() != 13){
                return false;
            }
        }

        return true;
    }

    private void findRankingPosition() {

        int newTime = (int)_gameTime;
        int insertIndex = _rankingTimes.size();

        for (int i = 0; i < 15; i++) {
            if (i >= _rankingTimes.size()) {
                insertIndex = i;
                break;
            } else if (newTime < _rankingTimes.get(i)) {
                insertIndex = i;
                break;
            }
        }

        _ranking = insertIndex;
    }
    private void loadHighscores() {
        Preferences prefs = Gdx.app.getPreferences("highscores");

        _rankingNames = new ArrayList<>();
        _rankingTimes = new ArrayList<>();
        _rankingMoves = new ArrayList<>();

        for (int i = 0; i < 15; i++) {

            if (!prefs.contains("name" + i))
                break; // nie ma więcej zapisanych wyników

            _rankingNames.add(prefs.getString("name" + i));
            _rankingTimes.add(prefs.getInteger("time" + i));
            _rankingMoves.add(prefs.getInteger("moves" + i));
        }
    }

    private void addToHighscores() {
        if (_ranking <= _rankingNames.size()) {
            _rankingNames.add(_ranking, _playerNameInput.getText());
            _rankingTimes.add(_ranking, (int)_gameTime);
            _rankingMoves.add(_ranking, _moves);
        }

        while (_rankingNames.size() > 15) {
            int last = _rankingNames.size() - 1;
            _rankingNames.remove(last);
            _rankingTimes.remove(last);
            _rankingMoves.remove(last);
        }
    }

    private void editNameInHighscores() {
        _rankingNames.set(_ranking, _playerNameInput.getText());
    }
    private void saveHighscores() {
        Preferences prefs = Gdx.app.getPreferences("highscores");

        // Zapis do Preferences
        for (int i = 0; i < _rankingNames.size(); i++) {
            prefs.putString("name" + i, _rankingNames.get(i));
            prefs.putInteger("time" + i, _rankingTimes.get(i));
            prefs.putInteger("moves" + i, _rankingMoves.get(i));
        }

        prefs.flush();
    }
    private void drawTimerAndMoves() {

        String text;
        GlyphLayout layout;
        float textWidth;
        float textHeight;
        float x;
        float y;

        //

        text = "Time";
        layout = new GlyphLayout();
        layout.setText(Font.gameTopTextsBigFont,text);
        textWidth = layout.width;
        textHeight = Font.gameTopTextsBigFont.getCapHeight();
        x = (256) - textWidth/2;
        y = Renderer.VIRTUAL_HEIGHT - textHeight - 24;
        Font.gameTopTextsBigFont.draw(Renderer.spriteBatch, text, x, y);


        text = Time.generateTimeString(_gameTime);
        layout = new GlyphLayout();
        layout.setText(Font.gameTopTextsSmallFont,text);
        textWidth = layout.width;
        textHeight = Font.gameTopTextsSmallFont.getCapHeight();
        x = (256) - textWidth/2;
        y = Renderer.VIRTUAL_HEIGHT - textHeight - 80;
        Font.gameTopTextsSmallFont.draw(Renderer.spriteBatch, text, x, y);


        /// //////

        text = "Moves";
        layout = new GlyphLayout();
        layout.setText(Font.gameTopTextsBigFont,text);
        textWidth = layout.width;
        textHeight = Font.gameTopTextsBigFont.getCapHeight();
        x = Renderer.VIRTUAL_WIDTH - 196 - (textWidth)/2;
        y = Renderer.VIRTUAL_HEIGHT - textHeight - 24;
        Font.gameTopTextsBigFont.draw(Renderer.spriteBatch, text, x, y);

        text = Integer.toString(_moves);
        layout = new GlyphLayout();
        layout.setText(Font.gameTopTextsSmallFont,text);
        textWidth = layout.width;
        textHeight = Font.gameTopTextsSmallFont.getCapHeight();
        x = Renderer.VIRTUAL_WIDTH - 196 - (textWidth)/2;
        y = Renderer.VIRTUAL_HEIGHT - textHeight - 80;
        Font.gameTopTextsSmallFont.draw(Renderer.spriteBatch, text, x, y);
    }

    private void drawGameFailedScreen() {
        float x = (Renderer.VIRTUAL_WIDTH)/2.0f;
        float y = (Renderer.VIRTUAL_HEIGHT)/2.0f + 128.0f;

        Texture smallPanelTexture = AssetsManager.getTexture("tex/smallPanel.png");
        if(smallPanelTexture != null){
            Sprite smallPanel = new Sprite(smallPanelTexture);
            smallPanel.setSize(512, 512);
            smallPanel.setCenter(x, y);
            smallPanel.draw(Renderer.spriteBatch);
        }

        // dy for movement of everything on the panel
        float dy = 16;
        GlyphLayout layout;
        float textWdt;
        float textHgh;
        String text = "no moves";

        layout = new GlyphLayout();
        layout.setText(Font.endScreenTopFont,text);
        textWdt = layout.width;
        textHgh = Font.endScreenTopFont.getCapHeight();
        Font.endScreenTopFont.draw(Renderer.spriteBatch, text, (x - textWdt/2.0f), (y + textHgh + 160 - dy));

        Texture emojiTexture = AssetsManager.getTexture("tex/sadEmoji.png");
        if(emojiTexture != null){
            Sprite emoji = new Sprite(emojiTexture);
            emoji.setSize(128, 128);
            emoji.setCenter(x, y - dy + 80);
            emoji.draw(Renderer.spriteBatch);
        }

        text = "There are no more";
        layout = new GlyphLayout();
        layout.setText(Font.endScreenBottomFont,text);
        textWdt = layout.width;
        //textHgh = Font.endScreenBottomFont.getCapHeight();
        Font.endScreenBottomFont.draw(Renderer.spriteBatch, text, (x - textWdt/2.0f), (y - dy));

        text = "moves available";
        layout = new GlyphLayout();
        layout.setText(Font.endScreenBottomFont,text);
        textWdt = layout.width;
        //textHgh = Font.endScreenBottomFont.getCapHeight();
        Font.endScreenBottomFont.draw(Renderer.spriteBatch, text, (x - textWdt/2.0f), (y - dy-48));

        _restartBtn.draw();
    }
    private void drawGameCompleteScreen() {

        float x = (Renderer.VIRTUAL_WIDTH)/2.0f;
        float y = (Renderer.VIRTUAL_HEIGHT)/2.0f + 128.0f;

        Texture smallPanelTexture = AssetsManager.getTexture("tex/smallPanel.png");
        if(smallPanelTexture != null){
            Sprite sprite = new Sprite(smallPanelTexture);
            sprite.setSize(512, 512);
            sprite.setCenter(x, y);
            sprite.draw(Renderer.spriteBatch);
        }

        // dy for movement of everything on the panel
        float dy = 16;
        GlyphLayout layout;
        float textWdt;
        float textHgh;
        String text = "you win";

        layout = new GlyphLayout();
        layout.setText(Font.endScreenTopFont,text);
        textWdt = layout.width;
        textHgh = Font.endScreenTopFont.getCapHeight();
        Font.endScreenTopFont.draw(Renderer.spriteBatch, text, (x - textWdt/2.0f), (y + textHgh + 160 - dy));

        // player name text input
        if(_ranking < 15){
            _playerNameInput.draw();
        }


        // time
        layout = new GlyphLayout();
        layout.setText(Font.endScreenBottomFont, "time: ");
        //textWdt = layout.width;
        textHgh = Font.endScreenBottomFont.getCapHeight();
        Font.endScreenBottomFont.draw(Renderer.spriteBatch, "time:", x - 224 + 16, y - 16 + textHgh - dy);

        layout = new GlyphLayout();
        layout.setText(Font.endScreenBottomFont, Time.generateTimeString(_gameTime));
        textWdt = layout.width;
        textHgh = Font.endScreenBottomFont.getCapHeight();
        Font.endScreenBottomFont.draw(Renderer.spriteBatch, Time.generateTimeString(_gameTime), x - textWdt + 192, y - 16 + textHgh - dy);

        // moves
        layout = new GlyphLayout();
        layout.setText(Font.endScreenBottomFont, "moves: ");
        //textWdt = layout.width;
        textHgh = Font.endScreenBottomFont.getCapHeight();
        Font.endScreenBottomFont.draw(Renderer.spriteBatch, "moves:", x - 224 + 16, y - 16 - 64 + textHgh - dy);

        layout = new GlyphLayout();
        layout.setText(Font.endScreenBottomFont, Integer.toString(_moves));
        textWdt = layout.width;
        textHgh = Font.endScreenBottomFont.getCapHeight();
        Font.endScreenBottomFont.draw(Renderer.spriteBatch, Integer.toString(_moves), x - textWdt + 192, y - 16 - 64 + textHgh - dy);

        _restartBtn.draw();
    }

    @Override
    public void handleEvents() {

        _backBtn.handleEvents();

        if(_state == GameStates.Game){
            if (MyInput.processor.isBackPressed()) {
                LayoutsManager.pop_back();
                MyInput.processor.reset();
            }
            CardsInHand.handleEvents();

            for(Tableau tableau : Tableaus.tableaus)
                tableau.handleEvents();

            for(Foundation foundation : Foundations.foundations)
                foundation.handleEvents();

            Stock.handleEvents();
            Waste.handleEvents();

            if(showedBtn == showedBtnType.AutoComplete){
                _autoCompleteBtn.handleEvents();
            }

            if(showedBtn == showedBtnType.NoMove)
                _noMovesBtn.handleEvents();

        }

        if(_state == GameStates.GameFailed){
            _restartBtn.handleEvents();
        }

        if(_state == GameStates.GameComplete){
            _restartBtn.handleEvents();
            _playerNameInput.handleEvents();
        }

    }

    @Override
    public void update() {

        _backBtn.update();

        switch (_state){
            case Start:
                startGame();
                _state = GameStates.Game;
                break;

            case Game:
                _gameTime = Time.currentTime - _startTime;

                for(Tableau tableau : Tableaus.tableaus)
                    tableau.update();

                for(Foundation foundation : Foundations.foundations)
                    foundation.update();

                Stock.update();
                Waste.update();
                CardsInHand.update();

                if(!isNextMove()){
                    showedBtn = showedBtnType.NoMove;
                }
                else if(readyToAutoComplete()){
                    showedBtn = showedBtnType.AutoComplete;
                }

                _noMovesBtn.update();
                _autoCompleteBtn.update();

                if(Animations.animations.isEmpty() && CardsInHand._cards.isEmpty()){
                    if(endGame()){
                        _state = GameStates.UpdateHighscores;
                    }
                }
                break;

            case AutoComplete:

                for(Tableau tableau : Tableaus.tableaus)
                    tableau.update();

                for(Foundation foundation : Foundations.foundations)
                    foundation.update();

                Stock.update();
                Waste.update();
                CardsInHand.update();

                if(Animations.animations.isEmpty()){
                    autoComplete();
                }
                if(Animations.animations.isEmpty() && endGame())
                    _state = GameStates.UpdateHighscores;

                _backBtn.update();
                break;

            case UpdateHighscores:
                loadHighscores();
                findRankingPosition();
                //generateEndScreenComment();
                addToHighscores();
                saveHighscores();
                System.out.println("_ranking = " + _ranking);
                _state = GameStates.GameComplete;
                break;

            case GameFailed:
                _restartBtn.update();
                break;

            case GameComplete:
                _restartBtn.update();
                _playerNameInput.update();
                break;

            case End:
                LayoutsManager.pop_back();
                break;

        }

        Animations.update();
    }

    @Override
    public void draw() {

        Texture backgroundTexture = AssetsManager.getTexture("tex/mainBoard.png");
        if(backgroundTexture!=null){
            Sprite background = new Sprite(backgroundTexture);
            background.setPosition(0,0 );
            background.draw(Renderer.spriteBatch);
        }

        Texture topPanelTexture = AssetsManager.getTexture("tex/topPanel.png");
        if(topPanelTexture != null){
            Sprite topPanel = new Sprite(topPanelTexture);
            topPanel.setPosition(0,Renderer.VIRTUAL_HEIGHT - topPanel.getHeight() );
            topPanel.draw(Renderer.spriteBatch);
        }

        _backBtn.draw();

        drawTimerAndMoves();

        switch (_state){
            case Game:
                for(Tableau tableau : Tableaus.tableaus)
                    tableau.draw();
                for(Foundation foundation : Foundations.foundations)
                    foundation.draw();
                Stock.draw();
                Waste.draw();
                Animations.draw();
                CardsInHand.draw();

                if(showedBtn == showedBtnType.NoMove){
                    _noMovesBtn.draw();
                }
                else if(showedBtn == showedBtnType.AutoComplete)
                    _autoCompleteBtn.draw();



                break;
            case AutoComplete:
                for(Tableau tableau : Tableaus.tableaus)
                    tableau.draw();
                for(Foundation foundation : Foundations.foundations)
                    foundation.draw();
                Stock.draw();
                Waste.draw();
                Animations.draw();

                break;

            case UpdateHighscores:
                break;

            case GameFailed:
                for(Tableau tableau : Tableaus.tableaus)
                    tableau.draw();
                for(Foundation foundation : Foundations.foundations)
                    foundation.draw();
                Stock.draw();
                Waste.draw();
                drawGameFailedScreen();
                break;

            case GameComplete:
                for(Tableau tableau : Tableaus.tableaus)
                    tableau.draw();
                for(Foundation foundation : Foundations.foundations)
                    foundation.draw();
                Stock.draw();
                Waste.draw();
                drawGameCompleteScreen();
                break;
        }

        // ads panel
        Texture bottomPanelTexture = AssetsManager.getTexture("tex/bottomPanel.png");
        if(bottomPanelTexture!=null){
            Sprite adsPanel = new Sprite(bottomPanelTexture);
            adsPanel.setPosition(0, 0);
            adsPanel.draw(Renderer.spriteBatch);
        }


    }
}
