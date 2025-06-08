package org.mve.sn;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Supernova.MODID)
public class Supernova
{
	public static final String MODID = "supernova";
	// Directly reference a slf4j logger
	private static final Logger LOGGER = LogUtils.getLogger();

	public Supernova(FMLJavaModLoadingContext context)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event)
	{
		// Do something when the server starts
		LOGGER.info("Supernova starting");
	}
}
