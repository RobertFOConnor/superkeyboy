package com.yellowbyte.goofyguitarhero.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ParticleManager {

    //Particle Objects
    private ParticleEffectPool sparklePool;
    private Array<PooledEffect> effects;


    public ParticleManager() {
        effects = new Array<PooledEffect>();
        ParticleEffect particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particles/note.p"), Gdx.files.internal("particles"));
        sparklePool = new ParticleEffectPool(particleEffect, 1, 2);
        addEffect(0, 0); //First one doesn't play (bug)
    }

    public void addEffect(float x, float y) {
        PooledEffect effect = sparklePool.obtain();
        effect.setPosition(x, y);
        //effect.getEmitters().get(0).getTint().setColors(color);
        effects.add(effect);
    }

    public void update(float delta) {
        for (PooledEffect effect : effects) {
            effect.update(delta);
        }
    }

    public void render(SpriteBatch sb) {
        for (int i = 0; i < effects.size; i++) {
            PooledEffect effect = effects.get(i);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(i);
                effect.dispose();
            } else {
                effect.draw(sb);
            }
        }
    }
}