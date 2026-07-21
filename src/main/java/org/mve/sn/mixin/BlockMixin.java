package org.mve.sn.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin
{
	@Shadow
	public static List<ItemStack> getDrops(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack itemStack)
	{
		throw new AssertionError("Implemented via mixin");
	}

	@Shadow
	public static void popResource(Level level, BlockPos blockPos, ItemStack itemStack)
	{
		throw new AssertionError("Implemented via mixin");
	}

	@Inject(
		method = "dropResources(" +
			"Lnet/minecraft/world/level/block/state/BlockState;" +
			"Lnet/minecraft/world/level/Level;" +
			"Lnet/minecraft/core/BlockPos;" +
			"Lnet/minecraft/world/level/block/entity/BlockEntity;" +
			"Lnet/minecraft/world/entity/Entity;" +
			"Lnet/minecraft/world/item/ItemStack;" +
			")V",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void dropResource(
		BlockState blockState,
		Level level,
		BlockPos blockPos,
		BlockEntity blockEntity,
		Entity entity,
		ItemStack itemStack,
		CallbackInfo ci
	)
	{
		if (!Configuration.DIGGER_PICKUP.get()) return;
		if (!(entity instanceof ServerPlayer player)) return;
		if (!(player.isShiftKeyDown())) return;
		ci.cancel();
		List<ItemStack> drops = getDrops(blockState, (ServerLevel) level, blockPos, blockEntity, entity, itemStack);
		for (ItemStack drop : drops)
		{
			if (player.addItem(drop))
				level.playSound(
					null,
					blockPos,
					SoundEvents.ITEM_PICKUP,
					SoundSource.PLAYERS,
					0.2F,
					((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
				);
			if (!drop.isEmpty()) popResource(level, blockPos, drop);
		}
		if (blockState.getBlock() instanceof DropExperienceBlock dropExp)
		{
			IntProvider xpRange = ((DropExperienceBlockAccessor) dropExp).xpRange();
			int exp = EnchantmentHelper.processBlockExperience((ServerLevel) level, itemStack, xpRange.sample(level.getRandom()));
			player.giveExperiencePoints(exp);
			level.playSound(
				null,
				blockPos,
				SoundEvents.EXPERIENCE_ORB_PICKUP,
				SoundSource.PLAYERS,
				0.1F,
				(level.random.nextFloat() - level.random.nextFloat()) * 0.35F + 0.9F
			);
		}
	}
}
