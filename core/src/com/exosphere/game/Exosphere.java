package com.exosphere.game;

import com.badlogic.gdx.Game;
import com.exosphere.game.screens.MainMenu;

public class Exosphere extends Game {

	@Override
	public void create () {
		Settings.load();
		Assets.load();

		setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
        super.render();
	}
}
