package org.mve.sn.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.LootBonusEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin
{
	@Inject(at = @At("RETURN"), method = "isCompatibleWith", cancellable = true)
	public final void isCompatibleWith(Enchantment p_44696_, CallbackInfoReturnable<Boolean> cir)
	{
		cir.setReturnValue((Object) this != p_44696_);
	}

	@Inject(at = @At("RETURN"), method = "canApplyAtEnchantingTable", cancellable = true, remap = false)
	public void canApplyAtEnchantingTable(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
	{
		Enchantment _this = (Enchantment) (Object) this;
		if (_this instanceof LootBonusEnchantment luck)
		{
			if (luck.category == EnchantmentCategory.WEAPON)
			{
				boolean flag = cir.getReturnValue();
				flag |= stack.getItem() == Items.BOW;
				flag |= stack.getItem() == Items.CROSSBOW;
				flag |= stack.getItem() instanceof AxeItem;
				flag |= stack.getItem() == Items.TRIDENT;
				flag |= stack.getItem() instanceof SwordItem;
				cir.setReturnValue(flag);
			}
		}
	}
}
