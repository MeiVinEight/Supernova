package org.mve.sn.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.mve.sn.Configuration;
import org.mve.sn.Supernova;

import java.util.ArrayList;
import java.util.List;

public class ConfigMenu extends Screen
{
	public static final int PADDING = 6;
	public static final String[] BOOLEAN_TEXT = {"§cNO", "§aYES"};
	private final Screen parent;
	public BooleanValue compatibility;
	public final List<String> explosion = new ArrayList<>(Configuration.ENTITY_EXPLOSION.get());

	public ConfigMenu(Screen parent)
	{
		super(Component.translatable("supernova.config.title"));
		this.parent = parent;
		Minecraft mc = parent.getMinecraft();
		this.compatibility = new BooleanValue("supernova.config.compatibility", mc.font);
		this.compatibility.value = Configuration.ENCHANTMENT_COMPATIBILITY.get();
	}

	@Override
	public void onClose()
	{
		this.save();
		if (this.minecraft == null)
			super.onClose();
		else
			this.minecraft.setScreen(this.parent);
	}

	@Override
	public void init()
	{
		this.clearWidgets();

		int containerWidth = this.width - (2 * PADDING);
		int containerHeight = this.height - (3 * PADDING) - 20;

		this.compatibility.width = containerWidth;

		int y = this.height - 20 - PADDING;
		int doneButtonWidth = Math.min(200, (this.width - (PADDING * 3)) / 2);
		Button saveButton = new Button.Builder(Component.translatable("supernova.config.save"), b -> this.save())
			.bounds((width - (doneButtonWidth * 2) - PADDING) / 2, y, doneButtonWidth, 20)
			.build();
		Button doneButton = new Button.Builder(Component.translatable("supernova.config.done"), (button1) -> this.close())
			.bounds((width - PADDING) / 2 + PADDING, y, doneButtonWidth, 20)
			.build();
		this.addRenderableWidget(saveButton);
		this.addRenderableWidget(doneButton);

		ConfigArray array = new ConfigArray(this.getMinecraft(), containerWidth, containerHeight, PADDING, PADDING);
		array.push(this.compatibility);
		this.addRenderableWidget(array);
	}

	@Override
	public void render(@NotNull GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_)
	{
		this.renderBackground(p_281549_);
		super.render(p_281549_, p_281550_, p_282878_, p_282465_);
	}

	public void save()
	{
		Supernova.LOGGER.info("Configuration saving");
		Configuration.ENCHANTMENT_COMPATIBILITY.set(this.compatibility.value);
		Configuration.ENCHANTMENT_COMPATIBILITY.save();
		Configuration.ENTITY_EXPLOSION.set(this.explosion);
		Configuration.ENTITY_EXPLOSION.save();
	}

	public void close()
	{
		this.save();
		this.onClose();
	}
}
