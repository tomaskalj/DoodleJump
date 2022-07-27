package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.tomaskalj.doodlejump.common.Constants;
import com.tomaskalj.doodlejump.common.Direction;
import lombok.Getter;
import lombok.Setter;

public class DoodleBoy {
    @Getter
    private final Rectangle rectangle;
    private State state;

    @Getter
    @Setter
    private Direction direction;

    @Getter
    private final Texture rightFacingSprite;
    @Getter
    private final Texture leftFacingSprite;
    @Getter
    private final Texture shootingSprite;
    @Getter
    private final Texture jetpackRightSprite;
    @Getter
    private final Texture jetpackLeftSprite;

    private long shootTime;
    private long jetpackTime;

    // Physics
    private final Vector2 position;
    private final Vector2 velocity;

    public DoodleBoy(float x, float y) {
        rectangle = new Rectangle(x, y, Constants.DOODLE_BOY_WIDTH, Constants.DOODLE_BOY_HEIGHT);
        state = State.FALLING;
        direction = Direction.RIGHT;
        rightFacingSprite = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "doodle_boy_right.png")));
        leftFacingSprite = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "doodle_boy_left.png")));
        shootingSprite = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "shooty_boy.png")));
        jetpackRightSprite = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "doodle_jetpack_right.png")));
        jetpackLeftSprite = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "doodle_jetpack_left.png")));

        position = new Vector2(x, y);
        velocity = new Vector2();
    }

    public void update(float delta) {
        velocity.add(0, Constants.GRAVITY * delta);
        position.add(0, velocity.y * delta);

        rectangle.y = position.y - rectangle.height / 2;

        if (velocity.y > 0 && state != State.HIT) {
            if (state != State.JUMPING) {
                state = State.JUMPING;
            }
        }

        if (velocity.y < 0 && state != State.HIT) {
            if (state != State.FALLING) {
                state = State.FALLING;
            }
        }

        if (rectangle.x < 0) {
            rectangle.x = Constants.WORLD_WIDTH;
        }

        if (rectangle.x > Constants.WORLD_WIDTH) {
            rectangle.x = 0;
        }
    }

    public void onPlatformCollide() {
        velocity.y = Constants.DOODLE_BOY_JUMP_VELOCITY;
        state = State.JUMPING;
    }

    public void onSpringCollide() {
        velocity.y = Constants.DOODLE_BOY_SPRING_VELOCITY;
        state = State.JUMPING;
    }

    public void onJetpackCollide() {
        velocity.y = Constants.DOODLE_BOY_JETPACK_VELOCITY;
        state = State.JUMPING;
    }

    public void onMonsterCollide(Monster monster) {
        if (state == State.JUMPING) {
            velocity.y = Constants.DOODLE_BOY_HIT_VELOCITY;
            state = State.HIT;
        } else if (state == State.FALLING) {
            monster.setHit(true);
            velocity.y = Constants.DOODLE_BOY_MONSTER_HIT_VELOCITY;
            state = State.JUMPING;
        }
    }

    public Rectangle getFeet() {
        return new Rectangle(rectangle.x + (direction == Direction.LEFT ? Constants.DOODLE_BOY_LEFT_OFFSET : Constants.DOODLE_BOY_RIGHT_OFFSET),
                rectangle.y,
                rectangle.width,
                rectangle.height - Constants.DOODLE_BOY_HEIGHT_OFFSET
        );
    }

    public boolean isHit() {
        return state == State.HIT;
    }

    public boolean isFalling() {
        return state == State.FALLING;
    }

    public void moveX(float amountX) {
        rectangle.x += amountX;
    }

    public boolean justShot() {
        return TimeUtils.timeSinceMillis(shootTime) < 250;
    }

    public void setShot() {
        shootTime = TimeUtils.millis();
    }

    public boolean justEquippedJetpack() {
        return TimeUtils.timeSinceMillis(jetpackTime) < 1000;
    }

    public boolean isInvincible() {
        return TimeUtils.timeSinceMillis(jetpackTime) < 3000;
    }

    public void setEquippedJetpack() {
        jetpackTime = TimeUtils.millis();
    }

    private enum State {
        JUMPING, FALLING, HIT
    }
}