package org.mve.sn.mixin;

import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DigDurabilityEnchantment.class)
public class DigDurabilityEnchantmentMixin
{
	@Inject(at = @At("RETURN"), method = "getMaxLevel", cancellable = true)
	public void getMaxLevel(CallbackInfoReturnable<Integer> ci)
	{
		ci.setReturnValue(5);
	}
}
