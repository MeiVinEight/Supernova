package org.mve.sn;

import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Supernova implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("Supernova");
	public static final String SUPERNOVA = "supernova";
	public static final String ATTR_NAME_GLIDABLE = "glidable";
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

	public static boolean gliding(Level level, ItemStack item)
	{
		Holder<Enchantment> gliding = level
			.registryAccess()
			.lookupOrThrow(Registries.ENCHANTMENT)
			.get(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("minecraft", "gliding")))
			.orElse(null);
		if (gliding == null) return false;
		return item.getEnchantments().getLevel(gliding) > 0;
	}
}
