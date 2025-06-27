package org.mve.sn.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getJumpPower()F", cancellable = true)
	public void getJumpPower(CallbackInfoReturnable<Float> cir)
	{
		if (!Configuration.SUPER_JUMP.get())
			return;
		LivingEntity _this = (LivingEntity) (Object) this;
		if (!(_this instanceof Player player))
			return;
		float jumpPower = cir.getReturnValue();
		if (player.isShiftKeyDown())
			cir.setReturnValue(jumpPower * Configuration.SUPER_JUMP_SCALE.get().floatValue());
	}
}
