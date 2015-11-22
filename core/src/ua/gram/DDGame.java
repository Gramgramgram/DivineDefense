package ua.gram;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.gram.controller.Resources;
import ua.gram.controller.security.SecurityHandler;
import ua.gram.model.Player;
import ua.gram.model.prototype.GamePrototype;
import ua.gram.model.prototype.ParametersPrototype;
import ua.gram.view.screen.ErrorScreen;
import ua.gram.view.screen.LaunchLoadingScreen;

/**
 * Game enter point
 *
 * TODO Make different assets for 4x3 and 16x9(16x10) screens
 * TODO For 16x9(16x10) make StretchViewport
 * TODO Add to JSON pressed and disabled drawable for buttons
 * TODO Add copyrights
 * TODO Handle Android Menu, Back click events
 * FIXME Logotype
 * FIXME Switch 16pt font with 20pt
 * NOTE Enemies should be tough because the amount of towers is unlimited
 * NOTE Skin should not contain nonexistent values
 *
 * @author Gram <gram7gram@gmail.com>
 */
public class DDGame<P extends GamePrototype> extends Game {

    public static final String ANGEL = "Angel";
    public static final String DEMON = "Demon";
    public static final byte TILE_HEIGHT = 60;
    public static final byte DEFAULT_BUTTON_HEIGHT = 80;
    public static boolean DEBUG = true;
    public static boolean PAUSE = false;
    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;
    public static int MAX_ENTITIES;
    private final P prototype;
    private SecurityHandler security;
    private float gameSpeed = 1;
    private Resources resources;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport view;
    private Player player;
    private BitmapFont info;

    public DDGame(SecurityHandler security, P prototype) {
        this.security = security;
        this.prototype = prototype;
    }

    @Override
    public void create() {
        sayHello();
//        Gdx.input.setCatchMenuKey(true);
//        Gdx.input.setCatchBackKey(true);
//        Gdx.app.setLogLevel(Application.LOG_NONE);
        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();
        MAP_WIDTH = WORLD_WIDTH / TILE_HEIGHT;
        MAP_HEIGHT = WORLD_HEIGHT / TILE_HEIGHT;
        MAX_ENTITIES = MAP_WIDTH * MAP_HEIGHT;//Maximum entities on the map
        resources = new Resources();
        Resources.game = this;
        info = new BitmapFont();
        info.setColor(1, 1, 1, 1);
        this.setScreen(new LaunchLoadingScreen(this, prototype));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void pause() {
        //PAUSE = true;
    }

    @Override
    public void resume() {
        //PAUSE = false;
    }

    @Override
    public void dispose() {
//      security.save();
        resources.dispose();
        batch.dispose();
        Gdx.app.log("INFO", "Game disposed");
        sayGoodbye();
    }

    public void createCamera() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    public void createBatch() {
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    public void createViewport() {
        float RATIO = (float) (Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
        view = new ExtendViewport(WORLD_WIDTH * RATIO, WORLD_HEIGHT, camera);
        view.apply();
    }

    /**
     * Creates FactoryInterface from Json representation of desired class.
     *
     * @param file     name of the Json file, that holds information about T class
     * @param type     desired type of class to load
     * @param <T>      class that is named in type variable.
     * @param throwExc should screen be switched to ErrorScreen in case of Exception
     * @return new object of the desired class with values from Json
     */
    public <T> T deserialize(String file, Class<T> type, boolean throwExc) {
        try {
            Json json = new Json();
            json.setTypeName(null);
            json.setUsePrototypes(false);
            json.setIgnoreUnknownFields(true);
            return json.fromJson(type, Gdx.files.internal(file));
        } catch (Exception e) {
            if (throwExc)
                this.setScreen(
                        new ErrorScreen(this, "Could not load factory: " + file, e));
        }
        return null;
    }

    public BitmapFont getInfo() {
        return info;
    }

    public <PP extends ParametersPrototype> PP getParameters() {
        return prototype.getParameters();
    }

    public Resources getResources() {
        return resources;
    }

    public Viewport getViewport() {
        return view;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public AssetManager getManager() {
        return resources.getManager();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public SecurityHandler getSecurity() {
        return security;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public float getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public GamePrototype getPrototype() {
        return prototype;
    }

    private synchronized void sayGoodbye() {
        for (String text : getParameters().consoleBye) {
            Gdx.app.log("INFO", text);
        }
    }

    private synchronized void sayHello() {
        for (String text : getParameters().consoleHello) {
            Gdx.app.log("INFO", text);
        }
    }
}
