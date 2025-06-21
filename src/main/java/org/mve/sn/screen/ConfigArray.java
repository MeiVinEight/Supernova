package org.mve.sn.screen;

import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class ConfigArray extends ScrollPanel
{
	private final List<ConfigValue> value = new LinkedList<>();
	private int previousY = Integer.MIN_VALUE;
	private final int x;

	public ConfigArray(Minecraft client, int width, int height, int top, int left)
	{
		super(client, width, height, top, left);
		this.x = left;
	}

	@Override
	protected int getContentHeight()
	{
		return 10 * 20 + 9 * 6 - 4;
	}

	@Override
	protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY)
	{
		relativeY -= 4;
		boolean changed = this.previousY != relativeY;
		this.previousY = relativeY;
		for (ConfigValue configValue : this.value)
		{
			if (changed)
				configValue.position(x, relativeY);
			configValue.draw(guiGraphics, mouseX, mouseY);
			relativeY += configValue.height;
		}
	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics, Tesselator tess, float partialTick)
	{
		super.drawBackground(guiGraphics, tess, partialTick);
	}

	@Override
	public @NotNull NarrationPriority narrationPriority()
	{
		return NarrationPriority.NONE;
	}

	@Override
	public void updateNarration(@NotNull NarrationElementOutput p_169152_)
	{
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if (!this.click(mouseX, mouseY))
			return super.mouseClicked(mouseX, mouseY, button);
		return true;
	}

	public void push(ConfigValue value)
	{
		this.value.add(value);
	}

	public boolean click(double x, double y)
	{
		if (y < 6)
			return false;
		if (y > this.height + 6)
			return false;
		if (x < 6)
			return false;
		if (x > this.width + 6)
			return false;
		double y1 = y - this.previousY;
		int cy = 0;
		for (ConfigValue configValue : this.value)
		{
			if (cy > y1)
				return false;
			if (cy + configValue.height >= y1)
			{
				configValue.click(x, y);
				break;
			}
		}
		return false;
	}
}
