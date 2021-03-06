package ua.gram.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.DDGame;
import ua.gram.Module;
import ua.gram.controller.security.SecurityManager;
import ua.gram.desktop.prototype.DesktopGamePrototype;
import ua.gram.model.prototype.ParametersPrototype;
import ua.gram.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DesktopModule implements Module {

    public static final String PARAMETERS = "data/parameters.json";
    private final SecurityManager<DesktopGamePrototype> securityManager;
    private final String rootDirectory;
    private DesktopGamePrototype prototype;
    private ApplicationListener app;

    public DesktopModule() {
        this("", PARAMETERS);
    }

    public DesktopModule(String rootDirectory, String parametersPath) {
        securityManager = new SecurityManager<DesktopGamePrototype>();
        securityManager.setFileHandle(new LwjglFiles());
        this.rootDirectory = rootDirectory;
        try {
            prototype = securityManager.load(DesktopGamePrototype.class, rootDirectory + parametersPath);

            securityManager.setPrototype(prototype);

            securityManager.cleanup();

            initParameters();
            initConfig();
        } catch (GdxRuntimeException e) {
            Log.exc("Could not load prototype", e);
            System.exit(1);
        }
    }

    @Override
    public ApplicationListener getApp() {
        if (app == null) throw new GdxRuntimeException("Module is not initialized yet");
        return app;
    }

    @Override
    public void initModule(String env) {
        if (env != null) {
            prototype.parameters.env = env;
        }
        app = new DDGame<DesktopGamePrototype>(securityManager, prototype, rootDirectory);
    }

    @Override
    public void initConfig() {
        if (prototype == null || prototype.gameConfig == null)
            throw new GdxRuntimeException("Game prototype was not loaded");

        LwjglApplicationConfiguration graphics = prototype.gameConfig.desktop;
        graphics.addIcon(prototype.gameConfig.logo128, Files.FileType.Internal);
        graphics.addIcon(prototype.gameConfig.logo32, Files.FileType.Internal);
        graphics.addIcon(prototype.gameConfig.logo16, Files.FileType.Internal);
    }

    @Override
    public String getName() {
        return "desktop";
    }

    @Override
    public void initParameters() {
        if (prototype == null) throw new GdxRuntimeException("Game prototype was not loaded");

        ParametersPrototype parameters = prototype.getParameters();
        parameters.id = null;
        parameters.gameModule = getName();
        parameters.osName = System.getProperty("os.name") + System.getProperty("os.version");
    }

    public LwjglApplicationConfiguration getConfig() {
        return prototype.gameConfig.desktop;
    }

}
