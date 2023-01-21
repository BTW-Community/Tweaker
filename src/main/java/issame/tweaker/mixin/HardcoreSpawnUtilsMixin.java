package issame.tweaker.mixin;

import btw.util.hardcorespawn.HardcoreSpawnUtils;
import btw.world.util.WorldUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import issame.tweaker.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HardcoreSpawnUtils.class)
public abstract class HardcoreSpawnUtilsMixin {

    @Inject(method = "getPlayerSpawnRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlayerSpawnRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                Config.getDouble("maxSpawnRadius")
                * HardcoreSpawnUtils.getWorldTypeRadiusMultiplier()
                * HardcoreSpawnUtils.getGameProgressRadiusMultiplier()
        );
    }

    @Inject(method = "getPlayerSpawnExclusionRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlayerSpawnExclusionRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                Config.getDouble("minSpawnRadius")
                * HardcoreSpawnUtils.getWorldTypeRadiusMultiplier()
        );
    }

    @Inject(method = "getPlayerMultipleRespawnRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlayerMultipleRespawnRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(Config.getDouble("quickSpawnRadius"));
    }

    @Inject(method = "getAbandonedVillageRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getAbandonedVillageRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                Config.getDouble("abandonedVillageRadius")
                * HardcoreSpawnUtils.getWorldTypeRadiusMultiplier()
        );
    }

    @Inject(method = "getPartiallyAbandonedVillageRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPartiallyAbandonedVillageRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                Config.getDouble("partiallyAbandonedVillageRadius")
                * HardcoreSpawnUtils.getWorldTypeRadiusMultiplier()
        );
    }

    @Inject(method = "getLootedTempleRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getLootedTempleRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                Config.getDouble("lootedTempleRadius")
                * HardcoreSpawnUtils.getWorldTypeRadiusMultiplier()
        );
    }

    @Inject(method = "getWorldTypeRadiusMultiplier()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getWorldTypeRadiusMultiplier(CallbackInfoReturnable<Double> cir) {
        double multiplier = 1D;
        World overworld = MinecraftServer.getServer().worldServers[0];
        if (overworld != null && overworld.getWorldInfo().getTerrainType() == WorldType.LARGE_BIOMES ) {
            multiplier = Config.getDouble("largeBiomeMultiplier");
        }
        cir.setReturnValue(multiplier);
    }

    @Inject(method = "getGameProgressRadiusMultiplier()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getGameProgressRadiusMultiplier(CallbackInfoReturnable<Double> cir) {
        double multiplier = 1D;
        if (WorldUtils.gameProgressHasEndDimensionBeenAccessedServerOnly()) {
            multiplier = Config.getDouble("endMultiplier");
        } else if ( WorldUtils.gameProgressHasWitherBeenSummonedServerOnly()) {
            multiplier = Config.getDouble("witherMultiplier");
        } else if ( WorldUtils.gameProgressHasNetherBeenAccessedServerOnly()) {
            multiplier = Config.getDouble("netherMultiplier");
        }
        cir.setReturnValue(multiplier);
    }

    private static final String HANDLE_HARDCORE_SPAWN =
            "handleHardcoreSpawn(" +
            "Lnet/minecraft/server/MinecraftServer;" +
            "Lnet/minecraft/src/EntityPlayerMP;" +
            "Lnet/minecraft/src/EntityPlayerMP;)V";

    @ModifyConstant(method = HANDLE_HARDCORE_SPAWN, constant = @Constant(intValue = 10))
    private static int quickSpawnHealth(int health) {
        return Config.getInteger("quickSpawnHealth");
    }

    @ModifyVariable(method = HANDLE_HARDCORE_SPAWN, at = @At(value = "STORE", ordinal = 0), ordinal = 0)
    private static int quickSpawnHungerLoss(int foodLevel) {
        // Add 6 to counteract `iFoodLevel -= 6`.
        return foodLevel - Config.getInteger("quickSpawnHungerLoss") + 6;
    }

    @ModifyConstant(method = HANDLE_HARDCORE_SPAWN, constant = @Constant(intValue = 24))
    private static int quickSpawnHungerMin(int hungerMin) {
        return Config.getInteger("quickSpawnHungerMin");
    }
}
