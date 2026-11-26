package org.mve.sn.mixin;

import net.minecraft.nbt.StringTag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StringTag.class)
public class StringTagMixin
{
	@Shadow
	@Final
	private static StringTag EMPTY;

	@Inject(
		method = "valueOf",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void valueOf$HEAD(String data, CallbackInfoReturnable<StringTag> cir)
	{
		if (data == null)
			cir.setReturnValue(EMPTY);
	}
}
