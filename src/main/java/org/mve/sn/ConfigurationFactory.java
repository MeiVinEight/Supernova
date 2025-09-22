package org.mve.sn;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.mve.sn.screen.ConfigMenu;

import java.util.function.BiFunction;

public class ConfigurationFactory implements BiFunction<Minecraft, Screen, Screen>
{
	@Override
	public Screen apply(Minecraft minecraft, Screen screen)
	{
		ConfigMenu configScreen = new ConfigMenu(screen);
		minecraft.pushGuiLayer(configScreen);
		return configScreen;
	}
}
