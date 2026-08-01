package org.mve.sn.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
	@Unique
	private static final ItemStack[] FLYING_UPGRADE_PROGRESS = {
		Items.END_CRYSTAL.getDefaultInstance(),
		Items.DRAGON_HEAD.getDefaultInstance(),
		Items.NETHER_STAR.getDefaultInstance(),
		Items.DRAGON_EGG.getDefaultInstance()
	};

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

	@Inject(
		method = "getTooltipLines",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
			ordinal = 0,
			shift = At.Shift.AFTER
		)
	)
	private void getTooltipLines$set0(
		Item.TooltipContext tooltipContext,
		@Nullable Player player,
		TooltipFlag tooltipFlag,
		CallbackInfoReturnable<List<Component>> cir,
		@Local(ordinal = 0) List<Component> list
	)
	{
		ItemStack _this = (ItemStack) (Object) this;
		CustomData data = _this.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
		CompoundTag tag = data.copyTag();
		if (!tag.contains(Supernova.TAG_SUPERNOVA_FLYING)) return;
		int progress = tag.getInt(Supernova.TAG_SUPERNOVA_FLYING);
		if (progress >= FLYING_UPGRADE_PROGRESS.length) return;
		MutableComponent component = Component.translatable("supernova.flying.tooltip.next");
		ItemStack next = FLYING_UPGRADE_PROGRESS[progress];
		component.append(next.getHoverName())
			.withStyle(next.getRarity().color());
		list.add(component);
	}
}
