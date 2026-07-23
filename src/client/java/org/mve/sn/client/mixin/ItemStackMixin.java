package org.mve.sn.client.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
	@Inject(
		method = "getTooltipLines",
		at = @At("RETURN")
	)
	private void getTooltip0(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir)
	{
		if (!player.level().isClientSide) return;
		if (!Screen.hasShiftDown()) return;
		ItemStack item = (ItemStack) (Object) this;
		Tag tag = item.save(player.level().registryAccess());
		Component component = NbtUtils.toPrettyComponent(tag);
		cir.getReturnValue().add(component);
	}
}
