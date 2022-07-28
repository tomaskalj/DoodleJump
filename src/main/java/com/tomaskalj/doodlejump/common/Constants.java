package com.tomaskalj.doodlejump.common;

public final class Constants {
    private Constants() {
    }

    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 700;

    public static final int WORLD_WIDTH = SCREEN_WIDTH;
    public static final int WORLD_HEIGHT = SCREEN_HEIGHT;

    public static final int GRID_SIZE = 10;

    public static final float DOODLE_BOY_HIT_VELOCITY = 100;
    public static final float DOODLE_BOY_JUMP_VELOCITY = 450;
    public static final float DOODLE_BOY_SPRING_VELOCITY = 750;
    public static final float DOODLE_BOY_MONSTER_HIT_VELOCITY = 550;
    public static final float DOODLE_BOY_JETPACK_VELOCITY = 1200;
    public static final float PROJECTILE_VELOCITY = 1000;
    public static final float FALLING_PLATFORM_VELOCITY = 350;

    public static final float GRAVITY = -600;

    public static final float Y_LIMIT = 60000;
    public static final float Y_INCREMENT = 35;

    public static final float DOODLE_BOY_HEIGHT = 43;
    public static final float DOODLE_BOY_WIDTH = 28;
    public static final float DOODLE_BOY_RIGHT_OFFSET = 3;
    public static final float DOODLE_BOY_LEFT_OFFSET = 15;
    public static final float DOODLE_BOY_HEIGHT_OFFSET = 38;

    public static final float MONSTER_X_OFFSET = 31f;
    public static final float MONSTER_Y_OFFSET = 29f;
    public static final float MONSTER_RADIUS = 29;

    public static final float PROJECTILE_RADIUS = 6f;

    public static final float PLATFORM_X_OFFSET = 7;
    public static final float PLATFORM_Y_OFFSET = 8;
    public static final float PLATFORM_WIDTH = 48;
    public static final float PLATFORM_HEIGHT = 6; // 14 originally

    public static final String FILE_LOCATION = "src/main/resources/assets/%s";

    public static final String GAME_TITLE = "doodle jump";
    public static final String GAME_OVER_TITLE = "game over!";

    public static final String[] MONSTER_NAMES = {"monster_1.png"};

    public static String CONTROLS = "controls:\nleft arrow/a to move left\nright arrow/d to move right\nspace to shoot\np to pause";
}
