package org.mve.sn.screen;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.mve.sn.Configuration;
import org.mve.sn.Supernova;

public class ConfigMenu extends Screen
{
	public static final int PADDING = 6;
	private final Screen parent;
	public final GroupValue groupEnchantment;
	public final BooleanValue repaircost;
	public final BooleanValue compatibility;
	public final GroupValue groupLevel;
	public final LongValue sharpness;
	public final GroupValue groupEnderbow;
	public final BooleanValue enderbow;
	public final DoubleValue skeleton;
	public final GroupValue groupExplosion;
	public final StringArrayValue explosion;

	public ConfigMenu(Screen parent)
	{
		super(Component.translatable("supernova.config.title"));
		this.parent = parent;
		Minecraft mc = parent.getMinecraft();

		this.groupEnchantment = new GroupValue(this, "supernova.config.group.enchantment", mc.font);

		this.repaircost = new BooleanValue(this, "supernova.config.repaircost", mc.font);
		this.repaircost.value = Configuration.REPAIR_COST.get();
		this.repaircost.resetValue = (val) -> ((BooleanValue) val).value = Configuration.REPAIR_COST.get();
		this.repaircost.tooltip = Component.translatable("supernova.config.repaircost.tooltip");

		this.compatibility = new BooleanValue(this, "supernova.config.compatibility", mc.font);
		this.compatibility.value = Configuration.ENCHANTMENT_COMPATIBILITY.get();
		this.compatibility.resetValue = (value) -> ((BooleanValue) value).value = Configuration.ENCHANTMENT_COMPATIBILITY.get();

		this.groupLevel = new GroupValue(this, "supernova.config.group.enchantment.level", mc.font);
		this.sharpness = new LongValue(this, "enchantment.minecraft.sharpness", mc.font);
		this.sharpness.min = 1;
		this.sharpness.max = 255;
		this.sharpness.value(Configuration.MAX_LEVEL_SHARPNESS.get());
		this.sharpness.resetValue = (val) -> ((LongValue) val).value(Configuration.MAX_LEVEL_SHARPNESS.get());
		this.groupLevel.group.add(this.sharpness);

		this.groupEnchantment.group.add(this.repaircost);
		this.groupEnchantment.group.add(this.compatibility);
		this.groupEnchantment.group.add(this.groupLevel);

		this.groupEnderbow = new GroupValue(this, "supernova.config.group.enderbow", mc.font);
		this.enderbow = new BooleanValue(this, "supernova.config.enderbow", mc.font);
		this.enderbow.value = Configuration.ENDER_BOW.get();
		this.enderbow.resetValue = (value) -> ((BooleanValue) value).value = Configuration.ENDER_BOW.get();
		this.skeleton = new DoubleValue(this, "supernova.confug.ender.skeleton", mc.font);
		this.skeleton.value(Configuration.ENDER_SKELETON_PROBABILITY.get());
		this.skeleton.resetValue = (value) -> ((DoubleValue) value).value(Configuration.ENDER_SKELETON_PROBABILITY.get());
		this.groupEnderbow.group.add(this.enderbow);
		this.groupEnderbow.group.add(this.skeleton);

		this.groupExplosion = new GroupValue(this, "supernova.config.group.explosion", mc.font);
		this.explosion = new StringArrayValue(this, "supernova.config.entity.explosion", mc.font);
		this.explosion.value(Configuration.ENTITY_EXPLOSION.get());
		this.explosion.defaultValue = ImmutableList.copyOf(Configuration.ENTITY_EXPLOSION.getDefault());
		this.explosion.tooltip = Component.translatable("supernova.config.entity.explosion.tooltip");
		this.groupExplosion.group.add(this.explosion);
	}

	@Override
	public void onClose()
	{
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
		array.push(this.groupEnchantment);
		array.push(this.repaircost);
		array.push(this.compatibility);
		array.push(this.groupLevel);
		array.push(this.sharpness);
		array.push(this.groupEnderbow);
		array.push(this.enderbow);
		array.push(this.skeleton);
		array.push(this.groupExplosion);
		array.push(this.explosion);
		this.addRenderableWidget(array);
	}

	@Override
	public void render(@NotNull GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_)
	{
		this.skeleton.active = this.enderbow.value;
		this.renderBackground(p_281549_);
		super.render(p_281549_, p_281550_, p_282878_, p_282465_);
	}

	@Override
	public boolean keyPressed(int p_96552_, int p_96553_, int p_96554_)
	{
		return super.keyPressed(p_96552_, p_96553_, p_96554_);
	}

	public void save()
	{
		Supernova.LOGGER.info("Configuration saving");
		Configuration.REPAIR_COST.set(this.repaircost.value);
		Configuration.REPAIR_COST.save();
		Configuration.ENCHANTMENT_COMPATIBILITY.set(this.compatibility.value);
		Configuration.ENCHANTMENT_COMPATIBILITY.save();
		Configuration.MAX_LEVEL_SHARPNESS.set(this.sharpness.value.intValue());
		Configuration.MAX_LEVEL_SHARPNESS.save();
		Configuration.ENDER_BOW.set(this.enderbow.value);
		Configuration.ENDER_BOW.save();
		Configuration.ENDER_SKELETON_PROBABILITY.set(this.skeleton.value.doubleValue());
		Configuration.ENDER_SKELETON_PROBABILITY.save();
		Configuration.ENTITY_EXPLOSION.set(this.explosion.value());
		Configuration.ENTITY_EXPLOSION.save();
	}

	public void close()
	{
		this.save();
		this.onClose();
	}
}
