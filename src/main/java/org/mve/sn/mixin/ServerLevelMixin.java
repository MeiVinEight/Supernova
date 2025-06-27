package org.mve.sn.mixin;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin
{
	@Inject(
		at = @At("HEAD"),
		method = "playSeededSound("+
			"Lnet/minecraft/world/entity/player/Player;"+
			"D"+
			"D"+
			"D"+
			"Lnet/minecraft/core/Holder;"+
			"Lnet/minecraft/sounds/SoundSource;"+
			"F"+
			"F"+
			"J"+
			")V",
		cancellable = true
	)
	public void playSeededSound(
		Player p_263330_,
		double p_263393_,
		double p_263369_,
		double p_263354_,
		Holder<SoundEvent> p_263412_,
		SoundSource p_263338_,
		float p_263352_,
		float p_263390_,
		long p_263403_,
		CallbackInfo ci
	)
	{
		if (p_263412_.value() == null)
			ci.cancel();
	}
}
