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
import net.neoforged.fml.config.ModConfig;
import org.mve.sn.network.ServerboundKeyboardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Supernova implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("Supernova");
	public static final String SUPERNOVA = "supernova";
	public static final String ATTR_NAME_GLIDABLE = "glidable";
	public static final ResourceLocation RESOURCE_GLIDABLE = ResourceLocation.fromNamespaceAndPath(Supernova.SUPERNOVA, Supernova.ATTR_NAME_GLIDABLE);
	public static final int SUPERNOVA_ENDERBOW = 0;
	public static final Holder<Attribute> ATRIBUTE_GLIDABLE = Registry.registerForHolder(
		BuiltInRegistries.ATTRIBUTE,
		RESOURCE_GLIDABLE,
		new RangedAttribute("attribute." + RESOURCE_GLIDABLE.toLanguageKey(), 0, 0, 1)
			.setSyncable(true)
	);


	@Override
	public void onInitialize()
	{
		ForgeConfigRegistry.INSTANCE.register(Supernova.SUPERNOVA, ModConfig.Type.COMMON, Configuration.SPECIFICATION);
		PayloadTypeRegistry.playC2S().register(ServerboundKeyboardEvent.TYPE, ServerboundKeyboardEvent.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(ServerboundKeyboardEvent.TYPE, (payload, context) -> ((KeyboardEventConsumer) context.player()).onKeyboardEvent(payload));
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
