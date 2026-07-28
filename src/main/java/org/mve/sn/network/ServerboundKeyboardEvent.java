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
	public static final StreamCodec<ByteBuf, Boolean> BOOLEAN_STREAM_CODEC = new StreamCodec<ByteBuf, Boolean>()
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
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundKeyboardEvent> CODEC = StreamCodec.composite(BOOLEAN_STREAM_CODEC, ServerboundKeyboardEvent::up, ServerboundKeyboardEvent::new);
	public final boolean up;

	public ServerboundKeyboardEvent(boolean up)
	{
		this.up = up;
	}

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	public boolean up()
	{
		return this.up;
	}
}
