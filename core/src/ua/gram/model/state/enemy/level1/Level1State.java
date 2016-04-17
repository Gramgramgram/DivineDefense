package ua.gram.model.state.enemy.level1;

import ua.gram.DDGame;
import ua.gram.model.enums.Types;
import ua.gram.model.state.enemy.EnemyState;
import ua.gram.model.state.enemy.EnemyStateManager;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class Level1State extends EnemyState {

    private Types.EnemyState animationType;

    public Level1State(DDGame game, EnemyStateManager manager) {
        super(game, manager);
    }

    public Types.EnemyState getAnimationType() {
        return animationType;
    }

    public void setAnimationType(Types.EnemyState animationType) {
        this.animationType = animationType;
    }
}
