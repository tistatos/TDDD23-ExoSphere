package com.exosphere.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.exosphere.game.screens.MainMenu;

public class Exosphere extends Game {


    private ModelBatch mBatcher;
    private SpriteBatch mSpriteBatcher;

	public ModelBatch getBatcher() {
		return mBatcher;
	}

    public SpriteBatch getSpriteBatcher() {
        return mSpriteBatcher;
    }

	@Override
	public void create () {
		Settings.load();
		Assets.load();
        mSpriteBatcher = new SpriteBatch();
        mBatcher  = new ModelBatch();
		setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
        super.render();
	}
}
