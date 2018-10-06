package com.yellowbyte.goofyguitarhero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.yellowbyte.goofyguitarhero.Audience;
import com.yellowbyte.goofyguitarhero.KeyNames;
import com.yellowbyte.goofyguitarhero.LeaderboardManager;
import com.yellowbyte.goofyguitarhero.MainGame;
import com.yellowbyte.goofyguitarhero.effects.ParticleManager;
import com.yellowbyte.goofyguitarhero.media.Assets;
import com.yellowbyte.goofyguitarhero.media.Fonts;
import com.yellowbyte.goofyguitarhero.media.Sounds;
import com.yellowbyte.goofyguitarhero.notes.Note;
import com.yellowbyte.goofyguitarhero.notes.NoteBar;

import java.util.ArrayList;

public class GameScreen extends Screen {

    private BitmapFont UIFont = Fonts.getFont(Fonts.FUTURA);
    private BitmapFont scoresFont = Fonts.getFont(Fonts.MONO);
    private ParticleManager particles;

    private int score = 0;
    private int hitStreak = 0;
    private int missStreak = 0;

    private String title = "SUPER KEY BOY!";
    private float titleWidth = Fonts.getWidth(UIFont, title);

    private Texture bg;
    private Texture guitarist[];
    private Texture guitaristImage;


    private float barY = 100;

    private boolean cameraShaking = false;
    private float shakeLimit = 10;
    private float shakeAmount = 0;

    private String centerText = null;

    private Audience audience;
    private NoteBar noteBar;

    private boolean gameStated = false;
    private boolean gameOver = false;

    private boolean posing = false;
    private int poseTime = 50;
    private int poseAmount = 0;

    @Override
    public void create() {
        super.create();
        bg = new Texture("sprites/stage.jpg");

        guitarist = new Texture[3];
        guitarist[0] = getTexture("sprites/guitarman/guy_idle.png");
        guitarist[1] = getTexture("sprites/guitarman/guy_miss.png");
        guitarist[2] = getTexture("sprites/guitarman/guy_win.png");
        guitaristImage = guitarist[0];

        bg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        noteBar = new NoteBar();
        particles = new ParticleManager();
        audience = new Audience();

        final LeaderboardManager lm = new LeaderboardManager();


        final Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (LeaderboardManager.scoreItems.size() > 0) {
                    timer.stop();
                    timer.clear();
                } else {
                    lm.getScores();
                    System.out.println("Trying to fetch scores");
                }
            }
        }, 0, 3f);
        timer.start();
    }

    private Texture getTexture(String path) {
        Texture t = new Texture(path);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return t;
    }

    private void doPose(int index) {
        guitaristImage = guitarist[index];
        posing = true;
        poseAmount = 0;
    }


    private void handleMiss() {
        cameraShaking = true;
        shakeAmount = 0;
        missStreak++;
        hitStreak = 0;
        if (audience.getApproval() > 15) {
            audience.setApproval(audience.getApproval() - missStreak * 2);
        } else {
            audience.setApproval(audience.getApproval() - 1);
        }
        Sounds.play(Assets.MISS_SOUND);
        doPose(1);
    }

    private void updateCamera() {
        if (cameraShaking) {
            camera.position.set(MainGame.WIDTH / 2 - 8 + (int) (Math.random() * 16), camera.position.y, 0);
            camera.update();
            shakeAmount++;
            if (shakeAmount >= shakeLimit) {
                cameraShaking = false;
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        handleKeyPresses();
        updateCamera();

        if (gameStated) {
            noteBar.update(delta);

            for (int i = 0; i < noteBar.getNotes().size(); i++) {
                if (noteBar.getNotes().get(i).getY() < MainGame.HEIGHT / 2 - Note.height * 2f) {
                    noteBar.getNotes().remove(i);
                    System.out.println("Missed!");
                    handleMiss();
                }
            }

            checkGameOver();
        }
        particles.update(delta);

        if (posing) {
            poseAmount++;
            if (poseAmount > poseTime) {
                posing = false;
                guitaristImage = guitarist[0];
            }
        }
        //recordKeys();
    }

    private void checkGameOver() {
        if (!Sounds.isMusicPlaying()) {
            if (noteBar.getNotes().size() == 0) {
                LeaderboardManager leaderboardManager = new LeaderboardManager();
                if (leaderboardManager.showHighScoreInput(score)) {
                    ScreenManager.setScreen(new HighscoreScreen(score, audience.getApproval()));
                } else {
                    ScreenManager.setScreen(new GameOverScreen(score, audience.getApproval()));
                }
            }
        }

        if (noteBar.getNotes().size() == 0 && noteBar.isSongOver()) {
            audience.getSongOverReaction();

        }
    }

    private void handleKeyPresses() {

        if (!gameStated) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                gameStated = true;
                noteBar.start();
            }
        }

        if (noteBar.getNotes().size() > 0) {
            for (int keycode : KeyNames.keys) {
                if (Gdx.input.isKeyJustPressed(keycode)) {
                    Note note = noteBar.getNotes().get(0); // Get next note to be played.
                    if (keycode == note.getCharacter()) {
                        if (note.getY() > MainGame.HEIGHT / 2 - note.getHeight() && note.getY() < MainGame.HEIGHT / 2) {
                            setScore((int) (score + (100 * (hitStreak * 1.5f))));
                            if (audience.getApproval() < 80) {
                                audience.setApproval(audience.getApproval() + (hitStreak * 2));
                            } else {
                                audience.setApproval(audience.getApproval() + 1);
                            }
                            hitStreak++;
                            doPose(2);
                            missStreak = 0;
                            //Sounds.play(Assets.HIT_SOUND);
                            particles.addEffect(note.getX() + note.getWidth() / 2, note.getY() + note.getHeight() / 2);
                            noteBar.getNotes().remove(0); //On tune!!
                            Sounds.playAtVolume(Assets.HIT_SOUND, 0.08f);
                        } else {
                            if (note.getY() > barY - note.getHeight()) {
                                System.out.println("Too early!");
                                handleMiss();
                            } else {
                                System.out.println("Too late!");
                                handleMiss();
                            }
                        }
                    }
                }
            }
        }
    }

    private void setScore(int score) {
        this.score = score;
        if (this.score < 0) {
            this.score = 0;
        }
    }

    public void renderUI(SpriteBatch sb, ShapeRenderer sr) {
        sb.end();
        sr.setProjectionMatrix(camera.combined);

        //render UI shapes here

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.end();
        sb.begin();
        sb.setColor(Color.WHITE);
        audience.renderReaction(sb);
        UIFont.setColor(cameraShaking ? Color.RED : Color.WHITE);
        UIFont.draw(sb, "Score: " + score, 50, MainGame.HEIGHT - 50);
        //UIFont.draw(sb, "Streak: " + hitStreak, 50, MainGame.HEIGHT - 140);

        if (centerText != null) {
            UIFont.draw(sb, centerText, MainGame.WIDTH / 2, MainGame.HEIGHT / 2);
        }
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        super.render(sb, sr); // 2. render the GUI
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.setColor(cameraShaking ? Color.RED : Color.WHITE);
        if (!gameStated) {
            sb.setColor(Color.LIGHT_GRAY);
        }
        sb.draw(bg, -20, 20, MainGame.WIDTH + 40, MainGame.HEIGHT + 40);
        sb.draw(guitaristImage, MainGame.WIDTH / 2 - guitaristImage.getWidth() / 2 + 80, 150);
        audience.render(sb);
        //rigidMan.render(sb);

        if (gameStated) {
            noteBar.render(sb, sr);
            renderUI(sb, sr);
        } else {
            UIFont.draw(sb, title, MainGame.WIDTH / 2 - titleWidth / 2, MainGame.HEIGHT - 300);
            UIFont.draw(sb, "A game by Donall & Robert", MainGame.WIDTH / 2 + 200, 70);
            UIFont.setColor(Color.PINK);
            UIFont.draw(sb, "(press enter)", MainGame.WIDTH / 2 - 160, MainGame.HEIGHT - 370);
            UIFont.setColor(Color.WHITE);

            scoresFont.draw(sb, "Leaderboard", 30, MainGame.HEIGHT - 50);

            //TODO fix score display.
            if (LeaderboardManager.scoreItems.size() > 0) {
                ArrayList<LeaderboardManager.ScoreItem> scores = LeaderboardManager.scoreItems;
                for (int i = 0; i < scores.size(); i++) {
                    scoresFont.draw(sb, scores.get(i).name + ": " + scores.get(i).score, 30, MainGame.HEIGHT - 100 - (i * 40))
                    ;
                }
            }
        }
        particles.render(sb);
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
