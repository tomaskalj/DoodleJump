package com.tomaskalj.doodlejump.objects.platforms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StandardPlatform extends Platform {
    public StandardPlatform(float x, float y) {
        super(x, y, "platform.png");
    }

    @Override
    public void onRender(float delta, SpriteBatch batch) {

    }
}
