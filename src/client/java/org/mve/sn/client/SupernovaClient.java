package org.mve.sn.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class SupernovaClient implements ClientModInitializer
{
	public static final KeyMapping CLIMBING = new KeyMapping(
		"key.supernova.climbing",
		InputConstants.Type.KEYSYM,
		GLFW.GLFW_KEY_C,
		"key.category.supernova"
	);

	@Override
	public void onInitializeClient()
	{
		KeyBindingHelper.registerKeyBinding(CLIMBING);
	}
}
