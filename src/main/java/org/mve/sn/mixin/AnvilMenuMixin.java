package org.mve.sn.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin
{
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
