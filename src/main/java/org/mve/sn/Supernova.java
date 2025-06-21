package org.mve.sn;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Supernova.SUPERNOVA)
public class Supernova
{
	public static final String SUPERNOVA = "supernova";
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LoggerFactory.getLogger("Supernova");
	public static final ConfigScreenHandler.ConfigScreenFactory CONFIG_SCREEN_FACTORY = new ConfigScreenHandler.ConfigScreenFactory(Configuration::screen);

	public Supernova(FMLJavaModLoadingContext context)
	{
		MinecraftForge.EVENT_BUS.register(this);
		context.registerConfig(ModConfig.Type.COMMON, Configuration.SPECIFICATION);
		context.registerExtensionPoint(CONFIG_SCREEN_FACTORY.getClass(), () -> CONFIG_SCREEN_FACTORY);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event)
	{
		// Do something when the server starts
		LOGGER.info("Supernova starting");
	}
}
