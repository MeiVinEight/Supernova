package org.mve.sn.client.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.mve.sn.KeyboardHandler;
import org.mve.sn.network.ServerboundKeyboardEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin
{
	@Shadow
	public Input input;
	@Unique
	public boolean clientKeyUp = false;

	@Inject(
		method = "aiStep",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/Input;tick(ZF)V",
			ordinal = 0,
			shift = At.Shift.AFTER
		)
	)
	public void aiStep0(CallbackInfo ci)
	{
		if (this.clientKeyUp != this.input.up)
		{
			ClientPlayNetworking.send(new ServerboundKeyboardEvent(this.input.up));
			((KeyboardHandler) this).keyUp(this.input.up);
		}
		this.clientKeyUp = this.input.up;
	}
}
