package org.mve.sn;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.mve.Trie;
import org.mve.sn.screen.ConfigMenu;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Configuration
{
	public static final short[] ENTITY_DICTIONARY;
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
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_RIPTIDE;
	public static final ForgeConfigSpec.IntValue MAX_LEVEL_UNBREAKING;
	public static final ForgeConfigSpec.BooleanValue ENDER_BOW;
	public static final ForgeConfigSpec.DoubleValue ENDER_SKELETON_PROBABILITY;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_EXPLOSION;
	public static final ForgeConfigSpec.BooleanValue DIGGER_PICKUP;
	public static final ForgeConfigSpec.BooleanValue KILLER_PICKUP;
	public static final ForgeConfigSpec.BooleanValue SUPER_JUMP;
	public static final ForgeConfigSpec.DoubleValue SUPER_JUMP_SCALE;
	public static final ForgeConfigSpec.BooleanValue INFINITY_FIX;
	public static final ForgeConfigSpec SPECIFICATION;
	public static final Trie ENTITY_ID_TRIE;
	private static boolean ENTITY_ID_TRIE_SETUP = false;

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

	public static void entityID(String id)
	{
		Configuration.ENTITY_ID_TRIE.add(id.getBytes(StandardCharsets.UTF_8));
	}

	public static void setup()
	{
		if (Configuration.ENTITY_ID_TRIE_SETUP)
			return;
		Supernova.LOGGER.info("Constructing entity id trie");
		Configuration.ENTITY_ID_TRIE.clear();
		for (ResourceLocation eid : ForgeRegistries.ENTITY_TYPES.getKeys())
		{
			try
			{
				entityID(eid.toString());
			}
			catch (Throwable e)
			{
				Supernova.LOGGER.warn("Cannot add entity ID: {}", eid, e);
			}
		}
		Configuration.ENTITY_ID_TRIE_SETUP = true;
	}

	public static int searchEntityID(String prefix, byte[] str)
	{
		if (prefix.isEmpty())
		{
			String val = "minecraft:";
			int len = Math.min(str.length, val.length());
			System.arraycopy(val.getBytes(), 0, str, 0, len);
			return len;
		}

		byte[] pfx = prefix.getBytes(StandardCharsets.UTF_8);
		byte[] buf = new byte[str.length];
		int slen = Configuration.ENTITY_ID_TRIE.search(pfx, 0, pfx.length, buf, 0, buf.length);
		if (slen == -1)
			return -1;

		int cidx = -1;
		for (int i = 0; (i < pfx.length) && (cidx == -1); i++)
			if (pfx[i] == ':')
				cidx = i;

		int retVal = 0;
		for (; retVal < Math.min(slen, str.length); retVal++)
		{
			str[retVal] = buf[retVal];
			if (str[retVal] == ':')
			{
				retVal++;
				break;
			}
		}
		if (cidx != -1)
		{
			for (; retVal < Math.min(slen, str.length); retVal++)
				str[retVal] = buf[retVal];
		}
		return retVal;
	}

	static
	{
		ENTITY_DICTIONARY = Trie.DEFAULT_DICTIONARY.clone();
		ENTITY_DICTIONARY[':'] = 26;
		ENTITY_DICTIONARY['_'] = 27;
		for (int i = 0; i < 10; i++)
			ENTITY_DICTIONARY['0' + i] = (short) (28 + i);

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
		MAX_LEVEL_RIPTIDE = builder
			.comment("Max level of trident")
			.defineInRange("MAX_LEVEL_TRIDENT", 3, 1, 255);
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
		DIGGER_PICKUP = builder
			.comment("Auto pickup item after destroy block when holding shift")
			.define("DIGGER_PICKUP", false);
		KILLER_PICKUP = builder
			.comment("Auto pickup entity loots after kill entity")
			.define("KILLER_PICKUP", false);
		SUPER_JUMP = builder
			.comment("Jump higher when holding shift")
			.define("SUPER_JUMP", false);
		SUPER_JUMP_SCALE = builder
			.comment("Y speed scale for super jump")
			.defineInRange("SUPER_JUMP_SCALE", 1.5, 1, Float.MAX_VALUE);
		INFINITY_FIX = builder
			.comment("Removes need to have an arrow in your inventory to use the Infinity enchant on your bow.")
			.define("INFINITY_FIX", false);
		SPECIFICATION = builder.build();

		ENTITY_ID_TRIE = new Trie(ENTITY_DICTIONARY);
	}
}
