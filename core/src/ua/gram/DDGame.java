package ua.gram;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.gram.controller.Log;
import ua.gram.controller.Resources;
import ua.gram.controller.StringProcessor;
import ua.gram.controller.security.SecurityManager;
import ua.gram.model.Player;
import ua.gram.model.Speed;
import ua.gram.model.prototype.GamePrototype;
import ua.gram.model.prototype.LevelPrototype;
import ua.gram.model.prototype.ParametersPrototype;
import ua.gram.view.screen.LaunchLoadingScreen;

/**
 * Game enter point
 * <br/>
 * TODO Make different assets for 4x3 and 16x9(16x10) screens
 * <br/>
 * TODO For 16x9(16x10) make StretchViewport
 * <br/>
 * TODO Add to JSON pressed and disabled drawable for buttons
 * <br/>
 * TODO Add copyrights
 * <br/>
 * TODO Handle Android Menu, Back click events
 * <br/>
 * FIXME Logotype
 * <br/>
 * FIXME Switch 16pt font with 20pt
 * <br/>
 * NOTE Enemies should be tough because the amount of towers is unlimited
 * <br/>
 * NOTE Skin should not contain nonexistent values
 * <br/>
 * NOTE Gdx is initialized only on create()
 * <br/>
 *
 * @author Gram <gram7gram@gmail.com>
 */
public class DDGame<P extends GamePrototype> extends Game {

    public static String FRACTION1;
    public static String FRACTION2;
    public static byte TILE_HEIGHT = 60;
    public static boolean DEBUG;
    public static boolean PAUSE = false;
    public static int DEFAULT_BUTTON_HEIGHT;
    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;
    public static int MAX_ENTITIES;
    public static int MAX_LEVELS;
    private final P prototype;
    private final ParametersPrototype parameters;
    private final Speed gameSpeed;
    private SecurityManager security;
    private Resources resources;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport view;
    private Player player;
    private BitmapFont info;

    public DDGame(SecurityManager security, P prototype) {
        this.security = security;
        this.prototype = prototype;
        this.parameters = prototype.getParameters();
        gameSpeed = new Speed();
        DEBUG = parameters.debugging;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(parameters.logLevel);
        sayHello();
        initGameValues();
        resources = new Resources(this);
        setScreen(new LaunchLoadingScreen(this, prototype));
    }

    private void initGameValues() {
        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();
        MAP_WIDTH = WORLD_WIDTH / TILE_HEIGHT;
        MAP_HEIGHT = WORLD_HEIGHT / TILE_HEIGHT;
        MAX_ENTITIES = MAP_WIDTH * MAP_HEIGHT;//Maximum entities on the map
        MAX_LEVELS = prototype.levelConfig.levels.length;
        DEFAULT_BUTTON_HEIGHT = parameters.constants.buttonHeight;
        FRACTION1 = parameters.constants.fraction1;
        FRACTION2 = parameters.constants.fraction2;
        info = new BitmapFont();
        info.setColor(1, 1, 1, 1);
    }

    @Override
    public void dispose() {
        security.save();
        resources.dispose();
        batch.dispose();
        Log.info("Game disposed");
        sayGoodbye();
    }

    public void createCamera() {
        if (camera != null) return;
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    public void createBatch() {
        if (batch != null) return;
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    public void createViewport() {
        if (view != null) return;
        float RATIO = (float) (WORLD_WIDTH / WORLD_HEIGHT);
        view = new ExtendViewport(WORLD_WIDTH * RATIO, WORLD_HEIGHT, camera);
        view.apply();
    }

    public BitmapFont getInfo() {
        return info;
    }

    public ParametersPrototype getParameters() {
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

    public AssetManager getAssetManager() {
        return resources.getManager();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public SecurityManager getSecurity() {
        return security;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Speed getSpeed() {
        return gameSpeed;
    }

    public GamePrototype getPrototype() {
        return prototype;
    }

    public LevelPrototype getLevelPrototype(int lvl) {
        LevelPrototype[] prototypes = prototype.levelConfig.levels;

        if (lvl <= 0 || prototypes.length < lvl)
            throw new IndexOutOfBoundsException("Cannot get level prototype");

        return prototypes[lvl - 1];
    }

    private synchronized void sayGoodbye() {
        for (String text : parameters.consoleBye) {
            Log.info(StringProcessor.processString(parameters, text));
        }
    }

    private synchronized void sayHello() {
        for (String text : parameters.consoleHello) {
            Log.info(StringProcessor.processString(parameters, text));
        }
    }
}
