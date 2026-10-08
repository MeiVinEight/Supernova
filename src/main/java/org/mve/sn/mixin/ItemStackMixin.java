package org.mve.sn.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.NonInteractiveResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.mve.sn.Supernova;
import org.mve.sn.world.ShulkerBox;
import org.mve.sn.world.inventory.IAbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin
{
	@ModifyVariable(at = @At("HEAD"), method = "set", index = 2, argsOnly = true)
	private Object set(Object value, @Local(argsOnly = true, index = 1) DataComponentType<?> type)
	{
		if (type == DataComponents.REPAIR_COST) return 0;
		//if (type == DataComponents.DAMAGE && Items.ELYTRA.equals(_this.getItem())) return 0;
		return value;
	}

	@Inject(
		method = "use",
		at = @At("HEAD")
	)
	private void use$HEAD(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir)
	{
		if (player.level().isClientSide())
			return;
		ItemStack that = (ItemStack) (Object) this;
		if (!Supernova.SHULKER_BOX_ITEM.contains(that.getItem()))
			return;
		if (interactionHand != InteractionHand.MAIN_HAND)
			return;
		if (that.getCount() > 1)
		{
			ItemStack copy = that.copyWithCount(that.getCount() - 1);
			that.setCount(1);
			CustomData data = that.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
			CompoundTag tag = data.copyTag();
			tag.put("supernova", StringTag.valueOf("unstackable_shulker_box"));
			that.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
			player.setItemInHand(interactionHand, that);
			player.getInventory().placeItemBackInInventory(copy);
			if (data.isEmpty())
				that.remove(DataComponents.CUSTOM_DATA);
			else
				that.set(DataComponents.CUSTOM_DATA, data);
			player.setItemInHand(interactionHand, that);
		}
		ShulkerBox box = new ShulkerBox(that.copy());
		int selectedHotbar = player.getInventory().selected;

		//player.setItemInHand(interactionHand, ItemStack.EMPTY);
		box.onClose = (player1) -> player1.getInventory().setItem(selectedHotbar, box.item);
		box.onChange = (player1) -> player1.getInventory().setItem(selectedHotbar, box.item);
		box.onCreate = (menu) ->
		{
			((IAbstractContainerMenu) menu).supernova$lockHotbar(selectedHotbar);
			int slotId = selectedHotbar + 54;
			Slot slot = menu.getSlot(slotId);
			NonInteractiveResultSlot slot1 = new NonInteractiveResultSlot(slot.container, slot.getContainerSlot(), slot.x, slot.y);
			slot1.index = slot.index;
			menu.slots.set(slotId, slot1);
			return menu;
		};
		box.openMenu((ServerPlayer) player);
	}
}
