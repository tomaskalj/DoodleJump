package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.tomaskalj.doodlejump.common.Constants;

public class Projectile {
	private Circle circle;
	private Texture texture;

	public Projectile(float x, float y) {
		circle = new Circle(x, y, Constants.PROJECTILE_RADIUS);
		texture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "projectile.png")));
	}

	public Circle getCircle() {
		return circle;
	}

	public Texture getTexture() {
		return texture;
	}

	public void onRender(float delta) {
		circle.y += Constants.PROJECTILE_VELOCITY * delta;
	}
}
