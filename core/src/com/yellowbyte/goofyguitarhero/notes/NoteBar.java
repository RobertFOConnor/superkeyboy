package com.yellowbyte.goofyguitarhero.notes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.yellowbyte.goofyguitarhero.KeyNames;
import com.yellowbyte.goofyguitarhero.MainGame;
import com.yellowbyte.goofyguitarhero.media.Assets;
import com.yellowbyte.goofyguitarhero.media.Sounds;

import java.util.ArrayList;

public class NoteBar {

    private String message;
    private String messag1 = "mycatsmellslikepoopallthedamntimeanditssick";
    private String messag2 = "thelazyoftenfarmerfeedshissheeponcedailyhah";
    private String messag3 = "jumpingonthebedisfununtilsomebodylosesaneye";
    private String messag4 = "pineappleonapizzaisnotnaturalsojustdontdoit";
    private String messag5 = "ifihadapennyforeverytimeihearddrakesayhello";
    private String[] messageArray = {messag1, messag2, messag3, messag4, messag5};

    private int index = 0;

    private Sprite noteBar;
    private ArrayList<Note> notes;
    private Timer timer;
    private float firstStringX;
    private float speed = 200;
    private float runTime = 1f;
    private float maxRunTime = 62f; //seconds
    private float BPM = 100;
    private float BPS = (60000f / BPM) * 0.001f;
    private boolean songOver = false;

    public NoteBar() {
        Texture noteBarTexture = new Texture("sprites/notebar.png");
        noteBarTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        noteBar = new Sprite(noteBarTexture);
        noteBar.setPosition(MainGame.WIDTH / 2 - noteBar.getWidth() / 2, MainGame.HEIGHT / 2 - noteBar.getHeight() / 2);
        firstStringX = MainGame.WIDTH / 2 - Note.width * 2;
        notes = new ArrayList<Note>();
        message = messageArray[(int) (Math.random() * messageArray.length)];
    }

    public void start() {
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                addNote();
                runTime += BPS * 4;
                if (runTime > 19) {
                    timer.stop();
                    timer = new Timer();
                    timer.scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            addNote();
                            runTime += BPS * 2;
                            if (runTime > maxRunTime) {
                                timer.stop();
                                songOver = true;
                            }
                        }
                    }, 1f, BPS * 2);
                    timer.start();
                }
            }
        }, 1f, BPS * 4);
        timer.start();
        Sounds.setMusic(Assets.GUITAR_MUSIC);
    }

    private int getCompletion() {
        return (int) ((runTime / maxRunTime) * 100);
    }

    private void addNote() {
        int keyCode = Input.Keys.valueOf(message.toUpperCase().charAt(index) + "");
        index++;
        if (index >= message.length()) {
            index = 0;
        }

        notes.add(new Note(
                getNoteStartX((int) (Math.random() * 5)),
                MainGame.HEIGHT,
                keyCode
        ));

        System.out.println("Note count: " + message.length());
    }

    private int getRandomKeyCode() {
        return KeyNames.keys[(int) (Math.random() * KeyNames.keys.length)];
    }

    private float getNoteStartX(int stringNumber) {
        return (firstStringX - Note.width / 2) + (Note.width * stringNumber);
    }

    private void updateNotes(float delta) {
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            note.update(delta, speed);
        }
    }

    public void update(float delta) {
        updateNotes(delta);
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.end();
        drawGuitarStrings(sr);
        sb.begin();
        drawNotes(sb);
        noteBar.draw(sb);
    }

    private void drawNotes(SpriteBatch sb) {
        for (int i = 0; i < notes.size(); i++) {
            notes.get(i).render(sb);
        }
    }

    private void drawGuitarStrings(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.GOLD);

        for (int i = 0; i < 5; i++) {
            float x = firstStringX + (i * Note.width);
            sr.rectLine(x, MainGame.HEIGHT, x, MainGame.HEIGHT / 2, 3);
        }
        sr.end();
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public boolean isSongOver() {
        return songOver;
    }
}
