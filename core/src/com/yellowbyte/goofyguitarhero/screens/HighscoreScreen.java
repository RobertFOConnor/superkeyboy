package com.yellowbyte.goofyguitarhero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.yellowbyte.goofyguitarhero.LeaderboardManager;
import com.yellowbyte.goofyguitarhero.MainGame;
import com.yellowbyte.goofyguitarhero.media.Assets;
import com.yellowbyte.goofyguitarhero.media.Fonts;

public class HighscoreScreen extends Screen {

    private Texture bg;
    private BitmapFont UIFont;
    private int score, approval;
    private NameInput nameInput;

    public HighscoreScreen(int score, int approval) {
        this.score = score;
        this.approval = approval;
    }

    @Override
    public void create() {
        super.create();
        //bg = new Texture("bg.jpg");
        UIFont = Fonts.getFont(Fonts.FUTURA);
        nameInput = new NameInput();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            ScreenManager.setScreen(new GameScreen());
        }
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        super.render(sb, sr);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        UIFont.draw(sb, "You got a highscore!!!" + score, MainGame.WIDTH / 3, MainGame.HEIGHT - 300);
        nameInput.render(sb);
        sb.end();
    }

    private class NameInput {

        private Skin skin;
        private Stage stage;
        private boolean showing = true;

        public NameInput() {
            skin = Assets.manager.get(Assets.SKIN, Skin.class);

            stage = new Stage();

            final TextField mapName = new TextField("", skin);
            mapName.setWidth(500f);
            mapName.setHeight(100f);
            mapName.setMaxLength(30);
            mapName.setMaxLength(8);
            mapName.setColor(Color.GRAY);
            mapName.setMessageText("Enter your name");
            mapName.setPosition(Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
            stage.addActor(mapName);

            final TextButton saveButton = new TextButton("Submit", skin, "default");
            saveButton.setWidth(500f);
            saveButton.setHeight(100f);
            saveButton.setPosition(Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2 - 120);

            saveButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    LeaderboardManager leaderboardManager = new LeaderboardManager();
                    leaderboardManager.postScore(mapName.getText(), score);
                    ScreenManager.setScreen(new GameOverScreen(score, approval));
                    Gdx.input.setInputProcessor(null);
                }
            });
            stage.addActor(saveButton);
            Gdx.input.setInputProcessor(stage);
        }

        public void render(SpriteBatch sb) {
            if (showing) {
                stage.draw();
            }
        }

        public boolean isShowing() {
            return showing;
        }

        public void setShowing(boolean showing) {
            this.showing = showing;

            if (showing) {
                Gdx.input.setInputProcessor(stage);
            } else {
                Gdx.input.setInputProcessor(null);
            }
        }
    }


    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void goBack() {

    }
}
