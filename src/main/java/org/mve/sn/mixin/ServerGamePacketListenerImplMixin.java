package org.mve.sn.mixin;

import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.mve.sn.world.inventory.IAbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin
{
	@Shadow
	public ServerPlayer player;

	@Inject(
		method = "handlePlayerAction",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket;getAction()Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;",
			ordinal = 0
		),
		cancellable = true
	)
	private void inject$handlePlayerAction$getAction$0(ServerboundPlayerActionPacket packet, CallbackInfo ci)
	{
		ServerboundPlayerActionPacket.Action action = packet.getAction();
		boolean changeHotbar = action == ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS ||
			action == ServerboundPlayerActionPacket.Action.DROP_ITEM ||
			action == ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND;
		boolean lockHotbar = (this.player.containerMenu instanceof IAbstractContainerMenu iam) &&
			(iam.supernova$lockHotbar() == this.player.getInventory().selected);
		if (changeHotbar && lockHotbar)
			ci.cancel();
	}

	@Inject(
		method = "handlePickItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerPlayer;getInventory()Lnet/minecraft/world/entity/player/Inventory;",
			ordinal = 0
		),
		cancellable = true
	)
	private void inject$handlePickItem$getInventory$0(ServerboundPickItemPacket serverboundPickItemPacket, CallbackInfo ci)
	{
		boolean lockHotbar = (this.player.containerMenu instanceof IAbstractContainerMenu iam) &&
			(iam.supernova$lockHotbar() != -1);
		if (lockHotbar)
			ci.cancel();
	}
}
