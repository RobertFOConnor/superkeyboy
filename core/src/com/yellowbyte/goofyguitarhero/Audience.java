package com.yellowbyte.goofyguitarhero;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbyte.goofyguitarhero.media.Assets;
import com.yellowbyte.goofyguitarhero.media.Sounds;

public class Audience {

    private Texture crowd;
    private int approval = 50;
    private boolean playedFinalReaction = false;

    private Texture reactions[];
    private Texture currReaction;

    public Audience() {
        crowd = new Texture("sprites/crowd.png");
        reactions = new Texture[4];
        for (int i = 0; i < 4; i++) {
            reactions[i] = getReaction(i);
        }
        currReaction = reactions[1];
    }

    private Texture getReaction(int index) {
        Texture texture = new Texture("sprites/audience/reaction_" + (index + 1) + ".png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return texture;
    }

    public int getApproval() {
        return approval;
    }

    public void setApproval(int approval) {
        if (approval < 0) {
            approval = 0;
        } else if (approval > 100) {
            approval = 100;
        }
        this.approval = approval;
        updateReactionImage();
    }

    private void updateReactionImage() {
        if (approval > 80) {
            currReaction = reactions[0];
        } else if (approval > 45) {
            currReaction = reactions[1];
        } else if (approval > 20) {
            currReaction = reactions[2];
        } else {
            currReaction = reactions[3];
        }
    }

    public void render(SpriteBatch sb) {
        sb.draw(crowd, -30, 0, MainGame.WIDTH + 60, crowd.getHeight());
    }

    public void getSongOverReaction() {
        if (!playedFinalReaction) {
            LeaderboardManager leaderboardManager = new LeaderboardManager();
            leaderboardManager.getScores();
            if (getApproval() > 30) {
                Sounds.play(Assets.CHEER_SOUND);
            } else {
                Sounds.play(Assets.BOO_SOUND);
            }
        }
        playedFinalReaction = true;
    }

    public void renderReaction(SpriteBatch sb) {
        sb.draw(currReaction, MainGame.WIDTH - currReaction.getWidth() - 40, MainGame.HEIGHT - currReaction.getHeight() - 40);
    }
}
