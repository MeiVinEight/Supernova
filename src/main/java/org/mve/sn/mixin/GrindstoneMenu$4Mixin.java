package org.mve.sn.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.mve.sn.GrindstoneMenuAccessor;
import org.mve.sn.SupernovaMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.BiConsumer;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$4")
public class GrindstoneMenu$4Mixin
{
	@Shadow
	@Final
	GrindstoneMenu field_16780;

	@Inject(
		method = "onTake",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
			ordinal = 0
		),
		cancellable = true
	)
	public void onTake0(Player player, ItemStack itemStack, CallbackInfo ci)
	{
		int sn = ((SupernovaMenu) this.field_16780).supernova();
		if (sn == 1)
		{
			ci.cancel();
			GrindstoneMenuAccessor acc = (GrindstoneMenuAccessor) this.field_16780;
			Container container = acc.repairSlots();
			ItemStack item0 = container.getItem(0);
			DataComponentType<ItemEnchantments> comp = GrindstoneMenuAccessor.getComponentType(item0);
			ItemEnchantments enchants = item0.getOrDefault(comp, ItemEnchantments.EMPTY);
			item0.set(comp, ItemEnchantments.EMPTY);
			Set<Object2IntMap.Entry<Holder<Enchantment>>> entries = enchants.entrySet();
			boolean skipFirst = item0.is(Items.ENCHANTED_BOOK);
			boolean notFirst = false;
			for (Object2IntMap.Entry<Holder<Enchantment>> entry : entries)
			{
				Holder<Enchantment> holder = entry.getKey();
				if (holder.is(EnchantmentTags.CURSE))
				{
					item0.enchant(holder, entry.getIntValue());
					continue;
				}
				if (skipFirst && notFirst)
					item0.enchant(holder, entry.getIntValue());
				notFirst = true;
			}
			container.getItem(1).shrink(1);
			Level level = player.level();
			level.playSound(
				null,
				new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ()),
				SoundEvents.ENCHANTMENT_TABLE_USE,
				SoundSource.BLOCKS,
				1.0F,
				level.random.nextFloat() * 0.1F + 0.9F
			);
			acc.createResult0();
		}
	}

	@Redirect(
		method = "onTake",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V"
		)
	)
	public void onTake$execute0(ContainerLevelAccess instance, BiConsumer<Level, BlockPos> biConsumer)
	{
		int sn = ((SupernovaMenu) this.field_16780).supernova();
		if (sn != 0) return;
		instance.execute(biConsumer);
	}
}
