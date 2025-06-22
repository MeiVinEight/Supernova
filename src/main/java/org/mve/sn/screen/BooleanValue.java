package org.mve.sn.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BooleanValue extends ConfigValue
{
	public boolean value;
	private Button button;

	public BooleanValue(Screen screen, String key, Font font)
	{
		super(screen, key, font);
	}

	@Override
	public void position(int x, int y)
	{
		super.position(x, y);
		this.button = new Button.Builder(Component.literal(ConfigMenu.BOOLEAN_TEXT[intValue(this.value)]), this::onClick)
			.bounds(x + this.width - this.valueWidth - this.indent, y, this.valueWidth, this.lineHeight)
			.build();
	}

	@Override
	public void draw(GuiGraphics graphics, int mouseX, int mouseY)
	{
		super.draw(graphics, mouseX, mouseY);
		this.button.render(graphics, mouseX, mouseY, 0);
	}

	@Override
	public boolean click(double x, double y)
	{
		if (super.click(x, y))
			return true;
		if (this.button == null)
			return false;
		this.button.mouseClicked(x, y, 0);
		this.button = new Button.Builder(Component.literal(ConfigMenu.BOOLEAN_TEXT[intValue(this.value)]), this::onClick)
			.bounds(this.button.getX(), this.button.getY(), this.valueWidth, this.lineHeight)
			.build();
		this.parent.setFocused(this.button);
		return true;
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
