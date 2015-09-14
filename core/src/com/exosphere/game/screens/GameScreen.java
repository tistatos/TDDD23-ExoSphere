package com.exosphere.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.exosphere.game.Exosphere;

/**
 * exosphere - GameScreen
 * game screen during gameplay
 * Created by tistatos on 9/4/15.
 */
public class GameScreen extends ScreenAdapter {

    private Exosphere mGame;

    public GameScreen(Exosphere game) {
        mGame = game;



    }

    @Override
    public void render(float delta) {
        udpate(delta);
        draw();
    }

    private void draw() {

    }

    private void udpate(float delta) {
    }

    @Override
    public void dispose() {

    }
}
