package com.tomaskalj.doodlejump;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.tomaskalj.doodlejump.screens.MenuScreen;
import lombok.Getter;

public class DoodleJump extends Game {
    @Override
    public void create() {
//        FileHandle file = Gdx.files.local("high_score.txt");
//        if (file.exists()) {
//            highScore = Integer.parseInt(file.readString());
//        }

        setScreen(new MenuScreen(this));
    }

//    public void setHighScore(int highScore) {
//        this.highScore = highScore;
//
//        FileHandle file = Gdx.files.local("high_score.txt");
//        file.writeString(String.valueOf(highScore), false);
//    }
}
