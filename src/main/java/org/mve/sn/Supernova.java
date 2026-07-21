package org.mve.sn;

import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Supernova implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("Supernova");
	public static final String SUPERNOVA = "supernova";
	public static final int SUPERNOVA_ENDERBOW = 0;


	@Override
	public void onInitialize()
	{
		ForgeConfigRegistry.INSTANCE.register(Supernova.SUPERNOVA, ModConfig.Type.COMMON, Configuration.SPECIFICATION);
	}

	public static boolean check(int[] arr, int tag)
	{
		if (arr == null)
			return false;
		for (int j : arr)
			if (j == tag)
				return true;
		return false;
	}

	public static int[] add(int[] arr, int tag)
	{
		int len = 0;
		if (arr != null)
			len = arr.length;
		int[] ret = new int[len + 1];
		if (arr != null)
			System.arraycopy(arr, 0, ret, 0, len);
		ret[len] = tag;
		return ret;
	}
}
