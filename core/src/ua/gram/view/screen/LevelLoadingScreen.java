package ua.gram.view.screen;

import com.badlogic.gdx.Gdx;
import ua.gram.DDGame;
import ua.gram.controller.Resources;
import ua.gram.controller.factory.LevelFactory;
import ua.gram.model.Level;
import ua.gram.view.AbstractLoadingScreen;

/**
 * Loads resources essential for the game process itself: map, external sprites - and creates a Level.
 * TODO implement hints for player
 * TODO Add progress bar
 *
 * @author Gram <gram7gram@gmail.com>
 */
public class LevelLoadingScreen extends AbstractLoadingScreen {

    private final int lvl;

    public LevelLoadingScreen(DDGame game, int lvl) {
        super(game);
        this.lvl = lvl;
        Gdx.app.log("INFO", "Screen set to LevelLoadingScreen");
    }

    /**
     * NOTE: It is essential to call super.show() for stage to be created
     */
    @Override
    public void show() {
        super.show();
        Resources resources = game.getResources();
        resources.loadMap(lvl);
        resources.loadTexture(Resources.WEAPON_START_BACK);
        resources.loadTexture(Resources.WEAPON_START_OVER);
        resources.loadTexture(Resources.WEAPON_MIDDLE_BACK);
        resources.loadTexture(Resources.WEAPON_MIDDLE_OVER);
        resources.loadTexture(Resources.WEAPON_END_BACK);
        resources.loadTexture(Resources.WEAPON_END_OVER);
        resources.loadTexture(Resources.AIM_TEXTURE);
        resources.loadTexture(Resources.RANGE_TEXTURE);
    }

    @Override
    public void doAction() {
        stage_ui.update(progress);
        Gdx.app.log("INFO", "Loading " + progress + "%");
        LevelFactory container = game.getFactory("data/levels/level" + lvl + ".json", LevelFactory.class);
        Level level = container.create(Level.class);
        level.create(game, lvl);
        game.setScreen(new GameScreen(game, level));
    }

}