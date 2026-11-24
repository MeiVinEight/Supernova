package org.mve.sn.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashSet;
import java.util.Set;

@Mixin(Bat.class)
public class BatMixin
{
	@Unique
	private static boolean supernova$batCanSpawnOnInit = true;
	@Unique
	private static Set<Block> supernova$batCanSpawnOnBlocks = new HashSet<>();

	@Redirect(
		method = "checkBatSpawnRules",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ambient/Bat;checkMobSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
			ordinal = 0
		)
	)
	private static boolean checkBatSpawnRules$checkMobSpawnRules(EntityType<Bat> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource)
	{
		if (supernova$batCanSpawnOnInit)
		{
			supernova$batCanSpawnOnBlocks = Set.of(
				Blocks.STONE,
				Blocks.GRANITE,
				Blocks.DIORITE,
				Blocks.ANDESITE,
				Blocks.TUFF,
				Blocks.DEEPSLATE
			);
			supernova$batCanSpawnOnInit = false;
		}
		BlockState blockState = levelAccessor.getBlockState(blockPos.below());
		if (!supernova$batCanSpawnOnBlocks.contains(blockState.getBlock()))
			return false;
		return Mob.checkMobSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
	}
}
