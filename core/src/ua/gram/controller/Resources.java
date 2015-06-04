package ua.gram.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import ua.gram.DDGame;
import ua.gram.view.screen.ErrorScreen;

/**
 * TODO Merge resources in one big file.
 * NOTE Sprites should not be present in TextureAtlas: lock, weapon etc.
 *
 * @author Gram <gram7gram@gmail.com>
 */
public class Resources implements Disposable {

    public static final String SKIN_FILE = "data/skin/style.json";
    public static final String BACKGROUND_TEXTURE = "data/images/misc/background.jpg";
    public static final String WEAPON_START_BACK = "data/images/misc/start_back.png";
    public static final String WEAPON_START_OVER = "data/images/misc/start_over.png";
    public static final String WEAPON_MIDDLE_BACK = "data/images/misc/middle_back.png";
    public static final String WEAPON_MIDDLE_OVER = "data/images/misc/middle_over.png";
    public static final String WEAPON_END_BACK = "data/images/misc/end_back.png";
    public static final String WEAPON_END_OVER = "data/images/misc/end_over.png";
    public static final String RANGE_TEXTURE = "data/images/misc/enemy_range.png";
    public static final String LOCK_TEXTURE = "data/images/misc/fraction_lock.png";
    public static final String AIM_TEXTURE = "data/images/misc/enemy_aim.png";
    private final DDGame game;
    private final AssetManager manager;
    private Skin skin;

    public Resources(DDGame game) {
        this.game = game;
        manager = new AssetManager();
        try {
            skin = loadSkin(SKIN_FILE);
        } catch (GdxRuntimeException e) {
            Gdx.app.error("ERROR", "Could not load skin!" + "\nDue to: " + e);
            System.exit(1);//Gdx.app.exit() does not do it's job!...
        }
    }

    public void loadBasicFiles() {
        loadFont("ActionManShaded", 32, "black");//loading label
        loadFont("SfArchery", 32, "white"); //button labels
        loadFont("SfArchery", 16, "black"); //button labels
        loadFont("FffTusj", 64, "white");//Big labels
    }

    /**
     * Loads the JSON and corresponding Atlas files to AssetManager.
     * Atlas should be named same as the Json file: style.json and style.atlas.
     * Names are accessible through static Strings in Resources class.
     * Will display ErrorScreen it was not able to load skin.
     *
     * @param file - name of the Json
     * @return - new Skin, build with Json and Atlas, that matches Json file without extension
     */
    public Skin loadSkin(String file) throws GdxRuntimeException {
        String atlas = file.substring(0, file.lastIndexOf(".")) + ".atlas";
        manager.load(file, Skin.class, new SkinLoader.SkinParameter(atlas));
        manager.finishLoading();
        return manager.get(file, Skin.class);
    }

    /**
     * Loads fonts with specified name in lowercase: fontName+size+color.fnt.
     * Will display ErrorScreen it was not able to load font.
     *
     * @param fontName general font name
     * @param size     desired font size
     * @param color    desired font color
     */
    public void loadFont(String fontName, int size, String color) {
        try {
            manager.load("data/skin/fonts/" + fontName + size + color + ".fnt", BitmapFont.class);
        } catch (GdxRuntimeException e) {
            if (game.getCamera() == null) createDisplayComponents();
            game.setScreen(new ErrorScreen(game, "Could not load font: " + fontName, e));
        }
    }

    /**
     * Load tiled map for specified level.
     * Will display ErrorScreen it was not able to load map.
     *
     * @param level level number [1..n]
     */
    public void loadMap(int level) {
        try {
            manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
            manager.load("data/levels/maps/level" + level + "@60.tmx", TiledMap.class);
        } catch (GdxRuntimeException e) {
            if (game.getCamera() == null) createDisplayComponents();
            game.setScreen(new ErrorScreen(game, "Could not load map for level: " + level, e));
        }
    }

    /**
     * Load TextureAtlas for specified name.
     * Names are accessible through static Strings in Resources class.
     * Will display ErrorScreen it was not able to load map.
     *
     * @param file atlas location
     */
    public void loadAtlas(String file) {
        try {
            manager.load(file, TextureAtlas.class);
        } catch (GdxRuntimeException e) {
            if (game.getCamera() == null) createDisplayComponents();
            game.setScreen(new ErrorScreen(game, "Not loaded: " + file, e));
        }
    }

    /**
     * Load Texture for specified filename.
     * Names are accessible through static Strings in Resources class.
     * Will display ErrorScreen it was not able to load map.
     *
     * @param file texture location
     */
    public void loadTexture(String file) {
        try {
            manager.load(file, Texture.class);
        } catch (GdxRuntimeException e) {
            if (game.getCamera() == null) createDisplayComponents();
            game.setScreen(new ErrorScreen(game, "Not loaded: " + file, e));
        }
    }

    public AssetManager getManager() {
        return manager;
    }

    public Skin getSkin() {
        return skin;
    }

    public TiledMap getMap(int level) {
        return manager.get("data/levels/maps/level" + level + "@60.tmx", TiledMap.class);
    }

    public Texture getTexture(String file) {
        return manager.get(file, Texture.class);
    }

    public Texture getAtlasRegion(String region) {
        return skin.getRegion(region).getTexture();
    }

    /**
     * If there was an error in loading the resources, and DDGame did not create
     * nor Camera, nor Viewport, nor Batch - this method will create them for you,
     * so that you are able to display ErrorScreen and did not terminate the application.
     */
    private void createDisplayComponents() {
        game.createCamera();
        game.createViewport();
        game.createBatch();
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}