package org.mve.sn.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class ConfigValue
{
	public final String key;
	public final Font font;
	public int width = 200;
	public int height = 20;
	private StringWidget text;

	public ConfigValue(String key, Font font)
	{
		this.key = key;
		this.font = font;
	}

	public void position(int x, int y)
	{
		this.text = new StringWidget(x + 50, y, this.width, this.height, Component.translatable(this.key), this.font);
		this.text.alignLeft();
	}

	public void draw(GuiGraphics graphics, int mouseX, int mouseY)
	{
		this.text.render(graphics, mouseX, mouseY, 0);
	}

	public void click(double x, double y)
	{
	}
}
