package net.minecraft.src;

import java.util.Map;

public class TweakerAddon extends FCAddOn {
    private static TweakerAddon instance;
    private static Map<String, String> config;

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
        config = loadConfigProperties();
    }

    private void registerProperties() {
        // Spawn Radius
        registerProperty("maxSpawnRadius", "16");  // 2000
        registerProperty("minSpawnRadius", "0");  // 1000
        registerProperty("quickSpawnRadius", "100");
        registerProperty("abandonedVillageRadius", "2250");
        registerProperty("partiallyAbandonedVillageRadius", "3000");
        registerProperty("lootedTempleRadius", "2250");

        // Spawn Radius Multipliers
        registerProperty("largeBiomeMultiplier", "4");
        registerProperty("endMultiplier", "2.5");
        registerProperty("witherMultiplier", "2");
        registerProperty("netherMultiplier", "1.5");
    }

    public static double getDouble(String key) {
        return Double.parseDouble(config.get(key));
    }
}
