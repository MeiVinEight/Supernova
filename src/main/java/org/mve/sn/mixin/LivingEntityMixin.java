package org.mve.sn.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> entityType, Level level)
	{
		super(entityType, level);
	}

	@Redirect(
		method = "updateFallFlying",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
			ordinal = 0
		)
	)
	public boolean updateFallFlying$is0(ItemStack instance, Item item)
	{
		return instance.is(item) || Supernova.gliding(this.level(), instance);
	}

	@Inject(
		method = "createLivingAttributes",
		at = @At("RETURN")
	)
	private static void createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir)
	{
	}
}
