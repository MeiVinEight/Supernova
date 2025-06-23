package org.mve.sn.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BooleanValue extends ConfigValue
{
	public static final String[] BOOLEAN_TEXT = {"§cNO", "§aYES"};
	public boolean value;

	public BooleanValue(Screen screen, String key, Font font)
	{
		super(screen, key, font);
		this.valueWidget = new Button.Builder(Component.literal(BOOLEAN_TEXT[intValue(false)]), this::onClick)
			.size(this.valueWidth, this.lineHeight)
			.build();
	}

	@Override
	public void update()
	{
		this.valueWidget.setMessage(Component.literal(BOOLEAN_TEXT[intValue(this.value)]));
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
