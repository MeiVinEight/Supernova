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
	public static final int CONTENT_HEIGHT = 20;
	public ImmutableList<String> defaultValue;
	private boolean expand = false;
	public final ArrayList<StringEntry> value = new ArrayList<>();
	private Button addon;

	public StringArrayValue(Screen screen, String key, Font font)
	{
		super(screen, key, font);
		this.resetEnable = true;
	}

	@Override
	public void position(int x, int y)
	{
		super.position(x, y);
		x += this.indent + (2 * CONTENT_HEIGHT) + ConfigMenu.PADDING;
		y += CONTENT_HEIGHT + ConfigMenu.PADDING;
		int boxWidth = this.width - (2 * this.indent) - (2 * CONTENT_HEIGHT);
		for (StringEntry entry : this.value)
		{
			entry.remove = new Button.Builder(Component.literal("§c-"), (b) -> {})
				.bounds(x - CONTENT_HEIGHT - ConfigMenu.PADDING, y, CONTENT_HEIGHT, CONTENT_HEIGHT)
				.build();
			EditBox box = entry.value;
			EditBox box1 = new EditBox(this.font, x, y, boxWidth, CONTENT_HEIGHT, Component.empty());
			box1.setValue(box.getValue());
			entry.value = box1;
			y += CONTENT_HEIGHT + ConfigMenu.PADDING;
		}
		this.addon = new Button.Builder(Component.literal("+"), this::onAddon)
			.bounds(x, y, boxWidth, CONTENT_HEIGHT)
			.build();
		this.updateHeight();
	}

	@Override
	public void draw(GuiGraphics graphics, int mouseX, int mouseY)
	{
		this.resetActive = this.defaultValue != null;
		super.draw(graphics, mouseX, mouseY);
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
				this.position(this.previousX, this.previousY);
				return false;
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
	public void onReset(Button b)
	{
		this.value(this.defaultValue);
	}

	@Override
	public void clickText()
	{
		this.expand = !this.expand;
		this.updateHeight();
	}

	public void updateHeight()
	{
		int newHeight = CONTENT_HEIGHT;
		if (this.expand)
			newHeight += ((this.value.size() + 1) * (CONTENT_HEIGHT + ConfigMenu.PADDING)) + ConfigMenu.PADDING;
		this.height = newHeight;
	}

	public void onAddon(Button b)
	{
		int x = this.previousX + this.indent + (2 * CONTENT_HEIGHT) + ConfigMenu.PADDING;
		int y = this.previousY + CONTENT_HEIGHT + ConfigMenu.PADDING;
		y += this.value.size() * (CONTENT_HEIGHT + ConfigMenu.PADDING);

		int boxWidth = this.width - (2 * this.indent) - (2 * CONTENT_HEIGHT);
		EditBox box = new EditBox(this.font, x, y, boxWidth, CONTENT_HEIGHT, Component.empty());
		StringEntry entry = new StringEntry();
		entry.value = box;
		this.value.add(entry);
		this.position(this.previousX, this.previousY);
	}

	public void value(List<? extends String> value)
	{
		int x = this.previousX + this.indent + (2 * CONTENT_HEIGHT) + ConfigMenu.PADDING;
		int y = this.previousY + CONTENT_HEIGHT + ConfigMenu.PADDING;
		int boxWidth = this.width - (2 * this.indent) - (2 * CONTENT_HEIGHT);
		this.value.clear();
		for (String s : value)
		{
			EditBox box = new EditBox(this.font, x, y, boxWidth, CONTENT_HEIGHT, Component.empty());
			box.setValue(s);
			StringEntry entry = new StringEntry();
			entry.value = box;
			this.value.add(entry);
			y += CONTENT_HEIGHT + ConfigMenu.PADDING;
		}
		this.position(this.previousX, this.previousY);
	}

	public List<String> value()
	{
		LinkedList<String> list = new LinkedList<>();
		for (StringEntry value : this.value)
			if (!value.value.getValue().isEmpty())
				list.add(value.value.getValue());
		return list;
	}
}
