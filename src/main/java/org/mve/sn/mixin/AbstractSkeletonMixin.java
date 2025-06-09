package org.mve.sn.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.mve.sn.EnderBow;
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
		AbstractSkeleton _this = (AbstractSkeleton)(Object) this;
		if (p_218949_.nextDouble() > 0.0625) return;
		ItemStack item = _this.getMainHandItem();
		if (item == null || item.isEmpty()) return;
		if (item.getItem() != Items.BOW) return;

		item.getOrCreateTag().putUUID(Supernova.SUPERNOVA, EnderBow.UID);
	}
}
