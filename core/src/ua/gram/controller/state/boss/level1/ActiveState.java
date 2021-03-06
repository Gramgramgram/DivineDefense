package ua.gram.controller.state.boss.level1;

import ua.gram.DDGame;
import ua.gram.controller.state.boss.BossStateManager;
import ua.gram.model.actor.boss.Boss;
import ua.gram.model.enums.Types;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ActiveState extends Level1State {

    public ActiveState(DDGame game, BossStateManager manager) {
        super(game, manager);
    }

    @Override
    protected Types.BossState getType() {
        return null;
    }

    @Override
    public void preManage(Boss actor) {
        super.preManage(actor);
        manager.swapLevel2State(actor, manager.getIdleState());
    }
}
