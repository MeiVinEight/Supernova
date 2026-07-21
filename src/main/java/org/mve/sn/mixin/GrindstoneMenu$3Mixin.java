package org.mve.sn.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$3")
public class GrindstoneMenu$3Mixin
{
	@Redirect(
		method = "mayPlace",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;hasAnyEnchantments(Lnet/minecraft/world/item/ItemStack;)Z",
			ordinal = 0
		)
	)
	public boolean mayPlace(ItemStack itemStack)
	{
		return EnchantmentHelper.hasAnyEnchantments(itemStack) || (itemStack.getItem() == Items.BOOK);
	}
}
