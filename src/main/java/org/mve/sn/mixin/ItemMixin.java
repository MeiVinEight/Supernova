package org.mve.sn.mixin;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import org.mve.sn.world.item.Stackable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin implements Stackable
{
	@Unique
	private Integer stackable;

	@Inject(
		method = "components",
		at = @At("RETURN"),
		cancellable = true
	)
	private void components$RETURN(CallbackInfoReturnable<DataComponentMap> cir)
	{
		if (this.stackable != null)
			cir.setReturnValue(DataComponentMap.builder().addAll(cir.getReturnValue()).set(DataComponents.MAX_STACK_SIZE, this.stackable).build());
	}

	@Override
	public void stack(Integer size)
	{
		this.stackable = size;
	}
}
