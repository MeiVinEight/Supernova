package org.mve.sn.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin
{
	@Inject(at = @At("RETURN"), method = "calculateIncreasedRepairCost", cancellable = true)
	private static void calculateIncreasedRepairCost(int cost, CallbackInfoReturnable<Integer> cir)
	{
		if (!Configuration.REPAIR_COST.get())
			cir.setReturnValue(0);
	}
}
