package ua.gram.controller.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ua.gram.DDGame;
import ua.gram.controller.stage.GameBattleStage;
import ua.gram.controller.stage.GameUIStage;
import ua.gram.controller.tower.TowerShop;
import ua.gram.model.actor.Tower;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TowerShopInputListener extends ClickListener {

    private final GameUIStage stage_ui;
    private final GameBattleStage stage_battle;
    private final TiledMapTileLayer layer;
    private final Class<? extends ua.gram.model.actor.Tower> type;
    private final TowerShop shop;
    private ua.gram.model.actor.Tower tower;

    public TowerShopInputListener(TowerShop shop,
                                  GameBattleStage stage_battle,
                                  GameUIStage stage_ui,
                                  Class<? extends Tower> type) {
        this.shop = shop;
        this.type = type;
        this.stage_ui = stage_ui;
        this.stage_battle = stage_battle;
        this.layer = stage_battle.getLevel().getMap().getLayer();
    }

    /**
     * Put the Tower under the cursor.
     */
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        float X = event.getStageX() - event.getStageX() % DDGame.TILE_HEIGHT;
        float Y = event.getStageY() - event.getStageY() % DDGame.TILE_HEIGHT;
        try {
            tower = shop.preorder(type, X, Y);
            return true;
        } catch (NullPointerException e) {
            Gdx.app.error("EXC", "Could not get Tower from Shop: " + type.getSimpleName() + ". " + e.getMessage());
            return false;
        } catch (CloneNotSupportedException ex) {
            Gdx.app.error("EXC", "Could not clone Tower from Shop: " + type.getSimpleName() + ". " + ex.getMessage());
            return false;
        }
    }

    /**
     * Move the Tower across the map.
     */
    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        float X = event.getStageX() - event.getStageX() % DDGame.TILE_HEIGHT;
        float Y = event.getStageY() - event.getStageY() % DDGame.TILE_HEIGHT;
        if (!layer.getCell((int) (X / DDGame.TILE_HEIGHT), (int) (Y / DDGame.TILE_HEIGHT))
                .getTile().getProperties().containsKey("walkable")
                && stage_battle.isPositionEmpty(X, Y)) {
            tower.setPosition(X, Y);
            tower.toFront();
        }
    }

    /**
     * <pre>
     * Puts the Tower on the map if building is allowed.
     * As soon as tower is put on map, display building animation for 1.5 sec.
     * Then switch to idle animation and activate tower.
     * </pre>
     */
    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        float X = event.getStageX() - event.getStageX() % DDGame.TILE_HEIGHT;
        float Y = event.getStageY() - event.getStageY() % DDGame.TILE_HEIGHT;
        if (!layer.getCell((int) (X / DDGame.TILE_HEIGHT), (int) (Y / DDGame.TILE_HEIGHT))
                .getTile().getProperties().containsKey("walkable")
                && stage_battle.isPositionEmpty(X, Y)) {
            shop.build(tower, X, Y);
            if (!stage_ui.getLevel().getWave().isStarted) {
                stage_ui.getLevel().getWave().nextWave();
                stage_ui.getGameUIGroup().getCounterBut().setVisible(false);
            }
        } else {
            shop.release(tower);
        }
    }
}