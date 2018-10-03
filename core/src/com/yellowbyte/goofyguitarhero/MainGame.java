package com.yellowbyte.goofyguitarhero;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbyte.goofyguitarhero.media.Assets;
import com.yellowbyte.goofyguitarhero.media.Fonts;
import com.yellowbyte.goofyguitarhero.screens.GameScreen;
import com.yellowbyte.goofyguitarhero.screens.HighscoreScreen;
import com.yellowbyte.goofyguitarhero.screens.ScreenManager;

public class MainGame extends ApplicationAdapter {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    private boolean loaded = false;

    private SpriteBatch sb;
    private ShapeRenderer sr;

    @Override
    public void create() {
        Assets.load();
        Fonts.load();
        sr = new ShapeRenderer();
        sb = new SpriteBatch();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Assets.update() && !loaded) {
            loaded = true;
            ScreenManager.setScreen(new GameScreen());
        }

        if (loaded) {
            ScreenManager.getCurrentScreen().update(Gdx.graphics.getDeltaTime());
            ScreenManager.getCurrentScreen().render(sb, sr);
            //developerTools.renderOverlay(sb);
        }
    }

    @Override
    public void resize(int w, int h) {
        if (ScreenManager.getCurrentScreen() != null) {
            ScreenManager.getCurrentScreen().resize(w, h);
        }
    }

    @Override
    public void dispose() {
        sb.dispose();
        sr.dispose();
    }
}
