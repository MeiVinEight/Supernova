package org.mve.sn.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.mve.sn.Supernova;

public class SupernovaClient implements ClientModInitializer
{
	public static final KeyMapping CLIMBING = new KeyMapping(
		"key.supernova.climbing",
		InputConstants.Type.KEYSYM,
		GLFW.GLFW_KEY_C,
		new KeyMapping.Category(Identifier.fromNamespaceAndPath(Supernova.SUPERNOVA, "key.category.supernova"))
	);

	@Override
	public void onInitializeClient()
	{
		KeyMappingHelper.registerKeyMapping(CLIMBING);
	}
}
