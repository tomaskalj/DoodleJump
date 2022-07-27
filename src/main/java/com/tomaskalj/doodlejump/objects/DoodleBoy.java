package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.tomaskalj.doodlejump.common.Constants;
import com.tomaskalj.doodlejump.common.Direction;

public class DoodleBoy {
    private Rectangle rectangle;
    private State state;
    private Direction direction;
    private Texture rightFacingSprite;
    private Texture leftFacingSprite;
    private Texture shootingSprite;
    private Texture jetpackRightSprite;
    private Texture jetpackLeftSprite;

    private long shootTime;
    private long jetpackTime;

    // Physics
    private Vector2 position;
    private Vector2 velocity;

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

    public boolean isHit() {
        return state == State.HIT;
    }

    public boolean isFalling() {
        return state == State.FALLING;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Texture getRightFacingSprite() {
        return rightFacingSprite;
    }

    public Texture getLeftFacingSprite() {
        return leftFacingSprite;
    }

    public Texture getShootingSprite() {
        return shootingSprite;
    }

    public Texture getJetpackRightSprite() {
        return jetpackRightSprite;
    }

    public Texture getJetpackLeftSprite() {
        return jetpackLeftSprite;
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
