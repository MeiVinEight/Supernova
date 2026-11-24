package org.mve.sn.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.Vec3;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerExplosion.class)
public class ExplosionMixin
{
	@Final
	@Mutable
	@Shadow
	private Explosion.BlockInteraction blockInteraction;

	@Inject(
		method = "<init>",
		at = @At("RETURN")
	)
	private void initReturn(ServerLevel level, Entity entity, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, Vec3 center, float radius, boolean fire, Explosion.BlockInteraction blockInteraction, CallbackInfo ci)
	{
		if (entity == null) return;
		Identifier key = entity.level()
			.registryAccess()
			.get(Registries.ENTITY_TYPE)
			.orElseThrow()
			.value()
			.getKey(entity.getType());
		if (key == null) return;
		if (Configuration.ENTITY_EXPLOSION.get().contains(key.toString()))
			this.blockInteraction = Explosion.BlockInteraction.KEEP;
	}

	/*
	@Redirect(
		method = "explode",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Set;add(Ljava/lang/Object;)Z",
			ordinal = 0
		)
	)
	private <E> boolean explode$add$0(Set<E> instance, E e, @Local(ordinal = 0) BlockState blockState)
	{
		if (blockState.isAir())
			return false;
		BlockPos pos = (BlockPos) e;
		//if (this.level.isEmptyBlock(pos))
		//	return false;
		this.toBlow.add(pos);
		return true;
	}

	@Redirect(
		method = "explode",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;",
			ordinal = 0
		)
	)
	private FluidState supernova$explode$getFluidState(Level level, BlockPos pos, @Local(ordinal = 0) BlockState state)
	{
		if (state == null)
			return Fluids.EMPTY.defaultFluidState();
		return state.getFluidState();
	}
	*/
}
