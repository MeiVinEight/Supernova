package org.mve.sn.mixin;

import net.minecraft.world.item.enchantment.DamageEnchantment;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public class DamageEnchantmentMixin
{
	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	public void getMaxLevel(CallbackInfoReturnable<Integer> cir)
	{
		DamageEnchantment _this = (DamageEnchantment)(Object) this;
		// Sharpness
		if (_this.type == 0) cir.setReturnValue(Configuration.MAX_LEVEL_SHARPNESS.get());
	}
}
