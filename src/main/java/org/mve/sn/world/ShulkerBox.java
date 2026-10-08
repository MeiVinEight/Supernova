package org.mve.sn.world;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class ShulkerBox implements Container
{
	public static final int SHULKER_BOX_SIZE = 27;
	public final ItemStack item;
	public final NonNullList<ItemStack> items;
	public Function<ShulkerBoxMenu, ShulkerBoxMenu> onCreate;
	public Consumer<Player> onChange;
	public Consumer<Player> onClose;
	public ServerPlayer owner;
	public int nonEmpty;
	public boolean valid = true;

	public ShulkerBox(ItemStack item)
	{
		this.item = item;
		this.items = NonNullList.withSize(SHULKER_BOX_SIZE, ItemStack.EMPTY);
		this.nonEmpty = 0;
		ItemContainerContents contents = this.item.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
		contents.copyInto(this.items);
		for (int i = 0; i < SHULKER_BOX_SIZE; i++)
		{
			if (!this.items.get(i).isEmpty())
				this.nonEmpty++;
		}
	}

	@Override
	public int getContainerSize()
	{
		return SHULKER_BOX_SIZE;
	}

	@Override
	public boolean isEmpty()
	{
		return this.nonEmpty == 0;
	}

	@Override
	@NotNull
	public ItemStack getItem(int i)
	{
		return this.items.get(i);
	}

	@Override
	@NotNull
	public ItemStack removeItem(int i, int j)
	{
		ItemStack itemStack = ContainerHelper.removeItem(this.items, i, j);
		if (!itemStack.isEmpty())
		{
			this.setChanged();
			if (this.items.get(i).isEmpty())
				this.nonEmpty--;
		}
		return itemStack;
	}

	@Override
	@NotNull
	public ItemStack removeItemNoUpdate(int i)
	{
		if (!this.items.get(i).isEmpty())
			this.nonEmpty--;
		return ContainerHelper.takeItem(this.items, i);
	}

	@Override
	public void setItem(int i, ItemStack itemStack)
	{
		if (this.items.get(i).isEmpty() && !itemStack.isEmpty())
			this.nonEmpty++;
		if (!this.items.get(i).isEmpty() && itemStack.isEmpty())
			this.nonEmpty--;
		this.items.set(i, itemStack);
	}

	@Override
	public void setChanged()
	{
		this.item.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
		if (this.onChange != null)
			this.onChange.accept(this.owner);
	}

	@Override
	public boolean stillValid(Player player)
	{
		return this.valid;
	}

	@Override
	public void clearContent()
	{
		this.items.clear();
	}

	@Override
	public void stopOpen(Player player)
	{
		this.setChanged();
		if (this.onClose != null)
			this.onClose.accept(player);
		this.valid = false;
	}

	public void openMenu(ServerPlayer player)
	{
		player.openMenu(new MenuProvider()
		{
			@Override
			@NotNull
			public Component getDisplayName()
			{
				return Component.translatable("container.shulkerBox");
			}

			@Override
			@NotNull
			public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
			{
				ShulkerBoxMenu menu = new ShulkerBoxMenu(i, inventory, ShulkerBox.this);
				if (ShulkerBox.this.onCreate != null)
					menu = ShulkerBox.this.onCreate.apply(menu);
				return menu;
			}
		});
		this.owner = player;
	}
}
