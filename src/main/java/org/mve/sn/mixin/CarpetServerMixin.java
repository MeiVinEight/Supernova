package org.mve.sn.mixin;

import carpet.CarpetServer;
import carpet.script.CarpetScriptServer;
import carpet.script.external.Vanilla;
import net.minecraft.server.MinecraftServer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CarpetServer.class)
public class CarpetServerMixin
{
	@Redirect(
		method = "onServerClosed",
		at = @At(
			value = "INVOKE",
			target = "Lcarpet/script/external/Vanilla;MinecraftServer_getScriptServer(Lnet/minecraft/server/MinecraftServer;)Lcarpet/script/CarpetScriptServer;",
			ordinal = 0
		)
	)
	private static CarpetScriptServer onServerClosed$MinecraftServer_getScriptServer(MinecraftServer server)
	{
		if (server == null)
			return null;
		return Vanilla.MinecraftServer_getScriptServer(server);
	}

	@Redirect(
		method = "onServerClosed",
		at = @At(
			value = "FIELD",
			target = "Lcarpet/script/CarpetScriptServer;stopAll:Z",
			opcode = Opcodes.GETFIELD,
			ordinal = 0
		)
	)
	private static boolean onServerClosed$CarpetScriptServer$stopAll(CarpetScriptServer instance)
	{
		if (instance == null)
			return true;
		return instance.stopAll;
	}
}
