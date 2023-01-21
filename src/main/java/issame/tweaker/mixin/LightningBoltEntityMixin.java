package issame.tweaker.mixin;

import btw.entity.LightningBoltEntity;
import issame.tweaker.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightningBoltEntity.class)
public abstract class LightningBoltEntityMixin {
    @Inject(method = "isStrikingLightningRod()Z", at = @At("RETURN"), cancellable = true, remap = false)
    private void tweakLightningFire(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || !Config.getBoolean("lightningFire"));
    }
}
