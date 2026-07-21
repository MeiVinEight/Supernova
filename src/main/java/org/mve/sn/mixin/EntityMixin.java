package org.mve.sn.mixin;

import net.minecraft.world.entity.Entity;
import org.mve.sn.SupernovaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements SupernovaEntity
{
	@Unique
	public int[] supernova;

	@Override
	public int[] supernova()
	{
		return this.supernova;
	}

	@Override
	public void supernova(int[] supernova)
	{
		this.supernova = supernova;
	}
}
