package org.mve.sn.mixin;

import net.minecraft.world.entity.ai.gossip.GossipType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GossipType.class)
public class GossipTypeMixin
{
	@ModifyArg(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/gossip/GossipType;<init>(Ljava/lang/String;ILjava/lang/String;IIII)V",
			ordinal = 3
		),
		index = 4
	)
	private static int clinit$init0(int j)
	{
		return Integer.MAX_VALUE;
	}

	@ModifyArg(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/gossip/GossipType;<init>(Ljava/lang/String;ILjava/lang/String;IIII)V",
			ordinal = 3
		),
		index = 6
	)
	private static int clinit$init1(int j)
	{
		return 0;
	}
}
