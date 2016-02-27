package ua.gram.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ua.gram.DDGame;
import ua.gram.controller.Log;
import ua.gram.controller.Resources;
import ua.gram.controller.stage.LevelSelectStage;
import ua.gram.model.prototype.GamePrototype;
import ua.gram.view.AbstractScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LevelSelectScreen extends AbstractScreen {

    private final LevelSelectStage stage;
    private final Sprite background;

    public LevelSelectScreen(DDGame game, GamePrototype prototype) {
        super(game, prototype);
        stage = new LevelSelectStage(game, prototype);
        background = new Sprite(game.getResources().getTexture(Resources.BACKGROUND_TEXTURE));
        Log.info("LevelSelectScreen is OK");
    }

    @Override
    public void show() {
        Log.info("Screen set to LevelSelectScreen");
        Gdx.input.setInputProcessor(stage);
        background.setSize(DDGame.WORLD_WIDTH, DDGame.WORLD_HEIGHT);
    }

    @Override
    public void renderUiElements(float delta) {
        Gdx.gl.glClearColor(0, 111 / 255f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.getBatch().begin();
        background.draw(stage.getBatch());
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void renderOtherElements(float delta) {

    }

    @Override
    public void hide() {
        Log.warn("Hiding LevelSelectScreen");
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Log.warn("LevelSelectScreen disposed");
    }
}
