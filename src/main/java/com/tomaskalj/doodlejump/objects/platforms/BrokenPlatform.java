package com.tomaskalj.doodlejump.objects.platforms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tomaskalj.doodlejump.common.Constants;
import com.tomaskalj.doodlejump.objects.DoodleBoy;

public class BrokenPlatform extends Platform {
    private final Texture fallingTexture;

    public BrokenPlatform(float x, float y) {
        super(x, y, "broken_platform.png");
        fallingTexture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "broken_platform_falling.png")));
    }

    @Override
    public void onRender(float delta, SpriteBatch batch) {
        if (texture == fallingTexture) {
            rectangle.y -= Constants.FALLING_PLATFORM_VELOCITY * delta;
        }
    }

    @Override
    public void onCollide(DoodleBoy doodleBoy) {
        if (!doodleBoy.isFalling()) {
            return;
        }

        texture = fallingTexture;
    }
}
