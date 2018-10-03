package com.yellowbyte.goofyguitarhero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbyte.goofyguitarhero.MainGame;
import com.yellowbyte.goofyguitarhero.media.Fonts;

public class GameOverScreen extends Screen {

    private Texture bg;
    private BitmapFont UIFont;
    private int score, approval;

    public GameOverScreen(int score, int approval) {
        this.score = score;
        this.approval = approval;
    }

    @Override
    public void create() {
        super.create();
        //bg = new Texture("bg.jpg");
        UIFont = Fonts.getFont(Fonts.FUTURA);
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
        UIFont.draw(sb, "Final Score: " + score, MainGame.WIDTH / 3, MainGame.HEIGHT / 2);
        UIFont.draw(sb, "Audience Approval: " + approval + "%", MainGame.WIDTH / 3, MainGame.HEIGHT / 2 - 100);
        UIFont.draw(sb, "Press Enter to play again!", MainGame.WIDTH / 3, MainGame.HEIGHT / 2 - 200);
        //sb.draw(bg, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        sb.end();
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
