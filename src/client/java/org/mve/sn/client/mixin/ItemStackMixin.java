package org.mve.sn.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

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
		Minecraft instance = Minecraft.getInstance();
		int width = instance.getWindow().getGuiScaledWidth();
		width -= (int) instance.getWindow().getGuiScale() * 4;
		List<FormattedText> list = instance.font.getSplitter().splitLines(component, width, Style.EMPTY);
		for (FormattedText formattedText : list)
		{
			MutableComponent mutable = Component.empty();
			formattedText.visit((a, b) ->
			{
				MutableComponent block = Component.literal(b);
				if (!a.isEmpty()) block.withStyle(a);
				mutable.append(block);
				return Optional.empty();
			}, Style.EMPTY);
			cir.getReturnValue().add(mutable);
		}
	}
}
