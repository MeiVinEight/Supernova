package org.mve.sn.mixin;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.fixes.ChunkDeleteLightFix;
import net.minecraft.util.datafix.fixes.References;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(ChunkDeleteLightFix.class)
public abstract class ChunkDeleteLightFixMixin extends DataFix
{
	public ChunkDeleteLightFixMixin(Schema outputSchema, boolean changesType)
	{
		super(outputSchema, changesType);
	}

	@Inject(
		method = "makeRule",
		at = @At("RETURN"),
		cancellable = true
	)
	public void makeRule(CallbackInfoReturnable<TypeRewriteRule> cir)
	{
		TypeRewriteRule rule = this.fixTypeEverywhereTyped(
			"ChunkDeleteLightFix for " + this.getOutputSchema().getVersionKey(),
			this.getInputSchema().getType(References.CHUNK),
			Function.identity()
		);
		cir.setReturnValue(rule);
	}
}
