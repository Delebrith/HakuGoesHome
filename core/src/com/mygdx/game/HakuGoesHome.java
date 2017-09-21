package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ui.screens.PlayScreen;

public class HakuGoesHome extends Game {

	public static final int VIRTUAL_WIDTH = 400;
	public static final int VIRTUAL_HEIGHT = 192;
	public static final int PIXELS_PER_METER = 96;
	public static int level = 1;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
