package org.mve.sn.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin
{
	@Inject(
		at = @At("HEAD"),
		method = "dropResources(" +
			"Lnet/minecraft/world/level/block/state/BlockState;" +
			"Lnet/minecraft/world/level/Level;" +
			"Lnet/minecraft/core/BlockPos;" +
			"Lnet/minecraft/world/level/block/entity/BlockEntity;" +
			"Lnet/minecraft/world/entity/Entity;" +
			"Lnet/minecraft/world/item/ItemStack;" +
			"Z" +
			")V",
		remap = false,
		cancellable = true
	)
	private static void dropResources(
		BlockState p_49882_,
		Level p_49883_,
		BlockPos p_49884_,
		BlockEntity p_49885_,
		Entity p_49886_,
		ItemStack p_49887_,
		boolean dropXp,
		CallbackInfo ci
	)
	{
		if (!Configuration.SHIFT_AUTO_PICKUP.get())
			return;
		if (!(p_49883_ instanceof ServerLevel))
			return;
		if (!(p_49886_ instanceof ServerPlayer player))
			return;
		if (!player.isShiftKeyDown())
			return;
		ci.cancel();
		List<ItemStack> drops = Block.getDrops(p_49882_, (ServerLevel) p_49883_, p_49884_, p_49885_, p_49886_, p_49887_);
		for (ItemStack drop : drops)
		{
			ItemHandlerHelper.giveItemToPlayer(player, drop);
		}
		p_49882_.spawnAfterBreak((ServerLevel) p_49883_, p_49884_, p_49887_, false);
	}
}
