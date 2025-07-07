package org.mve.sn.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity
{
	public PlayerMixin(EntityType<?> p_19870_, Level p_19871_)
	{
		super(p_19870_, p_19871_);
	}

	@Inject(at = @At("RETURN"), method = "getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", cancellable = true)
	public void getProjectile(ItemStack p_36349_, CallbackInfoReturnable<ItemStack> cir)
	{
		if (!Configuration.INFINITY_FIX.get())
			return;
		if (this.level().isClientSide())
			return;
		if (cir.getReturnValue() != null && !cir.getReturnValue().isEmpty())
			return;
		ItemStack item = Items.ARROW.getDefaultInstance();
		if (p_36349_.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0)
			cir.setReturnValue(item);
	}
}
