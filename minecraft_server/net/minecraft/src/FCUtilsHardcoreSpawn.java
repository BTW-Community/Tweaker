package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class FCUtilsHardcoreSpawn
{
    public static final int m_iHardcoreSpawnTimeBetweenReassignments = HCSCMod.getConfigInt("maxSpawnTime", "24000");
    private static final double m_dRadiusBase = HCSCMod.getConfigDouble("maxRespawnRadius", "2000.0");
    private static final double m_dRadiusBaseExclusionDistance = HCSCMod.getConfigDouble("minRespawnRadius", "1000.0");
    private static final double m_dRadiusBaseMultipleRespawn = HCSCMod.getConfigDouble("quickRespawnRadius", "100.0");
    private static final double m_dRadiusBaseVillageAbandoned = HCSCMod.getConfigDouble("abandonedVillageRadius", "2250.0");
    private static final double m_dRadiusBaseVillagePartiallyAbandoned = HCSCMod.getConfigDouble("partiallyAbandonedVillageRadius", "3000.0");
    private static final double m_dRadiusBaseLootedTemple = HCSCMod.getConfigDouble("lootedTempleRadius", "2250.0");
    private static final double m_dLargeBiomesRadiusMultiplier = HCSCMod.getConfigDouble("largeMultiplier", "4.0");
    
    private static final double m_dEndRadiusMultiplier = HCSCMod.getConfigDouble("endMultiplier", "4.0");
    private static final double m_dWitherRadiusMultiplier = HCSCMod.getConfigDouble("witherMultiplier", "2.0");
    private static final double m_dNetherRadiusMultiplier = HCSCMod.getConfigDouble("netherMultiplier", "1.5");
    
    private static final int m_dQuickRespawnHealth = Math.max(HCSCMod.getConfigInt("quickRespawnHealth", "10"), 0);
    private static final int m_dQuickRespawnMinFood = Math.max(HCSCMod.getConfigInt("quickRespawnMinFood", "12"), 0);
    private static final int m_dQuickRespawnFoodDecrement = Math.max(HCSCMod.getConfigInt("quickRespawnFoodDecrement", "6"), 0);
    private static final int m_dHCSoulMatingProgression = HCSCMod.getConfigInt("hcSoulMatingProgression", "1");

    public static double GetPlayerSpawnRadius()
    {
        return m_dRadiusBase * GetWorldTypeRadiusMultiplier() * GetGameProgressRadiusMultiplier();
    }

    public static double GetPlayerSpawnExclusionRadius()
    {
        return m_dRadiusBaseExclusionDistance * GetWorldTypeRadiusMultiplier() * GetGameProgressRadiusMultiplier();
    }

    public static double GetPlayerMultipleRespawnRadius()
    {
        return m_dRadiusBaseMultipleRespawn;
    }

    public static double GetAbandonedVillageRadius()
    {
        return m_dRadiusBaseVillageAbandoned * GetWorldTypeRadiusMultiplier();
    }

    public static double GetPartiallyAbandonedVillageRadius()
    {
        return m_dRadiusBaseVillagePartiallyAbandoned * GetWorldTypeRadiusMultiplier();
    }

    public static double GetLootedTempleRadius()
    {
        return m_dRadiusBaseLootedTemple * GetWorldTypeRadiusMultiplier();
    }

    public static double GetWorldTypeRadiusMultiplier()
    {
        WorldServer var0 = MinecraftServer.getServer().worldServers[0];
        if (var0 != null && var0.worldInfo.getTerrainType() == WorldType.LARGE_BIOMES) {
        	return m_dLargeBiomesRadiusMultiplier;
        } else {
        	return 1.0D;
        }
    }

    public static double GetGameProgressRadiusMultiplier()
    {
    	if (FCUtilsWorld.GameProgressHasEndDimensionBeenAccessedServerOnly()) {
    		return m_dEndRadiusMultiplier;
    	} else if (FCUtilsWorld.GameProgressHasWitherBeenSummonedServerOnly()) {
    		return m_dWitherRadiusMultiplier;
    	} else if (FCUtilsWorld.GameProgressHasNetherBeenAccessedServerOnly()) {
    		return m_dNetherRadiusMultiplier;
    	} else {
    		return 1.0D;
    	}
    }

    public static void HandleHardcoreSpawn( MinecraftServer server, EntityPlayerMP oldPlayer, EntityPlayerMP newPlayer )
    {
        WorldServer newWorld = server.worldServerForDimension( newPlayer.dimension );
        
    	if ( oldPlayer.playerConqueredTheEnd )
    	{
    		ReturnPlayerToOriginalSpawn( newWorld, newPlayer );
    		
    		return;
    	}
    	
		long lOverworldTime = FCUtilsWorld.GetOverworldTimeServerOnly();
		long lTimeOfLastPlayerSpawnAssignment = oldPlayer.m_lTimeOfLastSpawnAssignment;
		long lDeltaTimeSinceLastRespawnAssignment = lOverworldTime - lTimeOfLastPlayerSpawnAssignment; 
		
		boolean bSoftRespawn = false;
		
		if ( lTimeOfLastPlayerSpawnAssignment > 0 && lDeltaTimeSinceLastRespawnAssignment >= 0 &&  
			lDeltaTimeSinceLastRespawnAssignment < m_iHardcoreSpawnTimeBetweenReassignments )
		{
			// multiple respawns in a short period of time results in different behavior
			
			bSoftRespawn = true;
			
            newPlayer.health = m_dQuickRespawnHealth; // start the player hurt
            
            int iFoodLevel = oldPlayer.foodStats.getFoodLevel();
            
            iFoodLevel -= m_dQuickRespawnFoodDecrement; // knock off a 1 pip food penalty
            
            if ( iFoodLevel < m_dQuickRespawnMinFood )
            {
            	iFoodLevel = m_dQuickRespawnMinFood;
            }

        	newPlayer.foodStats.setFoodLevel( iFoodLevel );            
		}
		
		int progression = 0;
		if (FCUtilsWorld.GameProgressHasEndDimensionBeenAccessedServerOnly()) {
    		progression = 3;
    	} else if (FCUtilsWorld.GameProgressHasWitherBeenSummonedServerOnly()) {
    		progression = 2;
    	} else if (FCUtilsWorld.GameProgressHasNetherBeenAccessedServerOnly()) {
    		progression = 1;
    	}
			
    	if ( progression < m_dHCSoulMatingProgression )
    	{
    		// early game, players are tied to respawning together
    		
    		FCSpawnLocation recentLocation = newWorld.GetSpawnLocationList().GetMostRecentSpawnLocation();
    		
    		if ( recentLocation != null )
    		{
    			long lDeltaTime = lOverworldTime - recentLocation.m_lSpawnTime;

    			if ( lDeltaTime > 0 && lDeltaTime < FCUtilsHardcoreSpawn.m_iHardcoreSpawnTimeBetweenReassignments )
    			{
    				if ( AssignPlayerToOldSpawnPosWithVariance( newWorld, newPlayer,  
    					new ChunkCoordinates( recentLocation.m_iIPos, recentLocation.m_iJPos, recentLocation.m_iKPos ), 
						recentLocation.m_lSpawnTime ) )
    				{
    					return;
    				}
    			}
    		}
    	}
    	
		ChunkCoordinates oldSpawnPos = oldPlayer.m_HardcoreSpawnChunk;		
		
		// if a day has passed since the last spawn assignment, assign a new one
		
		if ( oldSpawnPos == null || !bSoftRespawn || 
			!AssignPlayerToOldSpawnPosWithVariance( newWorld, newPlayer, oldSpawnPos, lTimeOfLastPlayerSpawnAssignment ) )
		{
			if ( !AssignNewHardcoreSpawnLocation( newWorld, server, newPlayer ) )
			{
				ReturnPlayerToOriginalSpawn( newWorld, newPlayer );
				
		        return;
			}
		}

		ChunkCoordinates newSpawnPos = newPlayer.m_HardcoreSpawnChunk;
		
		if ( newSpawnPos != null )
		{
			newWorld.GetSpawnLocationList().AddPointIfNotAlreadyPresent( newSpawnPos.posX, newSpawnPos.posY, newSpawnPos.posZ, newPlayer.m_lTimeOfLastSpawnAssignment );
		}
    }

    public static boolean AssignNewHardcoreSpawnLocation(World var0, MinecraftServer var1, EntityPlayerMP var2)
    {
        boolean var3 = false;
        double var4 = GetPlayerSpawnRadius();
        double var6 = GetPlayerSpawnExclusionRadius();
        double var10000 = var4 - var6;
        double var10 = var6 * var6;
        double var12 = var4 * var4 - var10;

        for (int var14 = 0; var14 < 20; ++var14)
        {
            double var15 = Math.sqrt(var0.rand.nextDouble() * var12 + var10);
            double var17 = var0.rand.nextDouble() * Math.PI * 2.0D;
            double var19 = -Math.sin(var17) * var15;
            double var21 = Math.cos(var17) * var15;
            int var23 = MathHelper.floor_double(var19) + var0.worldInfo.getSpawnX();
            int var24 = MathHelper.floor_double(var21) + var0.worldInfo.getSpawnZ();
            int var25 = var0.getTopSolidOrLiquidBlock(var23, var24);

            if (var25 >= var0.provider.getAverageGroundLevel())
            {
                Material var26 = var0.getBlockMaterial(var23, var25, var24);

                if (var26 == null || !var26.isLiquid())
                {
                    var2.setLocationAndAngles((double)var23 + 0.5D, (double)var25 + 1.5D, (double)var24 + 0.5D, var0.rand.nextFloat() * 360.0F, 0.0F);
                    BumpPlayerPosUpwardsUntilValidSpawnReached(var2);
                    long var27 = FCUtilsWorld.GetOverworldTimeServerOnly();

                    if (FCBetterThanWolves.IsSinglePlayerNonLan())
                    {
                        var27 = (var27 / 24000L + 1L) * 24000L;

                        for (int var29 = 0; var29 < MinecraftServer.getServer().worldServers.length; ++var29)
                        {
                            WorldServer var30 = MinecraftServer.getServer().worldServers[var29];
                            var30.setWorldTime(var27);

                            if (var30.worldInfo.isThundering())
                            {
                                var30.worldInfo.setThundering(false);
                                var1.getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(8, 0));
                            }
                        }
                    }

                    var2.m_lTimeOfLastSpawnAssignment = var27;
                    ChunkCoordinates var31 = new ChunkCoordinates(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));
                    var2.m_HardcoreSpawnChunk = var31;
                    var3 = true;
                    break;
                }
            }
        }

        return var3;
    }

    private static boolean AssignPlayerToOldSpawnPosWithVariance(World var0, EntityPlayerMP var1, ChunkCoordinates var2, long var3)
    {
        for (int var5 = 0; var5 < 20; ++var5)
        {
            double var6 = Math.sqrt(var0.rand.nextDouble()) * GetPlayerMultipleRespawnRadius();
            double var8 = var0.rand.nextDouble() * Math.PI * 2.0D;
            double var10 = -Math.sin(var8) * var6;
            double var12 = Math.cos(var8) * var6;
            int var14 = MathHelper.floor_double(var10) + var2.posX;
            int var15 = MathHelper.floor_double(var12) + var2.posZ;
            int var16 = var0.getTopSolidOrLiquidBlock(var14, var15);

            if (var16 >= var0.provider.getAverageGroundLevel())
            {
                Material var17 = var0.getBlockMaterial(var14, var16, var15);

                if (var17 == null || !var17.isLiquid())
                {
                    var1.setLocationAndAngles((double)var14 + 0.5D, (double)var16 + 1.5D, (double)var15 + 0.5D, var0.rand.nextFloat() * 360.0F, 0.0F);
                    BumpPlayerPosUpwardsUntilValidSpawnReached(var1);
                    var1.m_lTimeOfLastSpawnAssignment = var3;
                    var1.m_HardcoreSpawnChunk = var2;
                    return true;
                }
            }
        }

        return AssignPlayerToOldSpawnPos(var0, var1, var2, var3);
    }

    private static boolean AssignPlayerToOldSpawnPos(World var0, EntityPlayerMP var1, ChunkCoordinates var2, long var3)
    {
        int var5 = MathHelper.floor_double((double)var2.posX);
        int var6 = MathHelper.floor_double((double)var2.posZ);
        int var7 = var0.getTopSolidOrLiquidBlock(var5, var6);
        var1.setLocationAndAngles((double)var5 + 0.5D, (double)var7 + 1.5D, (double)var6 + 0.5D, var0.rand.nextFloat() * 360.0F, 0.0F);
        var0.getBlockMaterial(var5, var7 + 1, var6);

        if (OffsetPlayerPositionUntilValidSpawn(var0, var1))
        {
            BumpPlayerPosUpwardsUntilValidSpawnReached(var1);
            var1.m_lTimeOfLastSpawnAssignment = var3;
            ChunkCoordinates var9 = new ChunkCoordinates(MathHelper.floor_double(var1.posX), MathHelper.floor_double(var1.posY), MathHelper.floor_double(var1.posZ));
            var1.m_HardcoreSpawnChunk = var9;
            return true;
        }
        else
        {
            return false;
        }
    }

    private static boolean OffsetPlayerPositionUntilValidSpawn(World var0, EntityPlayerMP var1)
    {
        int var2 = MathHelper.floor_double(var1.posX);
        int var3 = MathHelper.floor_double(var1.posZ);

        for (int var4 = 0; var4 < 20; ++var4)
        {
            int var5 = var0.getTopSolidOrLiquidBlock(var2, var3);
            Material var6 = var0.getBlockMaterial(var2, var5, var3);

            if (var6 == null || !var6.isLiquid())
            {
                var1.setLocationAndAngles((double)var2 + 0.5D, var1.posY, (double)var3 + 0.5D, var1.rotationYaw, var1.rotationPitch);
                return true;
            }

            var2 += var0.rand.nextInt(11) - 5;
            var3 += var0.rand.nextInt(11) - 5;
        }

        return false;
    }

    private static void ReturnPlayerToOriginalSpawn(World var0, EntityPlayerMP var1)
    {
        ChunkCoordinates var2 = var0.getSpawnPoint();
        int var3 = var2.posX;
        int var4 = var2.posY;
        int var5 = var2.posZ;

        if (!var0.provider.hasNoSky && var0.getWorldInfo().getGameType() != EnumGameType.ADVENTURE)
        {
            var3 += var0.rand.nextInt(20) - 10;
            var4 = var0.getTopSolidOrLiquidBlock(var3, var5);
            var5 += var0.rand.nextInt(20) - 10;
        }

        var1.setLocationAndAngles((double)var3 + 0.5D, (double)var4 + 1.5D, (double)var5 + 0.5D, 0.0F, 0.0F);
        BumpPlayerPosUpwardsUntilValidSpawnReached(var1);
        var1.m_lTimeOfLastSpawnAssignment = 0L;
        var1.m_HardcoreSpawnChunk = null;
    }

    private static void BumpPlayerPosUpwardsUntilValidSpawnReached(EntityPlayerMP var0)
    {
        while (true)
        {
            if (var0.posY > 0.0D)
            {
                var0.setLocationAndAngles(var0.posX, var0.posY, var0.posZ, var0.rotationYaw, var0.rotationPitch);

                if (!var0.worldObj.getCollidingBoundingBoxes(var0, var0.boundingBox).isEmpty())
                {
                    ++var0.posY;
                    continue;
                }
            }

            return;
        }
    }

    public static boolean IsInLootedTempleRadius(World var0, int var1, int var2)
    {
        int var3 = var0.getWorldInfo().getSpawnX();
        int var4 = var0.getWorldInfo().getSpawnZ();
        double var5 = (double)(var3 - var1);
        double var7 = (double)(var4 - var2);
        double var9 = var5 * var5 + var7 * var7;
        double var11 = GetLootedTempleRadius();
        return var9 < var11 * var11;
    }
    
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
