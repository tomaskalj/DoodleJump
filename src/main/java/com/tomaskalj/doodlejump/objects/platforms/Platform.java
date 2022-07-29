package com.tomaskalj.doodlejump.objects.platforms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tomaskalj.doodlejump.common.Constants;
import com.tomaskalj.doodlejump.objects.DoodleBoy;
import lombok.Getter;

@Getter
public abstract class Platform {
    protected Texture texture;
    protected Rectangle rectangle;

    public Platform(float x, float y, String textureName) {
        rectangle = new Rectangle(x, y, Constants.PLATFORM_WIDTH, Constants.PLATFORM_HEIGHT);
        texture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, textureName)));
    }

    public abstract void onRender(float delta, SpriteBatch batch);

    public void onCollide(DoodleBoy doodleBoy) {
        doodleBoy.onPlatformCollide();
    }

    public void dispose() {
        texture.dispose();
    }

    public float getX() {
        return rectangle.getX();
    }

    public float getY() {
        return rectangle.getY();
    }

    public boolean withinRange(float x, float y) {
        return (x >= getX() - 70 && x <= getX() + 70) && (y >= getY() - 20 && y <= getY() + 20);
    }
}
