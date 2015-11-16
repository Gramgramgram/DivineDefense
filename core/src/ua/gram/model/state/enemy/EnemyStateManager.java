package ua.gram.model.state.enemy;

import com.badlogic.gdx.Gdx;
import ua.gram.DDGame;
import ua.gram.model.actor.enemy.Enemy;
import ua.gram.model.state.StateInterface;
import ua.gram.model.state.StateManager;
import ua.gram.model.state.enemy.level1.*;
import ua.gram.model.state.enemy.level2.IdleState;
import ua.gram.model.state.enemy.level2.Level2State;
import ua.gram.model.state.enemy.level2.WalkingState;
import ua.gram.model.state.enemy.level3.AbilityState;
import ua.gram.model.state.enemy.level3.Level3State;
import ua.gram.model.state.enemy.level4.Level4State;
import ua.gram.model.state.enemy.level4.StunState;

import java.util.Arrays;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public final class EnemyStateManager extends StateManager<Enemy> {

    private final DDGame game;
    //Level 1
    private DeadState deadState;
    private FinishState finishState;
    private SpawnState spawnState;
    private ActiveState activeState;
    private InactiveState inactiveState;
    //Level 2
    private IdleState idleState;
    private WalkingState walkingState;
    //Level 3
    private AbilityState abilityState;
    //Level 4
    private StunState stunState;

    public EnemyStateManager(DDGame game) {
        super(game);
        this.game = game;
    }

    @Override
    public void init(Enemy enemy) {
        if (activeState == null) activeState = new ActiveState(game);
        if (inactiveState == null) inactiveState = new InactiveState(game);
        if (idleState == null) idleState = new IdleState(game);
        if (walkingState == null) walkingState = new WalkingState(game);
        if (abilityState == null) abilityState = new AbilityState(game);
        if (stunState == null) stunState = new StunState(game);
        if (deadState == null) deadState = new DeadState(game);
        if (finishState == null) finishState = new FinishState(game);
        if (spawnState == null) spawnState = new SpawnState(game);
    }

    @Override
    public void update(Enemy enemy, float delta) {
        if (enemy == null) return;
        if (enemy.getCurrentLevel1State() != null) try {
            enemy.getCurrentLevel1State().manage(enemy, delta);
        } catch (Exception e) {
            Gdx.app.error("EXC", "Could not manage Level1State on " + enemy
                    + "\r\n" + e.getMessage()
                    + "\r\n" + Arrays.toString(e.getStackTrace()));
        }
        if (enemy.getCurrentLevel2State() != null) try {
            enemy.getCurrentLevel2State().manage(enemy, delta);
        } catch (Exception e) {
            Gdx.app.error("EXC", "Could not manage Level2State on " + enemy
                    + "\r\n" + e.getMessage()
                    + "\r\n" + Arrays.toString(e.getStackTrace()));
        }
        if (enemy.getCurrentLevel3State() != null) try {
            enemy.getCurrentLevel3State().manage(enemy, delta);
        } catch (Exception e) {
            Gdx.app.error("EXC", "Could not manage Level3State on " + enemy
                    + "\r\n" + e.getMessage()
                    + "\r\n" + Arrays.toString(e.getStackTrace()));
        }
        if (enemy.getCurrentLevel4State() != null) try {
            enemy.getCurrentLevel4State().manage(enemy, delta);
        } catch (Exception e) {
            Gdx.app.error("EXC", "Could not manage Level4State on " + enemy
                    + "\r\n" + e.getMessage()
                    + "\r\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void swap(Enemy enemy, StateInterface current, StateInterface newState, int level) {
        if (enemy == null) return;

        if (current == null && newState == null)
            throw new NullPointerException("Could not swap both empty states");

        if (current != null) {
            try {
                current.postManage(enemy);
            } catch (Exception e) {
                Gdx.app.error("EXC", "Could not execute postManage() on "
                        + enemy + "'s state " + current
                        + "\r\n" + e.getMessage()
                        + "\r\n" + Arrays.toString(e.getStackTrace()));
            }
        }

        if (current == newState) {
            Gdx.app.log("WARN", "Ignored swap " + current + " to " + newState + " on " + enemy);
            return;
        }

        try {
            this.persist(enemy, newState, level);
        } catch (Exception e) {
            Gdx.app.error("EXC", "Could not execute persist() on "
                    + enemy + "'s state " + newState
                    + "\r\n" + e.getMessage()
                    + "\r\n" + Arrays.toString(e.getStackTrace()));
        }

        if (newState != null) {
            try {
                newState.preManage(enemy);
            } catch (Exception e) {
                Gdx.app.error("EXC", "Could not execute preManage() on "
                        + enemy + "'s state " + newState
                        + "\r\n" + e.getMessage()
                        + "\r\n" + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public void swapLevel1State(Enemy enemy, Level1State state) {
        swap(enemy, enemy.getCurrentLevel1State(), state, 1);
    }

    public void swapLevel2State(Enemy enemy, Level2State state) {
        swap(enemy, enemy.getCurrentLevel2State(), state, 2);
    }

    public void swapLevel3State(Enemy enemy, Level3State state) {
        swap(enemy, enemy.getCurrentLevel3State(), state, 3);
    }

    public void swapLevel4State(Enemy enemy, Level4State state) {
        swap(enemy, enemy.getCurrentLevel4State(), state, 4);
    }

    @Override
    public void persist(Enemy enemy, StateInterface newState, int level) throws NullPointerException {
        if (newState instanceof Level1State) {
            enemy.setCurrentLevel1State((Level1State) newState);
        } else if (newState instanceof Level2State) {
            enemy.setCurrentLevel2State((Level2State) newState);
        } else if (newState instanceof Level3State) {
            enemy.setCurrentLevel3State((Level3State) newState);
        } else if (newState instanceof Level4State) {
            enemy.setCurrentLevel4State((Level4State) newState);
        } else {
            switch (level) {
                case 1:
                    enemy.setCurrentLevel1State(null);
                    break;
                case 2:
                    enemy.setCurrentLevel2State(null);
                    break;
                case 3:
                    enemy.setCurrentLevel3State(null);
                    break;
                case 4:
                    enemy.setCurrentLevel4State(null);
                    break;
                default:
                    throw new NullPointerException("Unknown state: " + newState);
            }
        }
    }

    @Override
    public void reset(Enemy enemy) {
        enemy.setCurrentLevel1State(null);
        enemy.setCurrentLevel2State(null);
        enemy.setCurrentLevel3State(null);
        enemy.setCurrentLevel4State(null);
        Gdx.app.log("INFO", enemy + " states has been reseted");
    }

    public DeadState getDeadState() {
        return deadState;
    }

    public FinishState getFinishState() {
        return finishState;
    }

    public IdleState getIdleState() {
        return idleState;
    }

    public StunState getStunState() {
        return stunState;
    }

    public SpawnState getSpawnState() {
        return spawnState;
    }

    public WalkingState getWalkingState() {
        return walkingState;
    }

    public ActiveState getActiveState() {
        return activeState;
    }

    public InactiveState getInactiveState() {
        return inactiveState;
    }

    public AbilityState getAbilityState() {
        return abilityState;
    }
}
