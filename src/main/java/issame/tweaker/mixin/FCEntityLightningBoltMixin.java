package issame.tweaker.mixin;

import issame.tweaker.Config;
import net.minecraft.src.FCEntityLightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FCEntityLightningBolt.class)
public abstract class FCEntityLightningBoltMixin {
    @Inject(method = "IsStrikingLightningRod()Z", at = @At("RETURN"), cancellable = true, remap = false)
    private void tweakLightningFire(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || !Config.getBoolean("lightningFire"));
    }
}
