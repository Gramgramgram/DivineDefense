package ua.gram.controller.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;

import ua.gram.DDGame;
import ua.gram.model.prototype.ui.window.WindowPrototype;
import ua.gram.model.window.MainMenuWindow;

/**
 *
 * @author Gram <gram7gram@gmail.com>
 */
public class MainMenuStage extends AbstractStage {

    private final WindowPrototype prototype;

    public MainMenuStage(DDGame game) {
        super(game);
        prototype = game.getPrototype().ui.getWindow("main-menu");
        Actor window = new MainMenuWindow(game, getSkin(), prototype);
        addActor(window);
    }

    public WindowPrototype getPrototype() {
        return prototype;
    }
}
