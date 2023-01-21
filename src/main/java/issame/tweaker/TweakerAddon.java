package issame.tweaker;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.world.util.WorldData;

public class TweakerAddon extends BTWAddon {
    private static TweakerAddon instance;

    private TweakerAddon() {
        super("Tweaker", "1.4.0", "tweaker");
    }

    public static TweakerAddon getInstance() {
        if (instance == null)
            instance = new TweakerAddon();
        return instance;
    }

    @Override
    public void preInitialize() {
        registerProperties();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        Config.loadConfig(loadConfigProperties());
    }

    @Override
    public WorldData createWorldData() {
        return new TweakerWorldData();
    }

    public String getPrefix() {
        return prefix;
    }

    private void registerProperties() {
        Config.getKeys().forEach(k -> registerProperty(k, Config.getValueString(k), Config.getComment(k)));
    }
}
