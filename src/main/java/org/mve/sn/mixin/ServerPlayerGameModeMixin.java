package org.mve.sn.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.mve.sn.Configuration;
import org.mve.sn.Sounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin
{
	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/Block;popExperience(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;I)V"
		),
		method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
		locals = LocalCapture.CAPTURE_FAILSOFT,
		cancellable = true
	)
	public void destroyBlock(BlockPos p_9281_, CallbackInfoReturnable<Boolean> cir, BlockState blockstate, int exp)
	{
		if (!Configuration.DIGGER_PICKUP.get())
			return;
		ServerPlayerGameMode _this = (ServerPlayerGameMode) (Object) this;
		ServerPlayer player = _this.player;
		if (player.isShiftKeyDown())
		{
			player.giveExperiencePoints(exp);
			RandomSource random = Sounds.RANDOM;
			//_this.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
			SoundEvent sound = SoundEvents.EXPERIENCE_ORB_PICKUP;
			float pitch = (random.nextFloat() - random.nextFloat()) * 0.35F + 0.9F;
			Sounds.play(player, sound, SoundSource.PLAYERS, 0.5F, pitch);
			cir.setReturnValue(true);
		}
	}
}
