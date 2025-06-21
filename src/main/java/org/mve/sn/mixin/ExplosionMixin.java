package org.mve.sn.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.mve.sn.Configuration;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Explosion.class)
public class ExplosionMixin
{
	@Mutable
	@Final
	@Shadow
	private Explosion.BlockInteraction blockInteraction;
	@Inject(
		at = @At("RETURN"),
		method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;)V"
	)
	// {"target":"CLASS","name":"net.minecraft.world.level.Explosion$BlockInteraction"}
	// {"target":"FIELD","class":"net.minecraft.world.level.Explosion","fieldName":"f_46010_"}
	private void changeBlockInteraction(Level p_46051_, @Nullable Entity p_46052_, @Nullable DamageSource p_46053_, @Nullable ExplosionDamageCalculator p_46054_, double p_46055_, double p_46056_, double p_46057_, float p_46058_, boolean p_46059_, Explosion.BlockInteraction p_46060_, CallbackInfo ci)
	{
		List<? extends String> list = Configuration.ENTITY_EXPLOSION.get();
		Supernova.LOGGER.info(list.toString());
		if (list.isEmpty())
			return;
		if (p_46052_ == null)
			return;
		ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(p_46052_.getType());
		if (key == null)
		{
			Supernova.LOGGER.warn("Entity type not found: {}", p_46052_.getType());
			return;
		}
		if (!list.contains(key.toString()))
			return;
		this.blockInteraction = Explosion.BlockInteraction.KEEP;
	}
}
