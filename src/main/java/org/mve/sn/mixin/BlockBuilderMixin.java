package org.mve.sn.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.tterrag.registrate.builders.BlockBuilder")
public class BlockBuilderMixin
{
	@Inject(
		method = "createEntry()Lnet/minecraft/world/level/block/Block;",
		at = @At(
			value = "INVOKE",
			target = "Lcom/tterrag/registrate/util/nullness/NonNullFunction;apply(Ljava/lang/Object;)Ljava/lang/Object;",
			ordinal = 0
		)
	)
	private void createEntry$apply$0(CallbackInfoReturnable<Block> cir, @Local(ordinal = 0) BlockBehaviour.Properties properties)
	{
		((BlockBehaviour$PropertiesMixin1) properties).setDrops(null);
	}
}
