package org.mve.sn.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.mve.sn.world.inventory.IAbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin implements IAbstractContainerMenu
{
	@Unique
	private int supernova$lockHotbar = -1;

	@Inject(
		method = "doClick",
		at = @At("HEAD"),
		cancellable = true
	)
	private void doClick$HEAD(int i, int j, ClickType clickType, Player player, CallbackInfo ci)
	{
		if (clickType == ClickType.SWAP && j == this.supernova$lockHotbar)
			ci.cancel();
	}

	@Override
	public void supernova$lockHotbar(int j)
	{
		this.supernova$lockHotbar = j;
	}
}
