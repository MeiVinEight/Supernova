package org.mve.sn.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class BooleanValue extends ConfigValue
{
	public boolean value;
	public int buttonWidth = 100;
	private Button button;

	public BooleanValue(String key, Font font)
	{
		super(key, font);
	}

	@Override
	public void position(int x, int y)
	{
		super.position(x, y);
		this.button = new Button.Builder(Component.literal(ConfigMenu.BOOLEAN_TEXT[intValue(this.value)]), this::onClick)
			.bounds(x + this.width - this.buttonWidth - 50, y, this.buttonWidth, this.height)
			.build();
	}

	@Override
	public void draw(GuiGraphics graphics, int mouseX, int mouseY)
	{
		super.draw(graphics, mouseX, mouseY);
		this.button.render(graphics, mouseX, mouseY, 0);
	}

	@Override
	public void click(double x, double y)
	{
		if (this.button == null)
			return;
		this.button.mouseClicked(x, y, 0);
		this.button = new Button.Builder(Component.literal(ConfigMenu.BOOLEAN_TEXT[intValue(this.value)]), this::onClick)
			.bounds(this.button.getX(), this.button.getY(), this.buttonWidth, this.height)
			.build();
	}

	public void onClick(Button button)
	{
		this.value = !this.value;
	}

	private static int intValue(boolean bool)
	{
		return bool ? 1 : 0;
	}
}
