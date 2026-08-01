package org.mve.sn.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu
{
	@Shadow
	@Final
	private DataSlot cost;

	@Shadow
	public abstract void createResult();

	public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess)
	{
		super(menuType, i, inventory, containerLevelAccess);
	}

	@Inject(at = @At("RETURN"), method = "calculateIncreasedRepairCost", cancellable = true)
	private static void getNextCost(int cost, CallbackInfoReturnable<Integer> cir)
	{
		cir.setReturnValue(0);
	}

	@Redirect(
		method = "onTake",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Player;giveExperienceLevels(I)V"
		)
	)
	public void onTake$giveExperienceLevels(Player instance, int i)
	{
		instance.giveExperiencePoints((int) -experience(-i));
	}

	@Inject(
		method = "createResult",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V",
			ordinal = 2
		),
		cancellable = true
	)
	private void createResult$setItem0(CallbackInfo ci)
	{
		ItemStack item0 = this.inputSlots.getItem(0);
		ItemStack item1 = this.inputSlots.getItem(1);
		if (item0.is(Items.BOOK) && item1.is(Items.END_CRYSTAL))
		{
			ci.cancel();
			ItemStack result = Items.ENCHANTED_BOOK.getDefaultInstance();
			CustomData data = result.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
				.update(t -> t.putInt(Supernova.TAG_SUPERNOVA_FLYING, 1));
			result.set(DataComponents.CUSTOM_DATA, data);
			this.resultSlots.setItem(0, result);
			return;
		}
		if (item0.is(Items.ENCHANTED_BOOK))
		{
			CustomData data = item0.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
			CompoundTag tag = data.copyTag();
			if (!tag.contains(Supernova.TAG_SUPERNOVA_FLYING)) return;
			int progress = tag.getInt(Supernova.TAG_SUPERNOVA_FLYING);
			if (progress >= Supernova.SUPERNOVA_FLYING_PROGRESS.length) return;
			if (!item1.is(Supernova.SUPERNOVA_FLYING_PROGRESS[progress].getItem())) return;
			ItemStack result = Items.ENCHANTED_BOOK.getDefaultInstance();
			if (progress < 3)
			{
				data = data.update(t -> t.putInt(Supernova.TAG_SUPERNOVA_FLYING, progress + 1));
				result.set(DataComponents.CUSTOM_DATA, data);
			}
			else
			{
				Holder<Enchantment> enchantment = this.player
					.level()
					.registryAccess()
					.registryOrThrow(Registries.ENCHANTMENT)
					.getHolder(ResourceLocation.fromNamespaceAndPath("supernova", "flying"))
					.orElse(null);
				if (enchantment == null) return;
				result.enchant(enchantment, 1);
				this.cost.set(30);
			}
			ci.cancel();
			this.resultSlots.setItem(0, result);
		}
	}

	@ModifyArg(
		method = "onTake",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
			ordinal = 0
		),
		index = 1
	)
	private ItemStack onTake$setItem0(ItemStack itemStack)
	{
		itemStack = this.inputSlots.getItem(0);
		itemStack.shrink(1);
		return itemStack;
	}

	@ModifyArg(
		method = "onTake",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
			ordinal = 3
		),
		index = 1
	)
	private ItemStack onTake$setItem3(ItemStack itemStack)
	{
		itemStack = this.inputSlots.getItem(1);
		itemStack.shrink(1);
		return itemStack;
	}

	@Unique
	private static long experience(int lvl)
	{
		if (lvl < 1)
			return 0;
		if (lvl <= 16)
			return (lvl + 6) * lvl;
		if (lvl <= 31)
			return ((((5L * lvl) - 81) * lvl) >> 1) + 360;
		return ((((9L * lvl) - 325) * lvl) >> 1) + 2220;
	}
}
