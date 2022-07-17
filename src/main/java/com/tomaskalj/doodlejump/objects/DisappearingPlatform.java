package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DisappearingPlatform extends Platform {
	public DisappearingPlatform(float x, float y) {
		super(x, y, "disappearing_platform.png");
	}

	@Override
	public void onRender(float delta, SpriteBatch batch) {

	}
}
