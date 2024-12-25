# Tweaker

Want to be able to change HC Spawn mechanics? Want to disable lightning fires? This addon is for you!

Currently updated for BTW CE 2.1.4. Compatibility with is untested and not guarenteed.

[Download](https://github.com/BTW-Community/Tweaker/releases/latest)

## Installation
**Fabric is required!**
- Install Fabric with Better Than Wolves according to these [installation instructions](https://github.com/BTW-Community/BTW-gradle-fabric-example#releasing-modsaddons)
- Download `tweaker-x.x.x.jar` and place it into the mods folder
- Enjoy! :)

## Information
Tweaker currently lets you change the HC Spawn mechanics, abandoned village and looted temple generation radius, and whether lightning should start fires.

When you start the game with this addon, a file will be generated at `./config/tweaker.properties`. You can modify these values to determine the default behaviour for your worlds. Don't forget to restart the game after modifying the config!

If instead you want to define specific behaviour for only one world, or you do not want to restart the game, you can use the command `/tweaker <option> [value]` to tweak any of the available settings. `/tweaker <option>` will let you see what the current value is.

For multiplayer only the server needs to have this mod installed.

<details>
<summary>Preview of tweaker.properties</summary>

```python

# The maximum radius the player will respawn from world spawn.
maxSpawnRadius=2000.0

# The minimum radius the player will respawn from world spawn.
minSpawnRadius=1000.0

# The radius at which the player will respawn after dying recently.
quickSpawnRadius=100.0

# The player's health after a quick spawn. (2 = one heart)
quickSpawnHealth=10

# The minimum amount of hunger after a quick spawn. (6 = one shank)
quickSpawnHungerMin=24

# The amount of hunger a player loses on each quick spawn. (6 = one shank)
quickSpawnHungerLoss=6

# Multiplier used for `maxSpawnRadius` when playing with Large Biomes.
largeBiomeMultiplier=4.0

# Multiplier used for 'maxSpawnRadius' after activating an End portal.
endMultiplier=2.5

# Multiplier used for 'maxSpawnRadius' after summoning a Wither.
witherMultiplier=2.0

# Multiplier used for 'maxSpawnRadius' after activating a Nether portal.
netherMultiplier=1.5

# Villages within this radius from world spawn will be abandoned.
abandonedVillageRadius=2250.0

# Villages within this radius from world spawn will be partially abandoned.
partiallyAbandonedVillageRadius=3000.0

# Temples within this radius from world spawn will be looted.
lootedTempleRadius=2250.0

# Determines if lightning should start fires or not.
lightningFire=true

```

</details>

## Dev Instructions

For 2.1.4 mod dev, you'll need to obtain the BTW-sources. See https://github.com/BTW-Community/BTW-gradle for
instructions; the source should be copied to `./src/btw`. Gradle version must be <8 (version 7.2 is configured in
`gradle-wrapper.properties`).

There is a known issue with this dev environment causing the error `Caused by: java.io.IOException: The account being
accessed does not support http. for 
http://resources.download.minecraft.net/99/991b421dfd401f115241601b2b373140a8d78572`. As a workaround, you may need
to manually copy assets from an existing minecraft jar.
