package org.mve.sn;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class Sounds
{
	public static final RandomSource RANDOM = RandomSource.create();

	public static void play(ServerPlayer player, SoundEvent sound, SoundSource source, float volume, float pitch)
	{
		if (player == null)
			return;
		if (sound == null)
			return;
		if (source == null)
			return;
		Optional<Holder<SoundEvent>> optional = ForgeRegistries.SOUND_EVENTS.getHolder(sound);
		if (optional.isEmpty())
		{
			Supernova.LOGGER.warn("Registry holder is null: {}", sound);
			return;
		}
		long seed = RANDOM.nextLong();
		ClientboundSoundPacket packet = new ClientboundSoundPacket(
			optional.get(),
			source,
			player.getX(),
			player.getY() + 1.0,
			player.getZ(),
			volume,
			pitch,
			seed
		);
		player.connection.send(packet);
	}
}
