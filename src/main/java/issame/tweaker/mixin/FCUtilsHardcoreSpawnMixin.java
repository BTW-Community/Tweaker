package issame.tweaker.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FCUtilsHardcoreSpawn.class)
public abstract class FCUtilsHardcoreSpawnMixin {

    @Inject(method = "GetPlayerSpawnRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void GetPlayerSpawnRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                TweakerAddon.getDouble("maxSpawnRadius")
                * FCUtilsHardcoreSpawn.GetWorldTypeRadiusMultiplier()
                * FCUtilsHardcoreSpawn.GetGameProgressRadiusMultiplier()
        );
    }

    @Inject(method = "GetPlayerSpawnExclusionRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void GetPlayerSpawnExclusionRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                TweakerAddon.getDouble("minSpawnRadius")
                * FCUtilsHardcoreSpawn.GetWorldTypeRadiusMultiplier()
        );
    }

    @Inject(method = "GetPlayerMultipleRespawnRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void GetPlayerMultipleRespawnRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(TweakerAddon.getDouble("quickSpawnRadius"));
    }

    @Inject(method = "GetAbandonedVillageRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void GetAbandonedVillageRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                TweakerAddon.getDouble("abandonedVillageRadius")
                * FCUtilsHardcoreSpawn.GetWorldTypeRadiusMultiplier()
        );
    }

    @Inject(method = "GetPartiallyAbandonedVillageRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void GetPartiallyAbandonedVillageRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                TweakerAddon.getDouble("partiallyAbandonedVillageRadius")
                * FCUtilsHardcoreSpawn.GetWorldTypeRadiusMultiplier()
        );
    }

    @Inject(method = "GetLootedTempleRadius()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void GetLootedTempleRadius(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(
                TweakerAddon.getDouble("lootedTempleRadius")
                * FCUtilsHardcoreSpawn.GetWorldTypeRadiusMultiplier()
        );
    }

    @Inject(method = "GetWorldTypeRadiusMultiplier()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void GetWorldTypeRadiusMultiplier(CallbackInfoReturnable<Double> cir) {
        double multiplier = 1D;
        World overworld = MinecraftServer.getServer().worldServers[0];
        if (overworld != null && overworld.getWorldInfo().getTerrainType() == WorldType.LARGE_BIOMES ) {
            multiplier = TweakerAddon.getDouble("largeBiomeMultiplier");
        }
        cir.setReturnValue(multiplier);
    }

    @Inject(method = "GetGameProgressRadiusMultiplier()D", at = @At("HEAD"), cancellable = true, remap = false)
    private static void GetGameProgressRadiusMultiplier(CallbackInfoReturnable<Double> cir) {
        double multiplier = 1D;
        if (FCUtilsWorld.GameProgressHasEndDimensionBeenAccessedServerOnly()) {
            multiplier = TweakerAddon.getDouble("endMultiplier");;
        } else if ( FCUtilsWorld.GameProgressHasWitherBeenSummonedServerOnly()) {
            multiplier = TweakerAddon.getDouble("witherMultiplier");;
        } else if ( FCUtilsWorld.GameProgressHasNetherBeenAccessedServerOnly()) {
            multiplier = TweakerAddon.getDouble("netherMultiplier");;
        }
        cir.setReturnValue(multiplier);
    }
}
