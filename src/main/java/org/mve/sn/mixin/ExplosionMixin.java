package org.mve.sn.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin
{
	@Final
	@Mutable
	@Shadow
	private Explosion.BlockInteraction blockInteraction;

	@Inject(
		method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/Holder;)V",
		at = @At("RETURN")
	)
	private void initReturn(Level level, Entity entity, DamageSource damageSource, ExplosionDamageCalculator explosionDamageCalculator, double d, double e, double f, float g, boolean bl, Explosion.BlockInteraction blockInteraction, ParticleOptions particleOptions, ParticleOptions particleOptions2, Holder holder, CallbackInfo ci)
	{
		if (entity == null) return;
		ResourceLocation key = entity.level()
			.registryAccess()
			.registryOrThrow(Registries.ENTITY_TYPE)
			.getKey(entity.getType());
		if (key == null) return;
		if (Configuration.ENTITY_EXPLOSION.get().contains(key.toString()))
			this.blockInteraction = Explosion.BlockInteraction.KEEP;
	}
}
