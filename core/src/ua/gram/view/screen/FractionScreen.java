package ua.gram.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import ua.gram.DDGame;
import ua.gram.controller.Resources;
import ua.gram.controller.stage.FractionStage;
import ua.gram.view.AbstractScreen;

/**
 * <pre>
 * TODO On following launches display Unlock button for opposite fraction
 * TODO Send AJAX request for statistics about fraction
 * </pre>
 *
 * @author Gram
 */
public class FractionScreen extends AbstractScreen {

    private final FractionStage stage_ui;
    private final Sprite lockImage;
    private final Sprite background;
    private final SpriteBatch batch;

    public FractionScreen(DDGame game) {
        super(game);
        stage_ui = new FractionStage(game);
        batch = game.getBatch();
        lockImage = new Sprite(game.getResources().getTexture(Resources.LOCK_TEXTURE));
        background = new Sprite(game.getResources().getTexture(Resources.BACKGROUND_TEXTURE));
        Gdx.app.log("INFO", "Screen set to FractionScreen");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage_ui);
        Button demon = stage_ui.getDemonButton();
        lockImage.setPosition(
                demon.getX() + demon.getWidth() / 2f - lockImage.getWidth() / 2f,
                demon.getY() + demon.getHeight() / 2f - lockImage.getHeight() / 2f
        );
        background.setSize(DDGame.WORLD_WIDTH, DDGame.WORLD_HEIGHT);
        background.setPosition(0, 0);
    }

    @Override
    public void render_ui(float delta) {
        Gdx.gl.glClearColor(0, 111 / 255f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage_ui.act(delta);
        batch.begin();
        background.draw(batch);
        batch.end();
        stage_ui.draw();
        batch.begin();
        if (stage_ui.getGroup() == null || !stage_ui.getGroup().isVisible()) {
            lockImage.draw(batch);
        }
        batch.end();

    }

    @Override
    public void render_other(float delta) {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        lockImage.getTexture().dispose();
    }
}