package org.mve.sn.mixin;

import me.senseiwells.replay.chunk.ChunkRecorder;
import me.senseiwells.replay.recorder.ReplayRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "me.senseiwells.replay.compat.voicechat.ReplayVoicechatPlugin")
public class ReplayVoicechatPluginMixin
{
	@Inject(
		method = "recordAdditionalPackets",
		at = @At("HEAD"),
		cancellable = true
	)
	private void recordAdditionalPackets$HEAD(ReplayRecorder recorder, CallbackInfo ci)
	{
		ci.cancel();
	}

	@Inject(
		method = "onChunkReplayStart",
		at = @At("HEAD"),
		cancellable = true
	)
	private void onChunkReplayStart$HEAD(ChunkRecorder recorder, CallbackInfo ci)
	{
		ci.cancel();
	}
}
