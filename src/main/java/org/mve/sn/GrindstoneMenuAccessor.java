package org.mve.sn;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public interface GrindstoneMenuAccessor
{
	Container repairSlots();
	int maxCost();
	ItemStack removeNonCursesFrom0(ItemStack stack);
	void createResult0();

	public static DataComponentType<ItemEnchantments> getComponentType(ItemStack itemStack)
	{
		return itemStack.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
	}
}
