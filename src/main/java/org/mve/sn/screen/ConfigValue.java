package org.mve.sn.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.network.chat.Component;

public class ConfigValue
{
	public static final int COLOR_DEFAULT = 0;
	public static final int COLOR_HOVER   = 1;
	public AbstractContainerEventHandler parent;
	public final String key;
	public final Font font;
	public Component tooltip;
	public int width = 200;
	public int valueWidth = 100;
	public int height = 20;
	public int lineHeight = 20;
	public int indent = 50;
	private StringWidget text;
	public boolean resetEnable = false;
	public boolean resetActive = false;
	private Button reset;
	public int previousX;
	public int previousY;
	public double mouseX;
	public double mouseY;
	public final int[] color = {0xFFFFFF, 0xFFFF55};

	public ConfigValue(AbstractContainerEventHandler parent, String key, Font font)
	{
		this.parent = parent;
		this.key = key;
		this.font = font;
	}

	public void position(int x, int y)
	{
		this.previousX = x;
		this.previousY = y;
		int textWidth = this.width - (2 * this.indent) - this.valueWidth - ConfigMenu.PADDING;
		this.text = new StringWidget(x + this.indent, y, textWidth, this.lineHeight, Component.translatable(this.key), this.font);
		this.text.alignLeft();
		this.text.active = true;
		if (this.tooltip != null)
			this.text.setTooltip(Tooltip.create(this.tooltip));
		if (this.resetEnable)
		{
			this.reset = new Button.Builder(Component.literal("§c×"), this::onReset)
				.bounds(x + this.width - this.indent + ConfigMenu.PADDING, y, this.lineHeight, this.lineHeight)
				.tooltip(Tooltip.create(Component.translatable("supernova.config.reset")))
				.build();
		}
	}

	public void draw(GuiGraphics graphics, int mouseX, int mouseY)
	{
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		int textColor = this.color[this.hoverText() ? COLOR_HOVER : COLOR_DEFAULT];
		this.text.setColor(textColor);
		this.text.render(graphics, mouseX, mouseY, 0);
		if (this.resetEnable)
		{
			this.reset.active = this.resetActive;
			this.reset.render(graphics, mouseX, mouseY, 0);
		}
	}

	public boolean click(double x, double y)
	{
		if (this.text.mouseClicked(x, y, 0))
		{
			this.parent.setFocused(null);
			this.clickText();
			return true;
		}
		if (!this.resetActive)
			return false;
		if (this.reset.mouseClicked(x, y, 0))
		{
			this.parent.setFocused(this.reset);
			return true;
		}
		return false;
	}

	public void onReset(Button button)
	{
	}

	public boolean hoverText()
	{
		int textWidth = this.width - (2 * this.indent) - this.valueWidth - ConfigMenu.PADDING;
		if (this.mouseX < this.previousX + this.indent)
			return false;
		if (this.mouseX > this.previousX + this.indent + textWidth)
			return false;
		if (this.mouseY < this.previousY)
			return false;
		return !(this.mouseY > this.previousY + this.lineHeight);
	}

	public void clickText()
	{
	}
}
