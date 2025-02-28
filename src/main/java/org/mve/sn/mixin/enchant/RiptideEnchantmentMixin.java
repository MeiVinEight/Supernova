package org.mve.sn.mixin.enchant;

import net.minecraft.enchantment.RiptideEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RiptideEnchantment.class)
public class RiptideEnchantmentMixin
{
	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	public void getMaxLevel(CallbackInfoReturnable<Integer> cir)
	{
		cir.setReturnValue(5);
	}
}
