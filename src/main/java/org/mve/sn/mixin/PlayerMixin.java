package org.mve.sn.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin
{
	@Inject(
		method = "getProjectile",
		at = @At("RETURN"),
		cancellable = true
	)
	public void getProjectile(ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir)
	{
		if (!Configuration.INFINITY_FIX.get()) return;
		Player player = (Player) (Object) this;
		if (player.level().isClientSide) return;
		if (cir.getReturnValue() != null && !cir.getReturnValue().isEmpty()) return;
		ItemStack item = Items.ARROW.getDefaultInstance();
		Holder.Reference<Enchantment> infinity = player.level()
			.registryAccess()
			.lookupOrThrow(Registries.ENCHANTMENT)
			.getOrThrow(Enchantments.INFINITY);
		if (itemStack.getEnchantments().getLevel(infinity) > 0)
			cir.setReturnValue(item);
	}
}
