package org.mve.sn.mixin;

import net.minecraft.util.StringUtil;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StringUtil.class)
public class StringUtilMixin
{
	@Inject(method = "isAllowedChatCharacter(C)Z", at = @At("RETURN"), cancellable = true)
	private static void isAllowedChatCharacter(char ch, CallbackInfoReturnable<Boolean> cir)
	{
		if (Configuration.ESCAPE_ALLOWED.get())
			cir.setReturnValue(ch >= ' ' && ch != 127);
	}
}
