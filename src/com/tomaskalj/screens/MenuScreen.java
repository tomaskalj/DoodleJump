package com.tomaskalj.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tomaskalj.DoodleJump;
import com.tomaskalj.common.Constants;
import com.tomaskalj.common.Direction;
import com.tomaskalj.common.DrawUtil;
import com.tomaskalj.common.Ellipse;
import com.tomaskalj.objects.DoodleBoy;
import com.tomaskalj.objects.Platform;
import com.tomaskalj.objects.StandardPlatform;

public class MenuScreen implements Screen {
	private ShapeRenderer renderer;
	private OrthographicCamera camera;
	private Viewport viewport;

	private SpriteBatch batch;
	private BitmapFont font64;
	private BitmapFont font28;

	private Texture playButton;

	private DoodleBoy doodleBoy;
	private Platform[] platformLocations;

	private Ellipse playButtonEllipse;

	private DoodleJump game;

	public MenuScreen(DoodleJump game) {
		this.game = game;
	}

	@Override
	public void show() {
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
		batch = new SpriteBatch();
		font64 = new BitmapFont(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "doodle_jump_64.fnt")));
		font64.setColor(Color.RED);
		font28 = new BitmapFont(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "doodle_jump_28.fnt")));
		font28.setColor(Color.BLACK);
		playButton = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "play_button.png")));
		playButtonEllipse = new Ellipse((Constants.WORLD_WIDTH - playButton.getWidth()) / 2, Constants.WORLD_HEIGHT - 220, playButton.getWidth(), playButton.getHeight());

		doodleBoy = new DoodleBoy(65, 250);

		platformLocations = new Platform[] {
				new StandardPlatform(50, 150),
				new StandardPlatform(165, 355),
				new StandardPlatform(350, 400),
//				new StandardPlatform(255, 200),
				new StandardPlatform(400, 320)
		};
	}

	@Override
	public void render(float delta) {
		viewport.apply();
		Gdx.gl.glClearColor(248 / 255f, 248 / 255f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(245 / 255f, 222 / 255f, 179 / 255f, 1f);

		DrawUtil.drawGrid(renderer, 0, Constants.WORLD_WIDTH, 0, Constants.WORLD_HEIGHT, Constants.GRID_SIZE);

		renderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		GlyphLayout layout64 = new GlyphLayout(font64, Constants.GAME_TITLE);
		font64.draw(batch, Constants.GAME_TITLE,
				(Constants.WORLD_WIDTH - layout64.width) / 2,
				Constants.WORLD_HEIGHT - 100);

		font28.draw(batch, Constants.CONTROLS, 150, 275);

		DrawUtil.drawTexture(batch, playButton, playButtonEllipse.getX(), playButtonEllipse.getY());

		for (Platform platform : platformLocations) {
			DrawUtil.drawTexture(batch, platform.getTexture(), platform.getX(), platform.getY());
		}

		DrawUtil.drawTexture(batch,
				doodleBoy.getDirection() == Direction.RIGHT ? doodleBoy.getRightFacingSprite() : doodleBoy.getLeftFacingSprite(),
				doodleBoy.getRectangle().getX(), doodleBoy.getRectangle().getY());

		batch.end();

		doodleBoy.update(delta);

		checkCollisions();

		if (Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();

			Vector2 position = viewport.unproject(new Vector2(x, y));

			if (playButtonEllipse.contains(position.x, position.y)) {
				game.setScreen(new GameScreen(game));
			}
		}
	}

	private void checkCollisions() {
		for (Platform platform : platformLocations) {
			Rectangle doodleBoyFeet = new Rectangle(doodleBoy.getRectangle().x + (doodleBoy.getDirection() == Direction.LEFT ? Constants.DOODLE_BOY_LEFT_OFFSET : Constants.DOODLE_BOY_RIGHT_OFFSET), doodleBoy.getRectangle().y, doodleBoy.getRectangle().width, doodleBoy.getRectangle().height - Constants.DOODLE_BOY_HEIGHT_OFFSET);

			Rectangle topPlatform = new Rectangle(platform.getX() + Constants.PLATFORM_X_OFFSET, platform.getY() + Constants.PLATFORM_Y_OFFSET, platform.getRectangle().width, platform.getRectangle().height);

			if (doodleBoyFeet.overlaps(topPlatform)) {
				platform.onCollide(doodleBoy);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		renderer.dispose();
		batch.dispose();
		font64.dispose();
		font28.dispose();
		playButton.dispose();
		doodleBoy.getLeftFacingSprite().dispose();
		doodleBoy.getRightFacingSprite().dispose();
		doodleBoy.getShootingSprite().dispose();

		for (Platform platform : platformLocations) {
			platform.dispose();
		}
	}

	@Override
	public void dispose() {

	}
}
