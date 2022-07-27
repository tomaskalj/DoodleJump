package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.tomaskalj.doodlejump.common.Constants;
import lombok.Getter;

@Getter
public class Projectile {
    private final Circle circle;
    private final Texture texture;

    public Projectile(float x, float y) {
        circle = new Circle(x, y, Constants.PROJECTILE_RADIUS);
        texture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "projectile.png")));
    }

    public void onRender(float delta) {
        circle.y += Constants.PROJECTILE_VELOCITY * delta;
    }
}
