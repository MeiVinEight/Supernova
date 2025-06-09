package org.mve.sn.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import org.mve.sn.EnderBow;
import org.mve.sn.Supernova;
import org.mve.sn.SupernovaArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Arrow.class)
public class ArrowMixin implements SupernovaArrow
{
	@Unique
	public UUID supernova;

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V")
	public void arrow(Level p_36866_, LivingEntity p_36867_, CallbackInfo ci)
	{
		EnderBow.arrow((Arrow) (Object) this, p_36867_);
	}

	@Unique
	@Override
	public UUID supernova()
	{
		return this.supernova;
	}

	@Unique
	@Override
	public void supernova(UUID uuid)
	{
		this.supernova = uuid;
	}
}
