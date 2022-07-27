package com.tomaskalj.doodlejump;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.tomaskalj.doodlejump.screens.MenuScreen;

public class DoodleJump extends Game {
    private int highScore;

    @Override
    public void create() {
        FileHandle file = Gdx.files.local("high_score.txt");
        if (file.exists()) {
            highScore = Integer.parseInt(file.readString());
        }

        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;

        FileHandle file = Gdx.files.local("high_score.txt");
        file.writeString(String.valueOf(highScore), false);
    }
}
