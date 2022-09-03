package issame.tweaker;

import net.minecraft.src.FCAddOn;
import net.minecraft.src.FCAddOnHandler;
import net.minecraft.src.FCAddOnUtilsWorldData;
import net.minecraft.src.ServerCommandManager;

public class TweakerAddon extends FCAddOn {
    private static TweakerAddon instance;

    private TweakerAddon() {
        super("Tweaker", "1.3.0", "tweaker");
    }

    public static TweakerAddon getInstance() {
        if (instance == null)
            instance = new TweakerAddon();
        return instance;
    }

    @Override
    public void PreInitialize() {
        registerProperties();
    }

    @Override
    public void Initialize() {
        FCAddOnHandler.LogMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        Config.loadConfig(loadConfigProperties());
    }

    @Override
    public FCAddOnUtilsWorldData createWorldData() {
        return new TweakerUtilsWorldData();
    }

    public String getPrefix() {
        return prefix;
    }

    private void registerProperties() {
        Config.getKeys().forEach(k -> registerProperty(k, Config.getValueString(k), Config.getComment(k)));
    }
}
