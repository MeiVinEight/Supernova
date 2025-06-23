package org.mve.sn.mixin;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.LootBonusEnchantment;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootBonusEnchantment.class)
public class LootBonusEnchantmentMixin
{
	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	public void getMaxLevel(CallbackInfoReturnable<Integer> cir)
	{
		LootBonusEnchantment _this = (LootBonusEnchantment)(Object) this;
		if (_this.category == EnchantmentCategory.WEAPON)
			cir.setReturnValue(Configuration.MAX_LEVEL_LOOTING.get());
		if (_this.category == EnchantmentCategory.DIGGER)
			cir.setReturnValue(Configuration.MAX_LEVEL_FORTUNE.get());
		if (_this.category == EnchantmentCategory.FISHING_ROD)
			cir.setReturnValue(Configuration.MAX_LEVEL_LUCKOFTHESEA.get());
	}
}
