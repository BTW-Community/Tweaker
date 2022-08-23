package net.minecraft.src;

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
        registerCommands();
        Config.loadConfig(loadConfigProperties());
    }

    @Override
    public FCAddOnUtilsWorldData createWorldData() {
        return new TweakerUtilsWorldData();
    }

    private void registerProperties() {
        Config.getKeys().forEach(k -> {
            if (Config.getDouble(k) != null) {
                registerProperty(k, String.valueOf(Config.getDouble(k)), Config.getComment(k));
            }
        });
    }

    private void registerCommands() {
        ServerCommandManager.registerAddonCommand(new TweakerCommand());
    }
}
