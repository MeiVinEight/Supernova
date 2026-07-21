package org.mve.sn.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.mve.sn.GrindstoneMenuAccessor;
import org.mve.sn.SupernovaMenu;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin extends AbstractContainerMenu implements SupernovaMenu, GrindstoneMenuAccessor
{
	@Shadow
	@Final
	Container repairSlots;

	@Unique
	private int supernova = 0;
	@Unique
	private int maxCost = 0;

	public GrindstoneMenuMixin(@Nullable MenuType<?> menuType, int i)
	{
		super(menuType, i);
	}

	@Shadow
	protected abstract ItemStack removeNonCursesFrom(ItemStack itemStack);

	@Shadow
	protected abstract ItemStack mergeItems(ItemStack itemStack, ItemStack itemStack2);

	@Shadow
	protected abstract void createResult();

	@Override
	public void supernova(int v)
	{
		this.supernova = v;
	}

	@Override
	public int supernova()
	{
		return this.supernova;
	}

	@Override
	public Container repairSlots()
	{
		return this.repairSlots;
	}

	@Override
	public int maxCost()
	{
		return this.maxCost;
	}

	@Override
	public ItemStack removeNonCursesFrom0(ItemStack stack)
	{
		return this.removeNonCursesFrom(stack);
	}

	@Override
	public void createResult0()
	{
		this.createResult();
	}

	@Inject(
		method = "computeResult",
		at = @At("HEAD")
	)
	public void computeResult0(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<ItemStack> cir)
	{
		this.supernova = 0;
		this.maxCost = 0;
	}

	@Inject(
		method = "computeResult",
		at = @At(
			value = "FIELD",
			ordinal = 1,
			target = "Lnet/minecraft/world/item/ItemStack;EMPTY:Lnet/minecraft/world/item/ItemStack;",
			opcode = Opcodes.GETSTATIC
		),
		cancellable = true
	)
	public void computeResult1(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<ItemStack> cir)
	{
		cir.setReturnValue(this.mergeItems(itemStack, itemStack2));
	}

	@Inject(
		method = "mergeItems",
		at = @At("HEAD"),
		cancellable = true
	)
	public void mergeItems0(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<ItemStack> cir)
	{
		if (!itemStack2.is(Items.BOOK)) return;
		int count = 0;
		int cost = 0;
		ItemStack result = Items.ENCHANTED_BOOK.getDefaultInstance();
		Set<Object2IntMap.Entry<Holder<Enchantment>>> entries = itemStack.getOrDefault(GrindstoneMenuAccessor.getComponentType(itemStack), ItemEnchantments.EMPTY).entrySet();
		for (Object2IntMap.Entry<Holder<Enchantment>> entry : entries)
		{
			Holder<Enchantment> holder = entry.getKey();
			if (holder.is(EnchantmentTags.CURSE)) continue;

			int lvl = entry.getIntValue();
			result.enchant(holder, lvl);
			count++;
			cost += holder.value().getMaxCost(0);
			if (itemStack.is(Items.ENCHANTED_BOOK)) break;
		}
		if (count > 0)
		{
			this.supernova = 1;
			this.maxCost = cost;
			cir.setReturnValue(result);
		}
	}

	@Inject(
		method = "quickMoveStack",
		at = @At("HEAD"),
		cancellable = true
	)
	public void quickMoveStack$getItem0(Player player, int i, CallbackInfoReturnable<ItemStack> cir)
	{
		if (i != 2) return;
		if (this.maxCost > player.totalExperience) cir.setReturnValue(ItemStack.EMPTY);
	}
}
