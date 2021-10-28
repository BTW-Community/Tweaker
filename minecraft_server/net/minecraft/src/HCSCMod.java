package net.minecraft.src;

import java.util.Map;

public class HCSCMod extends FCAddOn {
	public static int maxRadius = 2000;
	public static int minRadius = 1000;
	
	public static float netherMultiplier = 1.5f;
	public static float witherMultiplier = 2.0f;
	public static float endMultiplier = 2.5f;
	public static float largeMultiplier = 4.0f;
	
	public static int maxSpawnTime = 10800;
	public static int quickRespawnRadius = 100;
	public static int quickRespawnHealth = 10;
	public static int quickRespawnMinFood = 24;
	public static int quickRespawnFoodDecrement = 6;
	public static int hcSoulMating = 1;
	public static int deathDespawnTime = 24000;
	
	public static int abandonedVillageRadius = 2250;
	public static int partiallyAbandonedRadius = 3000;
	public static int lootedTempleRadius = 2250;
	
	private static HCSCMod instance;

	public HCSCMod() {
		super("HC Spawn Config", "1.2.0", "HCSC");
		this.shouldVersionCheck = false;
	}
	
	public static HCSCMod getInstance() {
		if (instance == null) {
			instance = new HCSCMod();
		}
		return instance;
	}
	
	@Override
	public void PreInitialize() {
		registerProperties();
	}
	
	@Override
	public void Initialize() {
		FCAddOnHandler.LogMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
		FCAddOnHandler.LogMessage(this.getName() + " Initialized");
	}
	
	@Override
	public void handleConfigProperties(Map<String, String> propertyValues) {
		maxRadius = Integer.parseInt(propertyValues.get("maxRadius"));
		minRadius = Integer.parseInt(propertyValues.get("minRadius"));
		
		netherMultiplier = Float.parseFloat(propertyValues.get("netherMultiplier"));
		witherMultiplier = Float.parseFloat(propertyValues.get("witherMultiplier"));
		endMultiplier = Float.parseFloat(propertyValues.get("endMultiplier"));
		largeMultiplier = Float.parseFloat(propertyValues.get("largeMultiplier"));
		
		maxSpawnTime = Integer.parseInt(propertyValues.get("maxSpawnTime"));
		quickRespawnRadius = Integer.parseInt(propertyValues.get("quickRespawnRadius"));
		quickRespawnHealth = Integer.parseInt(propertyValues.get("quickRespawnHealth"));
		quickRespawnMinFood = Integer.parseInt(propertyValues.get("quickRespawnMinFood"));
		quickRespawnFoodDecrement = Integer.parseInt(propertyValues.get("quickRespawnFoodDecrement"));
		hcSoulMating = Integer.parseInt(propertyValues.get("hcSoulMating"));
		deathDespawnTime = Integer.parseInt(propertyValues.get("deathDespawnTime"));
		
		abandonedVillageRadius = Integer.parseInt(propertyValues.get("abandonedVillageRadius"));
		partiallyAbandonedRadius = Integer.parseInt(propertyValues.get("partiallyAbandonedRadius"));
		lootedTempleRadius = Integer.parseInt(propertyValues.get("lootedTempleRadius"));
	}

	private void registerProperties() {
		// Respawn Radius
		registerProperty("maxRadius", "2000", "**Respawn Radius**\n\n"
				+ "# Maximum radius the player will respawn from spawn.");
		registerProperty("minRadius", "1000", "Minimum radius the player will respawn from spawn.");
		
		// Respawn Multipliers
		registerProperty("netherMultiplier", "1.5", "**Respawn Multipliers**\n\n"
				+ "# Multiplier after activating a Nether portal.");
		registerProperty("witherMultiplier", "2.0", "Multiplier after summoning a Wither.");
		registerProperty("endMultiplier", "2.5", "Multiplier after activating an End portal.");
		registerProperty("largeMultiplier", "4.0", "Multiplier for the Large Biomes world type.");
		
		// Respawn Behaviour
		registerProperty("maxSpawnTime", "10800", "**Respawn Behaviour**\n\n"
				+ "# The maximum amount of time in ticks between respawns "
				+ "for special respawn behaviour. Players dying within this time will respawn according "
				+ "to quickRespawnRadius. If in multiplayer, players who die in this interval will respawn "
				+ "together, as long as progression hasn't passed hcSoulMating.");
		registerProperty("quickRespawnRadius", "100", "The maximum radius from the previous "
				+ "respawn the player will respawn at if they have died within maxSpawnTime.");
		registerProperty("quickRespawnHealth", "10", "The player's health after a quick respawn (2 = one heart).");
		registerProperty("quickRespawnMinFood", "24", "The player's minimum food after a quick respawn (6 = one shank).");
		registerProperty("quickRespawnFoodDecrement", "6", "The reduction per respawn for the player's "
				+ "food after a quick respawn (6 = one shank).");
		registerProperty("hcSoulMating", "1", "The point in progression at which Hardcore Soul Mating stops occuring (Overridden by \"AlwaysSpawnTogether\" in BTW.properties).\n"
				+ "# 0 = On Spawn, 1 = Nether, 2 = Wither, 3 = End.");
		registerProperty("deathDespawnTime", "24000", "Ticks until a player's items dropped on death despawn.");
		
		// Structure Generation
		registerProperty("abandonedVillageRadius", "2250", "**Structure Generation**\n\n"
				+ "# Villages within this radius from spawn will be abandoned.");
		registerProperty("partiallyAbandonedRadius", "3000", "Villages within this radius from spawn will be partially abandoned.");
		registerProperty("lootedTempleRadius", "2250", "Temples within this radius from spawn will be looted.");
	}

}
