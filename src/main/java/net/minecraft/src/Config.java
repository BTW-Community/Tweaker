package net.minecraft.src;

import java.util.*;

public class Config {
    public static final String DOUBLES_TAG = "Doubles";
    public static final String INTEGERS_TAG = "Integers";

    private static final TweakerAddon tweaker = TweakerAddon.getInstance();

    private static final LinkedHashMap<String, String> configComments = getDefaultComments();
    private static Map<String, Double> configDoubles = getDefaultDoubles();
    private static Map<String, Integer> configIntegers = getDefaultIntegers();

    public static void loadConfig(Map<String, String> configProperties) {
        configProperties.forEach(Config::putValue);
    }

    public static void resetConfig() {
        configDoubles = getDefaultDoubles();
        configIntegers = getDefaultIntegers();
        loadConfig(tweaker.loadConfigProperties());
    }

    private static void verifyConfig() {
        configDoubles.forEach((k, v) ->
                assertTrue(v >= 0, new NumberInvalidException("Value cannot be less than 0")));
        configIntegers.forEach((k, v) ->
                assertTrue(v >= 0, new NumberInvalidException("Value cannot be less than 0")));

        double maxSpawnRadius = Config.getDouble("maxSpawnRadius");
        double minSpawnRadius = Config.getDouble("minSpawnRadius");
        assertTrue(maxSpawnRadius >= minSpawnRadius,
                new NumberInvalidException("maxSpawnRadius must not be less than minSpawnRadius"));

        assertTrue(configIntegers.get("quickSpawnHealth") > 0,
                new NumberInvalidException("quickSpawnHealth must be greater than 0"));
    }

    private static void assertTrue(boolean expression, RuntimeException exception) {
        if (!expression) {
            throw exception;
        }
    }

    private static List<Map<String, ?>> getConfigs() {
        List<Map<String, ?>> configs = new ArrayList<>();

        configs.add(configDoubles);
        configs.add(configIntegers);

        return configs;
    }

    // === Default Configs ===

    private static LinkedHashMap<String, String> getDefaultComments() {
        LinkedHashMap<String, String> config = new LinkedHashMap<>();

        // Spawn Radius
        config.put("maxSpawnRadius", "The maximum radius the player will respawn from world spawn.");
        config.put("minSpawnRadius", "The minimum radius the player will respawn from world spawn.");

        // Quick Spawn
        config.put("quickSpawnRadius", "The radius at which the player will respawn after dying recently.");
        config.put("quickSpawnHealth", "The player's health after a quick spawn.");
        config.put("quickSpawnHungerMin", "The minimum amount of hunger after a quick spawn.");
        config.put("quickSpawnHungerLoss", "The amount of hunger a player loses on each quick spawn.");

        // Spawn Radius Multipliers
        config.put("largeBiomeMultiplier", "Multiplier used for `maxSpawnRadius` when playing with Large Biomes.");
        config.put("endMultiplier", "Multiplier used for 'maxSpawnRadius' after activating an End portal.");
        config.put("witherMultiplier", "Multiplier used for 'maxSpawnRadius' after summoning a Wither.");
        config.put("netherMultiplier", "Multiplier used for 'maxSpawnRadius' after activating a Nether portal.");

        // Structure Generation
        config.put("abandonedVillageRadius", "Villages within this radius from world spawn will be abandoned.");
        config.put("partiallyAbandonedVillageRadius", "Villages within this radius from world spawn will be partially abandoned.");
        config.put("lootedTempleRadius", "Temples within this radius from world spawn will be looted.");

        return config;
    }

    private static Map<String, Double> getDefaultDoubles() {
        Map<String, Double> config = new HashMap<>();

        // Spawn Radius
        config.put("maxSpawnRadius", 2000d);
        config.put("minSpawnRadius", 1000d);

        // Quick Spawn
        config.put("quickSpawnRadius", 100d);

        // Spawn Radius Multipliers
        config.put("largeBiomeMultiplier", 4d);
        config.put("endMultiplier", 2.5d);
        config.put("witherMultiplier", 2d);
        config.put("netherMultiplier", 1.5d);

        // Structure Generation
        config.put("abandonedVillageRadius", 2250d);
        config.put("partiallyAbandonedVillageRadius", 3000d);
        config.put("lootedTempleRadius", 2250d);

        return config;
    }

    private static Map<String, Integer> getDefaultIntegers() {
        Map<String, Integer> config = new HashMap<>();

        // Quick Spawn
        config.put("quickSpawnHealth", 10);
        config.put("quickSpawnHungerMin", 24);
        config.put("quickSpawnHungerLoss", 6);

        return config;
    }

    // === Getters ===

    public static Set<String> getKeys() {
        return configComments.keySet();
    }

    public static String getValueString(String key) {
        for (Map<String, ?> config : getConfigs()) {
            if (config.containsKey(key)) {
                return config.get(key).toString();
            }
        }
        throw new WrongUsageException(tweaker.prefix + " <option> [value]", key);
    }
    
    public static String getComment(String key) {
        return configComments.get(key);
    }

    public static Double getDouble(String key) {
        return configDoubles.get(key);
    }

    public static Integer getInteger(String key) {
        return configIntegers.get(key);
    }

    // === Putters ===

    public static void putValue(String key, String value) {
        if (configDoubles.containsKey(key)) {
            putType(configDoubles, key, parseDouble(value));
        } else if (configIntegers.containsKey(key)) {
            putType(configIntegers, key, parseInteger(value));
        } else {
            throw new CommandNotFoundException("Could not find command " + key);
        }
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

    // === Save to NBT ===

    public static NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound(tweaker.getName());
        tag.setCompoundTag(DOUBLES_TAG, toNBTDoubles());
        tag.setCompoundTag(INTEGERS_TAG, toNBTIntegers());
        return tag;
    }

    private static NBTTagCompound toNBTDoubles() {
        NBTTagCompound tag = new NBTTagCompound();
        configDoubles.forEach(tag::setDouble);
        return tag;
    }

    private static NBTTagCompound toNBTIntegers() {
        NBTTagCompound tag = new NBTTagCompound();
        configIntegers.forEach(tag::setInteger);
        return tag;
    }

    // === Load from NBT ===

    public static void loadNBT(NBTTagCompound tweakerTag) {
        if (tweakerTag.hasKey(DOUBLES_TAG)) {
            loadNBTDoubles(tweakerTag.getCompoundTag(DOUBLES_TAG));
        }
        if (tweakerTag.hasKey(INTEGERS_TAG)) {
            loadNBTIntegers(tweakerTag.getCompoundTag(INTEGERS_TAG));
        }
        verifyConfig();
    }

    private static void loadNBTDoubles(NBTTagCompound doublesTag) {
        configDoubles.keySet().stream()
                .filter(doublesTag::hasKey)
                .forEach(k -> configDoubles.put(k, doublesTag.getDouble(k)));
    }

    private static void loadNBTIntegers(NBTTagCompound integersTag) {
        configIntegers.keySet().stream()
                .filter(integersTag::hasKey)
                .forEach(k -> configIntegers.put(k, integersTag.getInteger(k)));
    }

    // === Parsing ===

    private static Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException var3) {
            throw new NumberInvalidException("commands.generic.double.invalid", value);
        }
    }

    private static Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException var3) {
            throw new NumberInvalidException("commands.generic.num.invalid", value);
        }
    }
}
