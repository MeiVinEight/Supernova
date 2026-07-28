package org.mve.sn;

import org.mve.sn.network.ServerboundKeyboardEvent;

public interface KeyboardEventConsumer
{
	void onKeyboardEvent(ServerboundKeyboardEvent event);
}
