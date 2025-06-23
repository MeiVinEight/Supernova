package org.mve.sn.screen;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.mve.sn.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StringArrayValue extends ConfigValue
{
	public ImmutableList<String> defaultValue;
	private boolean expand = false;
	public final ArrayList<StringEntry> value = new ArrayList<>();
	private final Button addon;

	public StringArrayValue(Screen screen, String key, Font font)
	{
		super(screen, key, font);
		this.resetEnable = true;
		this.addon = new Button.Builder(Component.literal("+"), this::onAddon)
			.size(this.boxWidth(), this.lineHeight)
			.build();
		this.resetValue = this::reset;
	}

	@Override
	public void draw(GuiGraphics graphics, int x, int y, int mouseX, int mouseY)
	{
		super.draw(graphics, x, y, mouseX, mouseY);

		x += this.indent + (2 * this.lineHeight) + ConfigMenu.PADDING;
		y += this.lineHeight + ConfigMenu.PADDING;
		for (StringEntry entry : this.value)
		{
			entry.remove.setPosition(x - this.lineHeight - ConfigMenu.PADDING, y);
			entry.remove.setWidth(this.lineHeight);
			entry.remove.setHeight(this.lineHeight);
			entry.value.setPosition(x, y);
			entry.value.setWidth(this.boxWidth());
			entry.value.setHeight(this.lineHeight);
			y += this.lineHeight + ConfigMenu.PADDING;
		}
		this.addon.setPosition(x, y);
		this.addon.setWidth(this.boxWidth());
		this.addon.setHeight(this.lineHeight);
		this.updateHeight();

		if (!this.expand)
			return;
		for (StringEntry value : this.value)
		{
			value.remove.render(graphics, mouseX, mouseY, 0);
			int color = 0xFF5555;
			if (Configuration.validateEntity(value.value.getValue()))
				color = 0x55FF55;
			value.value.setTextColor(color);
			value.value.render(graphics, mouseX, mouseY, 0);
		}
		this.addon.render(graphics, mouseX, mouseY, 0);
	}

	@Override
	public boolean click(double x, double y)
	{
		if (super.click(x, y))
			return true;

		if ((x < this.indent) || (x > this.previousX + this.width - this.indent))
			return false;

		if (!this.expand)
			return false;

		boolean flag = false;
		for (int i = 0; i < this.value.size(); i++)
		{
			if (flag)
				break;
			StringEntry value = this.value.get(i);
			if (value.remove.mouseClicked(x, y, 0))
			{
				this.value.remove(i);
				return true;
			}
			flag = value.remove.mouseClicked(x, y, 0);
			if (flag)
				this.parent.setFocused(value.remove);
			flag = value.value.mouseClicked(x, y, 0);
			if (flag)
				this.parent.setFocused(value.value);
		}
		if (flag)
			return true;
		flag = this.addon.mouseClicked(x, y, 0);
		if (flag)
			this.parent.setFocused(this.addon);
		return flag;
	}

	@Override
	public void update()
	{
		this.resetActive = this.defaultValue != null;
	}

	@Override
	public void clickText()
	{
		this.expand = !this.expand;
		this.updateHeight();
	}

	public void reset(ConfigValue ignored)
	{
		this.value(this.defaultValue);
	}

	public void updateHeight()
	{
		int newHeight = this.lineHeight;
		if (this.expand)
			newHeight += ((this.value.size() + 1) * (this.lineHeight + ConfigMenu.PADDING)) + ConfigMenu.PADDING;
		this.height = newHeight;
	}

	public void onAddon(Button b)
	{
		this.value.add(this.entry(""));
	}

	public void value(List<? extends String> value)
	{
		this.value.clear();
		for (String s : value)
		{
			this.value.add(this.entry(s));
		}
	}

	public List<String> value()
	{
		LinkedList<String> list = new LinkedList<>();
		for (StringEntry value : this.value)
			if (!value.value.getValue().isEmpty())
				list.add(value.value.getValue());
		return list;
	}

	public StringEntry entry(String val)
	{
		StringEntry entry = new StringEntry();
		entry.remove = new Button.Builder(Component.literal("§c-"), (b) -> {})
			.size(this.lineHeight, this.lineHeight)
			.build();
		int x = this.previousX + this.indent + (2 * this.lineHeight) + ConfigMenu.PADDING;
		int y = this.previousY + this.lineHeight + ConfigMenu.PADDING;
		EditBox box = new EditBox(this.font, x, y, this.boxWidth(), this.lineHeight, Component.empty());
		box.setValue(val);
		box.setHighlightPos(0);
		box.setCursorPosition(0);
		entry.value = box;
		return entry;
	}

	public int boxWidth()
	{
		return this.width - (2 * this.indent) - (2 * this.lineHeight) - ConfigMenu.PADDING;
	}
}
