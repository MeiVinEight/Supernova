package org.mve.sn.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.mve.sn.Configuration;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonMixin
{
	@Inject(at = @At("RETURN"), method = "populateDefaultEquipmentSlots")
	public void populateDefaultEquipmentSlots(RandomSource p_218949_, DifficultyInstance p_218950_, CallbackInfo ci)
	{
		if (!Configuration.ENDER_BOW.get())
			return;
		AbstractSkeleton _this = (AbstractSkeleton)(Object) this;
		if (p_218949_.nextDouble() > Configuration.ENDER_SKELETON_PROBABILITY.get()) return;
		ItemStack item = _this.getMainHandItem();
		if (item == null || item.isEmpty()) return;
		if (item.getItem() != Items.BOW) return;

		CompoundTag tag = item.getOrCreateTag();
		tag.putIntArray(Supernova.SUPERNOVA, Supernova.add(tag.getIntArray(Supernova.SUPERNOVA), Supernova.SUPERNOVA_ENDERBOW));
	}
}
