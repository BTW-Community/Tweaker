package net.minecraft.src;

import java.util.*;

public class Config {
    private static final LinkedHashMap<String, String> configComments = getConfigComments();
    private static Map<String, Double> configDouble = getDefaultDouble();

    public static void loadConfig(Map<String, String> configProperties) {
        configProperties.forEach((key, value) -> {
            if (configDouble.containsKey(key)) {
                configDouble.put(key, Double.valueOf(value));
            }
        });

        verifyConfig();
    }

    public static void resetConfig() {
        configDouble = getDefaultDouble();
        loadConfig(TweakerAddon.getInstance().loadConfigProperties());
    }

    private static void verifyConfig() {
        configDouble.forEach((k, v) -> {
            assertTrue(v >= 0, new NumberInvalidException("Value cannot be less than 0"));
        });
        double maxSpawnRadius = Config.getDouble("maxSpawnRadius");
        double minSpawnRadius = Config.getDouble("minSpawnRadius");
        assertTrue(maxSpawnRadius >= minSpawnRadius,
                new NumberInvalidException("maxSpawnRadius must not be less than minSpawnRadius"));
    }

    private static void assertTrue(boolean expression, CommandException exception) {
        if (!expression) {
            throw exception;
        }
    }

    public static Set<String> getKeys() {
        return configComments.keySet();
    }

    public static boolean hasKey(String key) {
        return configComments.containsKey(key);
    }

    public static String getValueString(String key) {
        if (configDouble.containsKey(key)) {
            return configDouble.get(key).toString();
        }
        return null;
    }

    public static void putValue(String key, String value) {
        if (configDouble.containsKey(key)) {
            double num = CommandBase.parseDouble(null, value);
            putType(configDouble, key, num);
        }
        throw new CommandNotFoundException();
    }

    private static <T> void putType(Map<String, T> configType, String key, T value) {
        T prevValue = configType.put(key, value);
        try {
            verifyConfig();
        } catch (CommandException e) {
            configType.put(key, prevValue);
            throw e;
        }
    }

    private static LinkedHashMap<String, String> getConfigComments() {
        LinkedHashMap<String, String> config = new LinkedHashMap<>();

        // Spawn Radius
        config.put("maxSpawnRadius", "The maximum radius the player will respawn from world spawn.");
        config.put("minSpawnRadius", "The minimum radius the player will respawn from world spawn");
        config.put("quickSpawnRadius", "The radius at which the player will respawn after dying recently.");
        config.put("abandonedVillageRadius", "Villages within this radius from world spawn will be abandoned.");
        config.put("partiallyAbandonedVillageRadius", "Villages within this radius from world spawn will be partially abandoned.");
        config.put("lootedTempleRadius", "Temples within this radius from world spawn will be looted.");

        // Spawn Radius Multipliers
        config.put("largeBiomeMultiplier", "Multiplier used for `maxSpawnRadius` when playing with Large Biomes.");
        config.put("endMultiplier", "Multiplier used for 'maxSpawnRadius' after activating an End portal.");
        config.put("witherMultiplier", "Multiplier used for 'maxSpawnRadius' after summoning a Wither.");
        config.put("netherMultiplier", "Multiplier used for 'maxSpawnRadius' after activating a Nether portal.");

        return config;
    }
    
    public static String getComment(String key) {
        return configComments.get(key);
    }

    private static Map<String, Double> getDefaultDouble() {
        Map<String, Double> config = new HashMap<>();

        // Spawn Radius
        config.put("maxSpawnRadius", 2000d);
        config.put("minSpawnRadius", 1000d);
        config.put("quickSpawnRadius", 100d);
        config.put("abandonedVillageRadius", 2250d);
        config.put("partiallyAbandonedVillageRadius", 3000d);
        config.put("lootedTempleRadius", 2250d);

        // Spawn Radius Multipliers
        config.put("largeBiomeMultiplier", 4d);
        config.put("endMultiplier", 2.5d);
        config.put("witherMultiplier", 2d);
        config.put("netherMultiplier", 1.5d);

        return config;
    }

    public static Double getDouble(String key) {
        return configDouble.get(key);
    }

    public static NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound("Tweaker");
        tag.setCompoundTag("Double", toNBTDouble());
        return tag;
    }

    public static void loadNBT(NBTTagCompound tweakerTag) {
        if (tweakerTag.hasKey("Double")) {
            loadNBTDouble(tweakerTag.getCompoundTag("Double"));
        }
    }

    private static NBTTagCompound toNBTDouble() {
        NBTTagCompound tag = new NBTTagCompound();
        configDouble.forEach(tag::setDouble);
        return tag;
    }

    private static void loadNBTDouble(NBTTagCompound doubleTag) {
        configDouble.keySet().stream()
                .filter(doubleTag::hasKey)
                .forEach(k -> configDouble.put(k, doubleTag.getDouble(k)));
    }
}
