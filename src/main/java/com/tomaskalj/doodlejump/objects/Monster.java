package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.tomaskalj.doodlejump.common.Constants;
import lombok.Getter;
import lombok.Setter;

public class Monster {
    @Getter
    private final Texture texture;
    @Getter
    private final Circle circle;
    @Setter
    private boolean hit;
    private int xOffset;
    private boolean add;

    public Monster(float x, float y) {
        texture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, Constants.MONSTER_NAMES[(int) (Math.random() * Constants.MONSTER_NAMES.length)])));
        circle = new Circle(x, y, Constants.MONSTER_RADIUS);
    }

    public float getX() {
        return circle.x;
    }

    public float getY() {
        return circle.y;
    }

    public void onRender(float delta) {
        if (add) {
            if (xOffset > 3) {
                add = false;
            }
            xOffset++;
        } else {
            if (xOffset < -3) {
                add = true;
            }
            xOffset--;
        }

        circle.x += xOffset;

        if (hit) {
            circle.y += Constants.GRAVITY * delta;
        }
    }
}
