package org.mve.sn.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.mve.sn.KeyboardEventConsumer;
import org.mve.sn.KeyboardHandler;
import org.mve.sn.network.ServerboundKeyboardEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements KeyboardEventConsumer
{
	@Override
	public void onKeyboardEvent(ServerboundKeyboardEvent event)
	{
		((KeyboardHandler) this).keyUp(event.up);
	}
}
