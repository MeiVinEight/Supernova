package org.mve.sn;

import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.config.ModConfig;
import org.mve.sn.network.ServerboundKeyboardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class Supernova implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("Supernova");
	public static final String TAG_SUPERNOVA_FLYING = "supernova:flying";
	public static final String SUPERNOVA = "supernova";
	public static final String ATTR_NAME_GLIDABLE = "glidable";
	public static final String ATTR_NAME_FLYING = "flying";
	public static final ResourceLocation RESOURCE_GLIDABLE = ResourceLocation.fromNamespaceAndPath(Supernova.SUPERNOVA, Supernova.ATTR_NAME_GLIDABLE);
	public static final ResourceLocation RESOURCE_FLYING = ResourceLocation.fromNamespaceAndPath(Supernova.SUPERNOVA, Supernova.ATTR_NAME_FLYING);
	public static final int SUPERNOVA_ENDERBOW = 0;
	public static final Holder<Attribute> ATRIBUTE_GLIDABLE = Registry.registerForHolder(
		BuiltInRegistries.ATTRIBUTE,
		RESOURCE_GLIDABLE,
		new RangedAttribute("attribute." + RESOURCE_GLIDABLE.toLanguageKey(), 0, 0, 1)
			.setSyncable(true)
	);
	public static final Holder<Attribute> ATTRIBUTE_FLYING = Registry.registerForHolder(
		BuiltInRegistries.ATTRIBUTE,
		RESOURCE_FLYING,
		new RangedAttribute("attribute." + RESOURCE_FLYING.toLanguageKey(), 0, 0, 1)
			.setSyncable(true)
	);
	public static final ItemStack[] SUPERNOVA_FLYING_PROGRESS = {null, null, null, null};
	public static final Set<Item> SHULKER_BOX_ITEM = new HashSet<>();

	@Override
	public void onInitialize()
	{
		ForgeConfigRegistry.INSTANCE.register(Supernova.SUPERNOVA, ModConfig.Type.COMMON, Configuration.SPECIFICATION);
		PayloadTypeRegistry.playC2S().register(ServerboundKeyboardEvent.TYPE, ServerboundKeyboardEvent.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(
			ServerboundKeyboardEvent.TYPE,
			(payload, context) -> ((KeyboardEventConsumer) context.player()).onKeyboardEvent(payload)
		);

		SUPERNOVA_FLYING_PROGRESS[0] = Items.END_CRYSTAL.getDefaultInstance();
		SUPERNOVA_FLYING_PROGRESS[1] = Items.DRAGON_HEAD.getDefaultInstance();
		SUPERNOVA_FLYING_PROGRESS[2] = Items.NETHER_STAR.getDefaultInstance();
		SUPERNOVA_FLYING_PROGRESS[3] = Items.DRAGON_EGG.getDefaultInstance();

		SHULKER_BOX_ITEM.addAll(Set.of(
			Items.SHULKER_BOX,
			Items.BLACK_SHULKER_BOX,
			Items.BLUE_SHULKER_BOX,
			Items.BROWN_SHULKER_BOX,
			Items.CYAN_SHULKER_BOX,
			Items.GRAY_SHULKER_BOX,
			Items.GREEN_SHULKER_BOX,
			Items.LIGHT_BLUE_SHULKER_BOX,
			Items.LIGHT_GRAY_SHULKER_BOX,
			Items.LIME_SHULKER_BOX,
			Items.MAGENTA_SHULKER_BOX,
			Items.ORANGE_SHULKER_BOX,
			Items.PINK_SHULKER_BOX,
			Items.PURPLE_SHULKER_BOX,
			Items.RED_SHULKER_BOX,
			Items.WHITE_SHULKER_BOX,
			Items.YELLOW_SHULKER_BOX
		));
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

	public static boolean glidable(LivingEntity entity)
	{
		AttributeInstance attribute = entity.getAttribute(Supernova.ATRIBUTE_GLIDABLE);
		if (attribute == null) return false;
		return attribute.getValue() > 0;
	}
}
