package com.yellowbyte.goofyguitarhero.media;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Assets {

    public static AssetManager manager = new AssetManager();
    private static HashMap<String, Class> assetMap = new HashMap<String, Class>();

    private static String ATLAS = "pack.atlas";
    public static final String SKIN = "ui/uiskin.json";
    public static String GUITAR_MUSIC = "music/music.mp3";
    public static String MISS_SOUND = "sounds/miss.wav";
    public static String HIT_SOUND = "sounds/hit.wav";
    public static String BOO_SOUND = "sounds/booing.wav";
    public static String CHEER_SOUND = "sounds/cheering.wav";

    // Loads Assets
    public static void load() {

        //assetMap.put(ATLAS, TextureAtlas.class);

        //Music
        assetMap.put(SKIN, Skin.class);
        assetMap.put(GUITAR_MUSIC, Music.class);
        assetMap.put(MISS_SOUND, Sound.class);
        assetMap.put(HIT_SOUND, Sound.class);
        assetMap.put(CHEER_SOUND, Sound.class);
        assetMap.put(BOO_SOUND, Sound.class);

        Iterator it = assetMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            manager.load((String) pair.getKey(), (Class) pair.getValue());
            it.remove();
        }
    }

    public static Texture getTexture(String name) {
        return manager.get(name, Texture.class);
    }

    public static Sprite getSprite(String name) {
        return manager.get(ATLAS, TextureAtlas.class).createSprite(name);
    }

    public static void dispose() {
        manager.dispose();
    }

    public static boolean update() {
        return manager.update();
    }
}
