package com.yellowbyte.goofyguitarhero.media;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Fonts {

    private static BitmapFont futuraMedium; //size: 56
    private static BitmapFont monoMedium; //size: 56
    public static final String FUTURA = "ui/futura";
    public static final String NOTE = "note";
    public static final String MONO = "ui/mono";

    public static void load() {
        futuraMedium = new BitmapFont(Gdx.files.internal(FUTURA.concat(".fnt")), Gdx.files.internal(FUTURA.concat(".png")), false);
        futuraMedium.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        monoMedium = new BitmapFont(Gdx.files.internal(MONO.concat(".fnt")), Gdx.files.internal(MONO.concat(".png")), false);
        monoMedium.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public static BitmapFont getFont(String fontName) {
        if (fontName.equals(FUTURA)) {
            return futuraMedium;
        } else if (fontName.equals(NOTE)) {
            futuraMedium.setColor(Color.BLACK);
            return futuraMedium;
        } else if (fontName.equals(MONO)) {
            monoMedium.setColor(Color.WHITE);
            return monoMedium;
        }
        return null;
    }

    public static float getWidth(BitmapFont font, String s) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, s);
        return layout.width;
    }
}
