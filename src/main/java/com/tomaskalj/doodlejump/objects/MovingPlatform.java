package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tomaskalj.doodlejump.common.Constants;
import com.tomaskalj.doodlejump.common.Direction;

public class MovingPlatform extends Platform {
    private Direction direction;

    public MovingPlatform(float x, float y) {
        super(x, y, "moving_platform.png");
        direction = Direction.RIGHT;
    }

    @Override
    public void onRender(float delta, SpriteBatch batch) {
        if (direction == Direction.RIGHT) {
            rectangle.x++;
            if ((int) getX() >= Constants.WORLD_WIDTH - texture.getWidth()) {
                direction = Direction.LEFT;
            }
        } else if (direction == Direction.LEFT) {
            rectangle.x--;
            if ((int) getX() == 0) {
                direction = Direction.RIGHT;
            }
        }
    }
}
