package org.mve.sn.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(Level.class)
public class LevelMixin
{
	/**
	 * @author meivi
	 * @reason catch null pointer exception
	 */
	@Overwrite
	public void playSeededSound(@Nullable Player p_220363_, double p_220364_, double p_220365_, double p_220366_, SoundEvent p_220367_, SoundSource p_220368_, float p_220369_, float p_220370_, long p_220371_)
	{
		Level _this = (Level)(Object) this;
		try
		{
			_this.playSeededSound(p_220363_, p_220364_, p_220365_, p_220366_, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(p_220367_), p_220368_, p_220369_, p_220370_, p_220371_);
		}
		catch (Throwable e)
		{
			Supernova.LOGGER.warn("playSeededSound", e);
		}
	}
}
