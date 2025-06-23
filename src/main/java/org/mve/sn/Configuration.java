package org.mve.sn;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.mve.sn.screen.ConfigMenu;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Configuration
{
	public static final ForgeConfigSpec.BooleanValue REPAIR_COST;
	public static final ForgeConfigSpec.BooleanValue ENCHANTMENT_COMPATIBILITY;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_BLASTPROTECTION;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_FEATHERFALLING;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_FIREPROTECTION;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_FORTUNE;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_LOOTING;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_LOYALTY;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_LUCKOFTHESEA;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_POWER;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_PROJECTILEPROTECTION;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_PROTECTION;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_SHARPNESS;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_SWEEPING;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_UNBREAKING;
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

	@SubscribeEvent
	public static void onConfig(ModConfigEvent event)
	{
		Configuration.check();
	}

	public static void check()
	{
		for (String id : ENTITY_EXPLOSION.get())
		{
			if (!validateEntity(id))
				Supernova.LOGGER.warn("Unknown entity ID: {}", id);
		}
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
		MAX_LEVEL_BLASTPROTECTION = builder
			.comment("Max level of blast protection")
			.defineInRange("MAX_LEVEL_BLASTPROTECTION", 4, 1, 255);
		MAX_LEVEL_FEATHERFALLING = builder
			.comment("Max level of feather falling")
			.defineInRange("MAX_LEVEL_FEATHERFALLING", 4, 1, 255);
		MAX_LEVEL_FIREPROTECTION = builder
			.comment("Max level of fire protection")
			.defineInRange("MAX_LEVEL_FIREPROTECTION", 4, 1, 255);
		MAX_LEVEL_FORTUNE = builder
			.comment("Max level of fortune")
			.defineInRange("MAX_LEVEL_FORTUNE", 3, 1, 255);
		MAX_LEVEL_LOOTING = builder
			.comment("Max level of looting")
			.defineInRange("MAX_LEVEL_LOOTING", 3, 1, 255);
		MAX_LEVEL_LOYALTY = builder
			.comment("Max level of loyalty")
			.defineInRange("MAX_LEVEL_LOYALTY", 3, 1, 255);
		MAX_LEVEL_LUCKOFTHESEA = builder
			.comment("Max level of luck of the sea")
			.defineInRange("MAX_LEVEL_LUCKOFTHESEA", 3, 1, 255);
		MAX_LEVEL_POWER = builder
			.comment("Max level of power")
			.defineInRange("MAX_LEVEL_POWER", 5, 1, 255);
		MAX_LEVEL_PROJECTILEPROTECTION = builder
			.comment("Max level of projectile protection")
			.defineInRange("MAX_LEVEL_PROJECTILEPROTECTION", 4, 1, 255);
		MAX_LEVEL_PROTECTION = builder
			.comment("Max level of protection")
			.defineInRange("MAX_LEVEL_PROTECTION", 4, 1, 255);
		MAX_LEVEL_SHARPNESS = builder
			.comment("Max level of sharpness")
			.defineInRange("MAX_LEVEL_SHARPNESS", 5, 1, 255);
		MAX_LEVEL_SWEEPING = builder
			.comment("Max level of sweeping")
			.defineInRange("MAX_LEVEL_SWEEPING", 3, 1, 255);
		MAX_LEVEL_UNBREAKING = builder
			.comment("Max level of unbreaking")
			.defineInRange("MAX_LEVEL_UNBREAKING", 3, 1, 255);
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
