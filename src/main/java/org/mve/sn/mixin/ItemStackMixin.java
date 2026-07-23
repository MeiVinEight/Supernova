package org.mve.sn.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
}
