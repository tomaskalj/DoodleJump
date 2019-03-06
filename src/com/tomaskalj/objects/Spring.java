package com.tomaskalj.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.tomaskalj.common.Constants;

public class Spring {
	private Texture texture;
	private Texture extendedTexture;
	private Rectangle rectangle;

	public Spring(float x, float y) {
		texture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "spring.png")));
		extendedTexture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "extended_spring.png")));
		rectangle = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Texture getExtendedTexture() {
		return extendedTexture;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void onRender(DoodleBoy doodleBoy) {
		if (!doodleBoy.isFalling()) {
			return;
		}

		if (doodleBoy.getRectangle().overlaps(rectangle)) {
			doodleBoy.onSpringCollide();
			texture = extendedTexture;
		}
	}
}
