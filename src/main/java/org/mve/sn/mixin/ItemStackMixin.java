package org.mve.sn.mixin;

import net.minecraft.world.item.ItemStack;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
	@ModifyVariable(method = "setRepairCost", at = @At("HEAD"), argsOnly = true, index = 1)
	public int setRepairCost(int value)
	{
		return Configuration.REPAIR_COST.get() ? value : 0;
	}
}
