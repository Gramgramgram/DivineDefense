package ua.gram.controller.tower;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ua.gram.controller.pool.animation.AnimationManager;
import ua.gram.controller.pool.animation.AnimationProvider;
import ua.gram.controller.pool.animation.TowerAnimationManager;
import ua.gram.model.enums.Types;
import ua.gram.model.prototype.tower.TowerPrototype;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TowerAnimationProvider extends AnimationProvider<TowerPrototype, Types.TowerState, Types.TowerLevels> {

    public TowerAnimationProvider(Skin skin, TowerPrototype[] registeredTypes) {
        super(skin, registeredTypes);
    }

    @Override
    protected AnimationManager<TowerPrototype, Types.TowerState, Types.TowerLevels> getInstance(TowerPrototype prototype) {
        return new TowerAnimationManager(getSkin(), prototype);
    }

}
