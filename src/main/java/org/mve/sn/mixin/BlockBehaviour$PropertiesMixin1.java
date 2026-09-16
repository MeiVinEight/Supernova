package org.mve.sn.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.Properties.class)
public interface BlockBehaviour$PropertiesMixin1
{
	@Accessor("drops")
	void setDrops(ResourceKey<LootTable> drops);
}
