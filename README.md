# HCSpawnConfig

Want to be able to change HC Spawn mechanics? This addon is for you!

[Download](https://github.com/BTW-Community/HCSpawnConfig/releases/latest)

HCSpawn Config currently lets you change the HC Spawn mechanics, as well as abandoned village and looted temple generation conditions.
Simply run the game once with this addon, which should generate a file at `./config/HCSC.properties`, and modify the values.

**This will change behavior for all worlds.**

This addon should also work on servers, and the client does not need to have the addon installed to use it.

<details>
<summary>Preview of HCSC.properties</summary>

```python
# **Respawn Radius**

# Maximum radius the player will respawn from spawn.
maxRadius=2000

# Minimum radius the player will respawn from spawn.
minRadius=1000

# **Respawn Multipliers**

# Multiplier after activating a Nether portal.
netherMultiplier=1.5

# Multiplier after summoning a Wither.
witherMultiplier=2.0

# Multiplier after activating an End portal.
endMultiplier=2.5

# Multiplier for the Large Biomes world type.
largeMultiplier=4.0

# **Respawn Behaviour**

# The maximum amount of time in ticks between respawns for special respawn behaviour. Players dying within this time will respawn according to quickRespawnRadius. If in multiplayer, players who die in this interval will respawn together, as long as progression hasn't passed hcSoulMating.
maxSpawnTime=10800

# The maximum radius from the previous respawn the player will respawn at if they have died within maxSpawnTime.
quickRespawnRadius=100

# The player's health after a quick respawn (2 = one heart).
quickRespawnHealth=10

# The player's minimum food after a quick respawn (6 = one shank).
quickRespawnMinFood=24

# The reduction per respawn for the player's food after a quick respawn (6 = one shank).
quickRespawnFoodDecrement=6

# The point in progression at which Hardcore Soul Mating stops occuring (Overridden by "AlwaysSpawnTogether" in BTW.properties).
# 0 = On Spawn, 1 = Nether, 2 = Wither, 3 = End.
hcSoulMating=1

# Ticks until a player's items dropped on death despawn.
deathDespawnTime=24000

# **Structure Generation**

# Villages within this radius from spawn will be abandoned.
abandonedVillageRadius=2250

# Villages within this radius from spawn will be partially abandoned.
partiallyAbandonedRadius=3000

# Temples within this radius from spawn will be looted.
lootedTempleRadius=2250
```

</details>

## Compatability
<details>
  <summary>Modified Classes</summary>
  
  - BlockSoulSand (Client|Server)
  - EntityItem (Client|Server)
  - FCUtilsHardcoreSpawn (Client|Server)
</details>
