package org.mve.sn;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Supernova implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("Supernova");
	public static final String SUPERNOVA = "supernova";
	public static final DataComponentType<int[]> DATA_SUPERNOVA = Registry.register(
		BuiltInRegistries.DATA_COMPONENT_TYPE,
		Supernova.SUPERNOVA,
		DataComponentType.<int[]>builder().networkSynchronized(new StreamCodec<>()
		{
			@Override
			public int[] decode(RegistryFriendlyByteBuf object)
			{
				return object.readVarIntArray();
			}

			@Override
			public void encode(RegistryFriendlyByteBuf object, int[] object2)
			{
				object.writeVarIntArray(object2);
			}
		}).cacheEncoding().build()
	);
	public static final int SUPERNOVA_ENDERBOW = 0;


	@Override
	public void onInitialize()
	{
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
