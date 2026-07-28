package org.mve.sn.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	@Shadow
	public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);

	public LivingEntityMixin(EntityType<?> entityType, Level level)
	{
		super(entityType, level);
	}

	@Inject(
		method = "updateFallFlying",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;",
			ordinal = 0
		),
		cancellable = true
	)
	public void updateFallFlying$is0(CallbackInfo ci)
	{
		LivingEntity living = (LivingEntity) (Object) this;
		if (!Supernova.glidable(living)) return;
		ci.cancel();
		int i = living.getFallFlyingTicks() + 1;
		if (!living.level().isClientSide)
		{
			this.setSharedFlag(7, true);
			if (i % 10 == 0)
			{
				int j = i / 10;
				if (j % 2 == 0)
				{
					EquipmentSlot[] slots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
					for (EquipmentSlot slot : slots)
					{
						ItemStack stack;
						if (!(stack = this.getItemBySlot(slot)).isEmpty())
						{
							stack.hurtAndBreak(1, living, slot);
							break;
						}
					}
				}
				this.gameEvent(GameEvent.ELYTRA_GLIDE);
			}
		}
	}

	@Inject(
		method = "createLivingAttributes",
		at = @At("RETURN")
	)
	private static void createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir)
	{
		cir.getReturnValue().add(Supernova.ATRIBUTE_GLIDABLE);
	}
}
