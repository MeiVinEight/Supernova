package org.mve.sn.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import org.mve.sn.Configuration;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonEntityMixin
{
	@Inject(at = @At("RETURN"), method = "populateDefaultEquipmentSlots")
	private void initEquipment(RandomSource random, DifficultyInstance difficultyInstance, CallbackInfo ci)
	{
		if (!Configuration.ENDER_BOW.get())
			return;
		AbstractSkeleton _this = (AbstractSkeleton) (Object) this;
		if (random.nextFloat() > Configuration.ENDER_SKELETON_PROBABILITY.get()) return;

		ItemStack item = _this.getMainHandItem();
		if (item == null) return;
		if (item.getItem() != Items.BOW) return;

		int[] supernova = new int[]{Supernova.SUPERNOVA_ENDERBOW};
		CustomData newData = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
			.update(tag -> tag.putIntArray(Supernova.SUPERNOVA, supernova));
		item.set(DataComponents.CUSTOM_DATA, newData);
	}
}
