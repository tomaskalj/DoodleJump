package com.tomaskalj.common;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DrawUtil {
	public static void drawGrid(ShapeRenderer renderer, int xMin, int xMax, int yMin, int yMax, int amount) {
		for (int i = yMin; i < yMax; i += amount) {
			renderer.line(xMin, i, xMax, i);
		}

		for (int i = xMin; i < xMax; i += amount) {
			renderer.line(i, yMin, i, yMax);
		}
	}

	public static void drawTexture(SpriteBatch batch, Texture texture, float x, float y) {
		batch.draw(texture, x, y);
	}
}