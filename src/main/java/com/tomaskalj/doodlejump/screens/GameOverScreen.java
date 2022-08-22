package com.tomaskalj.doodlejump.screens;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tomaskalj.doodlejump.DoodleJump;
import com.tomaskalj.doodlejump.common.Constants;
import com.tomaskalj.doodlejump.common.Direction;
import com.tomaskalj.doodlejump.common.DrawUtil;
import com.tomaskalj.doodlejump.common.Ellipse;
import com.tomaskalj.doodlejump.objects.DoodleBoy;

public class GameOverScreen implements Screen {
    private ShapeRenderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private BitmapFont font64;
    private BitmapFont font;

    private Texture playAgainButton;
    private Texture menuButton;

    private Ellipse playAgainButtonEllipse;
    private Ellipse menuButtonEllipse;

    private final DoodleBoy doodleBoy;

    private final int score;
    private final DoodleJump game;

    public GameOverScreen(int score, DoodleBoy doodleBoy, DoodleJump game) {
        this.score = score;
        this.doodleBoy = new DoodleBoy(doodleBoy.getRectangle().x, Constants.WORLD_HEIGHT, doodleBoy.getStats(), doodleBoy.getVelocity());
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
        font = new BitmapFont(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "comic_sans.fnt")));
        font.setColor(Color.BLACK);
        playAgainButton = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "play_again_button.png")));
        menuButton = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "menu_button.png")));
        playAgainButtonEllipse = new Ellipse((Constants.WORLD_WIDTH - playAgainButton.getWidth()) / 2f, 325, playAgainButton.getWidth(), playAgainButton.getHeight());
        menuButtonEllipse = new Ellipse((Constants.WORLD_WIDTH - menuButton.getWidth()) / 2f, 250, menuButton.getWidth(), menuButton.getHeight());
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

        DrawUtil.drawTexture(batch,
                doodleBoy.getDirection() == Direction.RIGHT ? doodleBoy.getRightFacingSprite() : doodleBoy.getLeftFacingSprite(),
                doodleBoy.getRectangle().getX(), doodleBoy.getRectangle().getY());

//		DrawUtil.drawTexture(batch, playAgainButton, (Constants.WORLD_WIDTH - playAgainButton.getWidth()) / 2, 325);
//		DrawUtil.drawTexture(batch, menuButton, (Constants.WORLD_WIDTH - menuButton.getWidth()) / 2, 250);

        DrawUtil.drawTexture(batch, playAgainButton, playAgainButtonEllipse.getX(), playAgainButtonEllipse.getY());
        DrawUtil.drawTexture(batch, menuButton, menuButtonEllipse.getX(), menuButtonEllipse.getY());

        GlyphLayout layout64 = new GlyphLayout(font64, Constants.GAME_OVER_TITLE);
        font64.draw(batch, Constants.GAME_OVER_TITLE,
                (Constants.WORLD_WIDTH - layout64.width) / 2,
                Constants.WORLD_HEIGHT - 100);

        String scoreText = "your score: " + score;
        GlyphLayout scoreLayout = new GlyphLayout(font, scoreText);
        font.draw(batch, scoreText,
                (Constants.WORLD_WIDTH - scoreLayout.width) / 2,
                Constants.WORLD_HEIGHT - 200);

        String highScoreText = "your high score: " + doodleBoy.getStats().getHighScore();
        GlyphLayout highScoreLayout = new GlyphLayout(font, highScoreText);
        font.draw(batch, highScoreText,
                (Constants.WORLD_WIDTH - highScoreLayout.width) / 2,
                Constants.WORLD_HEIGHT - 250);

        batch.end();

        doodleBoy.update(delta);

        checkClick();
    }

    private void checkClick() {
        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();

            Vector2 position = viewport.unproject(new Vector2(x, y));

            if (playAgainButtonEllipse.contains(position.x, position.y)) {
                game.setScreen(new GameScreen(game));
            } else if (menuButtonEllipse.contains(position.x, position.y)) {
                game.setScreen(new MenuScreen(game));
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
        font.dispose();
        doodleBoy.dispose();
        playAgainButton.dispose();
        menuButton.dispose();
    }

    @Override
    public void dispose() {

    }
}
