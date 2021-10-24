package net.minecraft.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class HCSCMod extends FCAddOn {
	private static Properties config;

	@Override
	public void Initialize() {
		FCAddOnHandler.LogMessage("HCSpawn Config Initializing...");
		loadConfig();
		FCAddOnHandler.LogMessage("HCSpawn Config Initialized!");
	}
	
	private static void loadConfig() {
    	config = new Properties();
    	
    	try {
			FileReader reader = new FileReader(configFile());
			config.load(reader);
			reader.close();
			System.out.println("HCSpawn Config file loaded at: " + configFile().getPath());
		} catch (FileNotFoundException e) {
			System.out.println("HCSpawn Config file not found. Creating...");
			createDefaultConfig();
		} catch (IOException e) {
			System.out.println("Cannot load HCSpawn Config file: " + e.getMessage());
		}
    }

	private static void createDefaultConfig() {
    	try {
    		configFile().getParentFile().mkdirs();
			configFile().createNewFile();
			FileWriter writer = new FileWriter(configFile());
			writer.write(configText());
			writer.close();
			loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getConfigString(String key, String defaultValue) {
		String value = config.getProperty(key);
		if (value == null) {
			value = defaultValue;
			System.out.println("Cannot find key: " + key);
			System.out.println("Using default value " + defaultValue);
		}
		return value;
	}
	
	public static double getConfigDouble(String key, String defaultValue) {
		return Double.parseDouble(getConfigString(key, defaultValue));
	}
	
	public static int getConfigInt(String key, String defaultValue) {
		return (int) getConfigDouble(key, defaultValue);
	}
	
	public static long getConfigLong(String key, String defaultValue) {
		return (long) getConfigDouble(key, defaultValue);
	}

	private static File configFile() {
		return new File(new File(".", "config"), "hcspawn.cfg");
	}
	
	private static String configText() {
		return
			  "### HCSpawn Config ###\n"
			+ "\n"
			+ "\n"
			+ "## Respawn Radius ##\n"
			+ "\n"
			+ "# The maximum radius the player will respawn from spawn. (Default: 2000.0)\n"
			+ "maxRespawnRadius=2000.0\n"
			+ "\n"
			+ "# The minimum radius the player will respawn from spawn. (Default: 1000.0)\n"
			+ "minRespawnRadius=1000.0\n"
			+ "\n"
			+ "\n"
			+ "## Respawn Multipliers ##\n"
			+ "\n"
			+ "# Multiplier after activating a Nether portal. (Default: 1.5)\n"
			+ "netherMultiplier=1.5\n"
			+ "\n"
			+ "# Multiplier after summoning a Wither. (Default: 2.0)\n"
			+ "witherMultiplier=2.0\n"
			+ "\n"
			+ "# Multiplier after activating an End portal. (Default: 4.0)\n"
			+ "endMultiplier=4.0\n"
			+ "\n"
			+ "# Multiplier for the Large Biomes world type. (Default: 4.0)\n"
			+ "largeMultiplier=4.0\n"
			+ "\n"
			+ "\n"
			+ "## Respawn Behaviour ##\n"
			+ "\n"
			+ "# The maximum amount of time in ticks between respawns for special respawn behaviour.\n"
			+ "# Players dying within this time will respawn according to quickRespawnRadius.\n"
			+ "# If in multiplayer, players who die in this interval will respawn together,\n"
			+ "# as long as progression hasn't passed hcSoulMatingProgression. (Default: 24000)\n"
			+ "maxSpawnTime=24000\n"
			+ "\n"
			+ "# The maximum radius from the previous respawn the player will\n"
			+ "# respawn at if they have died within maxSpawnTime. (Default: 100.0)\n"
			+ "quickRespawnRadius=100.0\n"
			+ "\n"
			+ "# The player's health after a quick respawn,\n"
			+ "# 2 = one heart. (Default: 10)\n"
			+ "quickRespawnHealth=10\n"
			+ "\n"
			+ "# The player's minimum food after a quick respawn,\n"
			+ "# 6 = one shank. (Default: 12)\n"
			+ "quickRespawnMinFood=12\n"
			+ "\n"
			+ "# The reduction per respawn for the player's food after a quick respawn,\n"
			+ "# 6 = one shank. (Default: 6)\n"
			+ "quickRespawnFoodDecrement=6\n"
			+ "\n"
			+ "# The point in progression at which Hardcore Soul Mating stops occuring.\n"
			+ "# 0 = On Spawn, 1 = Nether, 2 = Wither, 3 = End. (Default: 1)\n"
			+ "hcSoulMatingProgression=1\n"
			+ "\n"
			+ "# Ticks until a player's items dropped on death despawn. (Default: 24000)\n"
			+ "deathDespawnTime=24000\n"
			+ "\n"
			+ "\n"
			+ "## Structure Generation ##\n"
			+ "\n"
			+ "# Villages within this radius from spawn will be abandoned. (Default: 2250.0)\n"
			+ "abandonedVillageRadius=2250.0\n"
			+ "\n"
			+ "# Villages within this radius from spawn will be partially abandoned. (Default: 3000.0)\n"
			+ "partiallyAbandonedVillageRadius=3000.0\n"
			+ "\n"
			+ "# Temples within this radius from spawn will be looted. (Default: 2250.0)\n"
			+ "lootedTempleRadius=2250.0";
	}
	
	public static HCSCMod noticeMeSenpai() {
		return new HCSCMod();
	}
}
