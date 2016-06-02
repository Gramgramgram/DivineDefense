package ua.gram.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.DDGame;

/**
 *
 * @author Gram <gram7gram@gmail.com>
 */
public class Resources implements Disposable {

    public static final String BACKGROUND_TEXTURE = "data/images/misc/background.jpg";
    private final DDGame game;
    private final AssetManager manager;
    private final Skin skin;

    public Resources(DDGame game) {
        this.game = game;
        manager = new AssetManager();
        skin = loadSkin(game.getParameters().skin);
        loadTexture(BACKGROUND_TEXTURE);
        manager.finishLoading();
    }

    /**
     * Loads the JSON and corresponding Atlas files to AssetManager.
     * Atlas should be named same as the Json file: style.json and style.atlas.
     * Names are accessible through static Strings in Resources class.
     * Will display ErrorScreen it was not able to load skin.
     *
     * @param file - name of the Json
     * @return - new Skin, buy with Json and Atlas, that matches Json file without extension
     */
    public Skin loadSkin(String file) {
        String atlas = game.getParameters().atlas;
        if (atlas == null) {
            String name = file.substring(0, file.lastIndexOf("."));
            atlas = name + ".atlas";
            Log.warn("Atlas file not specified. Trying: " + atlas);
        }
        manager.load(file, Skin.class, new SkinLoader.SkinParameter(atlas));
        manager.finishLoading();

        return manager.get(file, Skin.class);
    }

    /**
     * Load tiled map for specified levelConfig by file.
     * Will display ErrorScreen it was not able to load map.
     */
    public void loadMap(String file) {
        try {
            manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
            manager.load(file, TiledMap.class);
        } catch (GdxRuntimeException e) {
            Log.exc("Could not load map for level: " + file, e);
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
            Log.exc("Not loaded: " + file, e);
        }
    }

    public AssetManager getManager() {
        return manager;
    }

    public Skin getSkin() {
        return skin;
    }

    public TiledMap getMap(String map) {
        return manager.get(map, TiledMap.class);
    }

    public Texture getRegisteredTexture(String file) {
        return manager.get(file, Texture.class);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    public void loadAtlas(String resource) {
        try {
            manager.load(resource + ".atlas", TextureAtlas.class);
        } catch (GdxRuntimeException e) {
            Log.exc("Not loaded: " + resource, e);
        }
    }

    public TextureAtlas getAtlas(String name) {
        return manager.get(name + ".atlas", TextureAtlas.class);
    }
}
