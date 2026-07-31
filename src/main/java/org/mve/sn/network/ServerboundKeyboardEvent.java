package org.mve.sn.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.mve.sn.Supernova;

public class ServerboundKeyboardEvent implements CustomPacketPayload
{
	public static final String PACKET_NAME = "keyboard_event";
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Supernova.SUPERNOVA, PACKET_NAME);
	public static final CustomPacketPayload.Type<ServerboundKeyboardEvent> TYPE = new Type<>(ID);
	public static final StreamCodec<ByteBuf, Boolean> BOOLEAN_STREAM_CODEC = new StreamCodec<>()
	{
		@Override
		public Boolean decode(ByteBuf object)
		{
			return object.readBoolean();
		}

		@Override
		public void encode(ByteBuf object, Boolean object2)
		{
			object.writeBoolean(object2);
		}
	};
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundKeyboardEvent> CODEC = new StreamCodec<>()
	{
		@Override
		public ServerboundKeyboardEvent decode(RegistryFriendlyByteBuf object)
		{
			boolean val1 = BOOLEAN_STREAM_CODEC.decode(object);
			boolean val2 = BOOLEAN_STREAM_CODEC.decode(object);
			return new ServerboundKeyboardEvent(val1, val2);
		}

		@Override
		public void encode(RegistryFriendlyByteBuf object, ServerboundKeyboardEvent object2)
		{
			BOOLEAN_STREAM_CODEC.encode(object, object2.up);
			BOOLEAN_STREAM_CODEC.encode(object, object2.climbing);
		}
	};
	public final boolean up;
	public final boolean climbing;

	public ServerboundKeyboardEvent(boolean up, boolean climbing)
	{
		this.up = up;
		this.climbing = climbing;
	}

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}
}
