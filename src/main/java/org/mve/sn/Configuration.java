package org.mve.sn;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import org.mve.sn.screen.ConfigMenu;

import java.util.List;

public class Configuration
{
	public static final ForgeConfigSpec.BooleanValue ENCHANTMENT_COMPATIBILITY;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_EXPLOSION;
	public static final ForgeConfigSpec SPECIFICATION;

	private static boolean validateEntity(Object id)
	{
		if (!(id instanceof String))
			return false;
		return ForgeRegistries.ENTITY_TYPES.containsKey(ResourceLocation.tryParse((String) id));
	}

	public static Screen screen(Minecraft mc, Screen screen)
	{
		ConfigMenu configScreen = new ConfigMenu(screen);
		mc.pushGuiLayer(configScreen);
		return configScreen;
	}

	static
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		ENCHANTMENT_COMPATIBILITY = builder
			.comment("Whether or not widen enchantment compatibility.")
			.define("enchantmentCompatibility", false);
		ENTITY_EXPLOSION = builder
			.comment("Entity in list explosion will not destroy blocks")
			.defineListAllowEmpty("entityExplosion", List.of("minecraft:creeper"), Configuration::validateEntity);
		SPECIFICATION = builder.build();
	}
}
