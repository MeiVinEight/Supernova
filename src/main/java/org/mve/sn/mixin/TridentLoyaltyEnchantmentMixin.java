package org.mve.sn.mixin;

import net.minecraft.world.item.enchantment.TridentLoyaltyEnchantment;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentLoyaltyEnchantment.class)
public class TridentLoyaltyEnchantmentMixin
{
	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	public void getMaxLevel(CallbackInfoReturnable<Integer> cir)
	{
		cir.setReturnValue(Configuration.MAX_LEVEL_LOYALTY.get());
	}
}
