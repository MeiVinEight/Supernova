package org.mve.sn.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.mve.sn.world.inventory.IAbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin
{
	@Inject(
		method = "place",
		at = @At("HEAD"),
		cancellable = true
	)
	private void place$HEAD(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<InteractionResult> cir)
	{
		Player player = blockPlaceContext.getPlayer();
		if (player.level().isClientSide())
			return;
		if ((player.containerMenu instanceof IAbstractContainerMenu iam) && (iam.supernova$lockHotbar() == player.getInventory().selected))
			cir.setReturnValue(InteractionResult.FAIL);
	}
}
