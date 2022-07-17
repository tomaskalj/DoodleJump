package com.tomaskalj.doodlejump;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tomaskalj.doodlejump.common.Constants;

public class GameLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        // Want the screen to be a fixed size and not able to be resized, so min width/height is same as max width/height
        config.setWindowSizeLimits(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        config.setResizable(false);

        new Lwjgl3Application(new DoodleJump(), config);
    }
}
