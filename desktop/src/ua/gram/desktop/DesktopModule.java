package ua.gram.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.net.NetworkInterface;
import java.util.Enumeration;

import ua.gram.AbstractModule;
import ua.gram.DDGame;
import ua.gram.controller.Log;
import ua.gram.controller.security.SecurityHandler;
import ua.gram.desktop.prototype.DesktopGamePrototype;
import ua.gram.desktop.prototype.DesktopParametersPrototype;
import ua.gram.model.prototype.TexturePackerPrototype;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DesktopModule extends AbstractModule<DesktopGamePrototype> {

    private final DesktopGamePrototype prototype;
    private final LwjglApplicationConfiguration config;

    public DesktopModule() {
        prototype = loadPrototype(DesktopGamePrototype.class, "data/parameters.json");
        initParameters();
        config = initConfig();
    }

    @Override
    public void initModule() {
        new LwjglApplication(new DDGame<>(new SecurityHandler<>(prototype), prototype), config);
    }

    private LwjglApplicationConfiguration initConfig() {
        if (prototype == null) throw new GdxRuntimeException("Game prototype was not loaded");

        LwjglApplicationConfiguration graphics = prototype.config.desktop;
        graphics.addIcon(prototype.config.logo128, Files.FileType.Internal);
        graphics.addIcon(prototype.config.logo32, Files.FileType.Internal);
        graphics.addIcon(prototype.config.logo16, Files.FileType.Internal);

        if (prototype.config.reloadTextures) {
            TexturePackerPrototype packer = prototype.config.texturePacker;
            TexturePacker.process(packer.config, packer.from, packer.to, packer.name);
        }

        return graphics;
    }

    private void initParameters() {
        if (prototype == null) throw new GdxRuntimeException("Game prototype was not loaded");

        DesktopParametersPrototype parameters = prototype.parameters;
        parameters.id = getMAC();
        parameters.gameModule = "Desktop";
        parameters.osName = System.getProperty("os.name");
        parameters.osVersion = System.getProperty("os.version");
        parameters.configPath = "";
    }

    private String getMAC() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                byte[] mac = interfaces.nextElement().getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            Log.warn("Could not get MAC-address. Used 'undefined'");
        }
        return "undefined";
    }
}