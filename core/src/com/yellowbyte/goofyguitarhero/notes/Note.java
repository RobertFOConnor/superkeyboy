package com.yellowbyte.goofyguitarhero.notes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbyte.goofyguitarhero.media.Fonts;

public class Note extends Sprite {

    private BitmapFont font = Fonts.getFont(Fonts.NOTE);
    public static final float width = 60;
    public static final float height = 30f;
    private int character;
    private int[] bgColor;
    private float textWidth;
    private String charString;

    public Note(float x, float y, int character) {
        super(new Texture("circle.png"));
        setX(x);
        setY(y);
        this.character = character;
        bgColor = new int[]{randomColor(), randomColor(), randomColor()};
        charString = Input.Keys.toString(character);
        textWidth = Fonts.getWidth(font, charString);
    }

    private int randomColor() {
        return (int) (Math.random() * 255);
    }

    public void update(float delta, float speed) {
        translateY(-speed * delta);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(bgColor[0], bgColor[1], bgColor[2], 1);
        sb.draw(getTexture(), getX(), getY(), width, height);
        font.setColor(Color.WHITE);
        font.draw(sb, charString, getX() + (width / 2) - (textWidth / 2), getY() + height + 60);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public int getCharacter() {
        return character;
    }
}
