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
	public static final ForgeConfigSpec.BooleanValue REPAIR_COST;
	public static final ForgeConfigSpec.BooleanValue ENCHANTMENT_COMPATIBILITY;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_SHARPNESS;
	public static final ForgeConfigSpec.BooleanValue ENDER_BOW;
	public static final ForgeConfigSpec.DoubleValue ENDER_SKELETON_PROBABILITY;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_EXPLOSION;
	public static final ForgeConfigSpec SPECIFICATION;

	public static boolean validateEntity(Object id)
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
		REPAIR_COST = builder
			.comment("Whether or not enable repair cost")
			.define("REPAIR_COST", true);
		ENCHANTMENT_COMPATIBILITY = builder
			.comment("Whether or not widen enchantment compatibility.")
			.define("ENCHANTMENT_COMPATIBILITY", false);
		MAX_LEVEL_SHARPNESS = builder
			.comment("Max level of sharpness")
			.defineInRange("MAX_LEVEL_SHARPNESS", 5, 1, 255);
		ENDER_BOW = builder
			.comment("Whether or not enable ender bow")
			.define("ENDERBOW", false);
		ENDER_SKELETON_PROBABILITY = builder
			.comment("Probability for skeleton become to ender skeleton (with ender bow)")
			.defineInRange("ENDER_SKELETON_PROBABILITY", 0.0625, 0, 1);
		ENTITY_EXPLOSION = builder
			.comment("Entity in list explosion will not destroy blocks")
			.defineListAllowEmpty("ENTITY_EXPLOSION", List.of("minecraft:creeper"), o -> true);
		SPECIFICATION = builder.build();
	}
}
