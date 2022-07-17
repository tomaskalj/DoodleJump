package com.tomaskalj.doodlejump.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tomaskalj.doodlejump.DoodleJump;
import com.tomaskalj.doodlejump.common.Constants;
import com.tomaskalj.doodlejump.common.Direction;
import com.tomaskalj.doodlejump.common.DrawUtil;
import com.tomaskalj.doodlejump.common.Ellipse;
import com.tomaskalj.doodlejump.common.Range;
import com.tomaskalj.doodlejump.objects.BrokenPlatform;
import com.tomaskalj.doodlejump.objects.DisappearingPlatform;
import com.tomaskalj.doodlejump.objects.DoodleBoy;
import com.tomaskalj.doodlejump.objects.JetpackPlatform;
import com.tomaskalj.doodlejump.objects.Monster;
import com.tomaskalj.doodlejump.objects.MovingPlatform;
import com.tomaskalj.doodlejump.objects.Platform;
import com.tomaskalj.doodlejump.objects.Projectile;
import com.tomaskalj.doodlejump.objects.SpringPlatform;
import com.tomaskalj.doodlejump.objects.StandardPlatform;

public class GameScreen implements Screen {
	private ShapeRenderer renderer;
	private OrthographicCamera camera;
	private Viewport viewport;

	private SpriteBatch batch;
	private BitmapFont scoreFont;
	private BitmapFont font;

	private DoodleBoy doodleBoy;
	private Array<Platform> platforms;
	private Array<Monster> monsters;
	private Array<Projectile> projectiles;

	private int yMin;
	private int yMax;
	private int time;
	private int score;

	private boolean paused;

	private Texture resumeButton;
	private Texture menuButton;

	private Ellipse resumeButtonEllipse;
	private Ellipse menuButtonEllipse;

	private DoodleJump game;

	public GameScreen(DoodleJump game) {
		this.game = game;
	}

	@Override
	public void show() {
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
		batch = new SpriteBatch();
		scoreFont = new BitmapFont(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "comic_sans.fnt")));
		scoreFont.setColor(Color.BLACK);
		font = new BitmapFont(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "doodle_jump_64.fnt")));
		font.setColor(Color.RED);
		platforms = new Array<>();
		monsters = new Array<>();
		projectiles = new Array<>();
		yMin = 0;
		yMax = Constants.WORLD_HEIGHT;
		resumeButton = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "resume_button.png")));
		menuButton = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "menu_button.png")));
		resumeButtonEllipse = new Ellipse((Constants.WORLD_WIDTH - resumeButton.getWidth()) / 2, camera.position.y, resumeButton.getWidth(), resumeButton.getHeight());
		menuButtonEllipse = new Ellipse((Constants.WORLD_WIDTH - menuButton.getWidth()) / 2, camera.position.y - 75, menuButton.getWidth(), menuButton.getHeight());

		Range[] ranges = {
				new Range(0, Constants.SCREEN_HEIGHT / 3),
				new Range(Constants.SCREEN_HEIGHT / 3, Constants.SCREEN_HEIGHT / 3 * 2),
				new Range(Constants.SCREEN_HEIGHT / 3 * 2, Constants.SCREEN_HEIGHT)
		};

		for (Range range : ranges) {
			for (int i = 0; i < 8; i++) {
				Platform platform = generatePlatform(range.getMin(), range.getMax(), false);
				if (platform != null) {
					platforms.add(platform);
				}
			}
		}

		Platform lowestPlatform = platforms.get(0);
		for (Platform platform : platforms) {
			if (platform.getY() < lowestPlatform.getY()) {
				lowestPlatform = platform;
			}
		}

		doodleBoy = new DoodleBoy(lowestPlatform.getX() + 35, lowestPlatform.getY() + 20);
		doodleBoy.onPlatformCollide();
	}

	@Override
	public void render(float delta) {
		viewport.apply();
		Gdx.gl.glClearColor(248 / 255f, 248 / 255f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		checkKeys();

		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(245 / 255f, 222 / 255f, 179 / 255f, 1f);

		DrawUtil.drawGrid(renderer, 0, Constants.WORLD_WIDTH, 0, yMax, Constants.GRID_SIZE);

		renderer.end();

		if (paused) {
			resumeButtonEllipse.setY(camera.position.y);
			menuButtonEllipse.setY(camera.position.y - 75);

			if (Gdx.input.justTouched()) {
				int x = Gdx.input.getX();
				int y = Gdx.input.getY();

				Vector2 position = viewport.unproject(new Vector2(x, y));

				if (resumeButtonEllipse.contains(position.x, position.y)) {
					paused = false;
				} else if (menuButtonEllipse.contains(position.x, position.y)) {
					game.setScreen(new MenuScreen(game));
				}
			}

			batch.setProjectionMatrix(camera.combined);
			batch.begin();

			DrawUtil.drawTexture(batch, resumeButton, resumeButtonEllipse.getX(), camera.position.y);
			DrawUtil.drawTexture(batch, menuButton, menuButtonEllipse.getX(), camera.position.y - 75);

			String text = "paused";
			GlyphLayout layout = new GlyphLayout(font, text);
			font.draw(batch, text,
					(Constants.WORLD_WIDTH - layout.width) / 2,
					camera.position.y + 150);

			batch.end();
			return;
		}

		doodleBoy.update(delta);

		updateCamera();

		if ((int) doodleBoy.getRectangle().y != (int) camera.position.y) {
			if (doodleBoy.getRectangle().y > score) {
				score = (int) doodleBoy.getRectangle().y;
			}
		} else {
			score = (int) camera.position.y;
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		Array<Platform> platformsToRemove = new Array<>();
		Array<Monster> monstersToRemove = new Array<>();
		Array<Projectile> projectilesToRemove = new Array<>();

		for (Platform platform : platforms) {
			if (platform.getY() < camera.position.y - (30 + Constants.WORLD_HEIGHT / 2)) {
				platformsToRemove.add(platform);
				continue;
			}

			DrawUtil.drawTexture(batch, platform.getTexture(), platform.getX(), platform.getY());
			platform.onRender(delta, batch);

			if (platform instanceof SpringPlatform) {
				((SpringPlatform) platform).getSpring().onRender(doodleBoy);
			} else if (platform instanceof JetpackPlatform) {
				((JetpackPlatform) platform).getJetpack().onRender(doodleBoy);
			}
		}

		for (Monster monster : monsters) {
			if (monster.getY() < camera.position.y - (30 + Constants.WORLD_HEIGHT / 2)) {
				monstersToRemove.add(monster);
				continue;
			}

			DrawUtil.drawTexture(batch, monster.getTexture(), monster.getX(), monster.getY());
			monster.onRender(delta);
		}

		for (Projectile proj : projectiles) {
			if (proj.getCircle().y > camera.position.y + (Constants.WORLD_HEIGHT / 2)) {
				projectilesToRemove.add(proj);
				continue;
			}

			DrawUtil.drawTexture(batch, proj.getTexture(), proj.getCircle().x, proj.getCircle().y);
			proj.onRender(delta);
		}

		if (doodleBoy.justShot()) {
			DrawUtil.drawTexture(batch, doodleBoy.getShootingSprite(), doodleBoy.getRectangle().getX(), doodleBoy.getRectangle().getY());
		} else if (doodleBoy.justEquippedJetpack()) {
			DrawUtil.drawTexture(batch,
					doodleBoy.getDirection() == Direction.RIGHT ? doodleBoy.getJetpackRightSprite() : doodleBoy.getJetpackLeftSprite(),
					doodleBoy.getRectangle().getX(), doodleBoy.getRectangle().getY());
		} else {
			DrawUtil.drawTexture(batch,
					doodleBoy.getDirection() == Direction.RIGHT ? doodleBoy.getRightFacingSprite() : doodleBoy.getLeftFacingSprite(),
					doodleBoy.getRectangle().getX(), doodleBoy.getRectangle().getY());
		}

		scoreFont.draw(batch, String.valueOf(score), 20, camera.position.y + (Constants.WORLD_HEIGHT / 2 - 10));

		batch.end();

		checkCollisions();

		checkDeath();

		time++;

		// I don't want it looping every render call to avoid unnecessary lag
		if (time % 150 == 0) {
			for (int i = 0; i < platformsToRemove.size; i++) {
				platforms.removeValue(platformsToRemove.get(i), true);
				platformsToRemove.get(i).dispose();
			}

			platformsToRemove.clear();

			for (int i = 0; i < monstersToRemove.size; i++) {
				monsters.removeValue(monstersToRemove.get(i), true);
				monstersToRemove.get(i).getTexture().dispose();
			}

			monstersToRemove.clear();

			for (int i = 0; i < projectilesToRemove.size; i++) {
				projectiles.removeValue(projectilesToRemove.get(i), true);
				projectilesToRemove.get(i).getTexture().dispose();
			}

			projectilesToRemove.clear();
		}
	}

	private boolean circlesOverlap(Circle circle) {
		if (circle == null) {
			return false;
		}

		for (Platform other : platforms) {
			if (Intersector.overlaps(circle, other.getRectangle()) || other.withinRange(circle.x, circle.y)) {
				return true;
			}
		}

		for (Monster monster : monsters) {
			if (circle.overlaps(monster.getCircle())) {
				return true;
			}
		}

		return false;
	}

	private boolean rectanglesOverlap(Rectangle rectangle) {
		if (rectangle == null) {
			return false;
		}

		for (Platform other : platforms) {
			if (other.getRectangle().overlaps(rectangle) || other.withinRange(rectangle.x, rectangle.y)) {
				return true;
			}
		}

		for (Monster monster : monsters) {
			if (Intersector.overlaps(monster.getCircle(), rectangle)) {
				return true;
			}
		}

		return false;
	}

	private boolean platformsOverlap(Platform platform) {
		if (platform instanceof MovingPlatform) {
			Rectangle row = new Rectangle(0, platform.getY(), Constants.WORLD_WIDTH, platform.getRectangle().height);
			for (Platform other : platforms) {
				if (other.getRectangle().overlaps(row)) {
					return true;
				}
			}
		}

		return rectanglesOverlap(platform.getRectangle());
	}

	private Platform generatePlatform(int yMin, int yMax, boolean variant) {
		Platform platform = null;

		int x = MathUtils.random(0, Constants.WORLD_WIDTH - 75);
		int y = MathUtils.random(yMin, yMax - 10);
		double chance = Math.random();

		if (variant) {
			if (chance <= 0.2) {
				platform = new MovingPlatform(x, y);
			} else if (chance > 0.2 && chance <= 0.25) {
				platform = new BrokenPlatform(x, y);
			} else if (chance > 0.25 && chance <= 0.35) {
				platform = new DisappearingPlatform(x, y);
			} else if (chance > 0.35 && chance <= 0.4) {
				platform = new SpringPlatform(x, y);
			} else if (chance > 0.4 && chance <= 0.41) {
				platform = new JetpackPlatform(x, y);
			} else if (chance > 0.41 && chance <= 0.9) {
				platform = new StandardPlatform(x, y);
			}
		} else {
			platform = new StandardPlatform(x, y);
		}

		if (platform != null && platformsOverlap(platform)) {
			platform = null;
		}

		return platform;
	}

	private Monster generateMonster() {
		int x = MathUtils.random(0, Constants.WORLD_WIDTH - 50);
		int y = MathUtils.random(yMin, yMax - 10);

		Monster monster = new Monster(x, y);

		if (circlesOverlap(monster.getCircle())) {
			monster = null;
		}

		return monster;
	}

	private void checkDeath() {
		if (doodleBoy.getRectangle().y < camera.position.y - (Constants.WORLD_HEIGHT / 2)) {
			game.setScreen(new GameOverScreen(score, doodleBoy, game));

			if (score > game.getHighScore()) {
				game.setHighScore(score);
			}
		}
	}

	private void checkKeys() {
		if (!doodleBoy.isHit()) {
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
				doodleBoy.setDirection(Direction.RIGHT);
				doodleBoy.getRectangle().x += 5;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
				doodleBoy.setDirection(Direction.LEFT);
				doodleBoy.getRectangle().x -= 5;
			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				if (!doodleBoy.justEquippedJetpack()) {
					doodleBoy.setShot();
					projectiles.add(new Projectile(doodleBoy.getRectangle().x + 9, doodleBoy.getRectangle().y + doodleBoy.getShootingSprite().getHeight()));
				}
			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
				paused = !paused;
			}
		}

		camera.update();
	}

	private void checkCollisions() {
		if (doodleBoy.isHit() || doodleBoy.isInvincible()) {
			return;
		}

		for (int i = 0; i < platforms.size; i++) {
			Platform platform = platforms.get(i);

			Rectangle doodleBoyFeet = new Rectangle(doodleBoy.getRectangle().x + (doodleBoy.getDirection() == Direction.LEFT ? Constants.DOODLE_BOY_LEFT_OFFSET : Constants.DOODLE_BOY_RIGHT_OFFSET), doodleBoy.getRectangle().y, doodleBoy.getRectangle().width, doodleBoy.getRectangle().height - Constants.DOODLE_BOY_HEIGHT_OFFSET);

			Rectangle topPlatform = new Rectangle(platform.getX() + Constants.PLATFORM_X_OFFSET, platform.getY() + Constants.PLATFORM_Y_OFFSET, platform.getRectangle().width, platform.getRectangle().height);

			if (doodleBoy.isFalling() && doodleBoyFeet.overlaps(topPlatform)) {
				platform.onCollide(doodleBoy);

				if (platform instanceof DisappearingPlatform) {
					platform.getTexture().dispose();
					platforms.removeIndex(i);
				}
			}
		}

		for (int i = 0; i < monsters.size; i++) {
			Monster monster = monsters.get(i);
			Circle actualCircle = new Circle(monster.getX() + Constants.MONSTER_X_OFFSET, monster.getY() + Constants.MONSTER_Y_OFFSET, Constants.MONSTER_RADIUS);

			if (Intersector.overlaps(actualCircle, doodleBoy.getRectangle())) {
				doodleBoy.onMonsterCollide(monster);
			}

			for (int j = 0; j < projectiles.size; j++) {
				Projectile proj = projectiles.get(j);

				if (proj.getCircle().overlaps(monster.getCircle())) {
					monsters.removeIndex(i);
					projectiles.removeIndex(j);
					monster.getTexture().dispose();
					proj.getTexture().dispose();
				}
			}
		}
	}

	private void updateCamera() {
		if (doodleBoy.getRectangle().y > camera.position.y) {
			camera.position.y = doodleBoy.getRectangle().y;

			if (camera.position.y < Constants.Y_LIMIT && yMax < Constants.Y_LIMIT) {
				yMin = yMax;
				yMax += Constants.Y_INCREMENT;

				Platform platform = generatePlatform(yMin, yMax, true);
				if (platform != null) {
					platforms.add(platform);
				}
			}

			if (Math.random() < 0.1) {
				Monster monster = generateMonster();
				if (monster != null) {
					monsters.add(monster);
				}
			}
		}

		camera.update();
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
		doodleBoy.getRightFacingSprite().dispose();
		doodleBoy.getLeftFacingSprite().dispose();
		doodleBoy.getShootingSprite().dispose();
		platforms.forEach(Platform::dispose);
		platforms.clear();
		monsters.forEach(monster -> monster.getTexture().dispose());
		monsters.clear();
		projectiles.forEach(proj -> proj.getTexture().dispose());
		projectiles.clear();
		resumeButton.dispose();
		menuButton.dispose();
	}

	@Override
	public void dispose() {

	}
}