package com.tomaskalj.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.tomaskalj.common.Constants;

public class Jetpack {
	private Texture texture;
	private Rectangle rectangle;

	public Jetpack(float x, float y) {
		texture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "jetpack.png")));
		rectangle = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
	}

	public Texture getTexture() {
		return texture;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void onRender(DoodleBoy doodleBoy) {
		if (doodleBoy.getRectangle().overlaps(rectangle)) {
			doodleBoy.setEquippedJetpack();
			if (texture != null) {
				texture.dispose();
				texture = null;
			}
		}

		if (doodleBoy.justEquippedJetpack()) {
			doodleBoy.onJetpackCollide();
		}
	}
}
