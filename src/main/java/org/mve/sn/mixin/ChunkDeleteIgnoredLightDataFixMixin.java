package org.mve.sn.mixin;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import net.minecraft.util.datafix.fixes.ChunkDeleteIgnoredLightDataFix;
import net.minecraft.util.datafix.fixes.References;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(ChunkDeleteIgnoredLightDataFix.class)
public abstract class ChunkDeleteIgnoredLightDataFixMixin extends DataFix
{
	public ChunkDeleteIgnoredLightDataFixMixin(Schema outputSchema, boolean changesType)
	{
		super(outputSchema, changesType);
	}

	@Inject(
		method = "makeRule",
		at = @At("RETURN"),
		cancellable = true
	)
	private void fix(CallbackInfoReturnable<TypeRewriteRule> cir)
	{
		Type<?> chunkType = this.getInputSchema().getType(References.CHUNK);
		cir.setReturnValue(this.fixTypeEverywhereTyped("ChunkDeleteIgnoredLightDataFix", chunkType, Function.identity()));
	}
}
