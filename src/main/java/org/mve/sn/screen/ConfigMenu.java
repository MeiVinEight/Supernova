package org.mve.sn.screen;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
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
	public final LongValue blastpro;
	public final LongValue featherfalling;
	public final LongValue firepro;
	public final LongValue fortune;
	public final LongValue looting;
	public final LongValue loyalty;
	public final LongValue lucksea;
	public final LongValue power;
	public final LongValue projectilepro;
	public final LongValue protection;
	public final LongValue sharpness;
	public final LongValue sweeping;
	public final LongValue unbreaking;
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
		this.repaircost.resetValue = (val) -> ((BooleanValue) val).value = Configuration.REPAIR_COST.getDefault();
		this.repaircost.tooltip = Component.translatable("supernova.config.repaircost.tooltip");

		this.compatibility = new BooleanValue(this, "supernova.config.compatibility", mc.font);
		this.compatibility.value = Configuration.ENCHANTMENT_COMPATIBILITY.get();
		this.compatibility.resetValue = (value) -> ((BooleanValue) value).value = Configuration.ENCHANTMENT_COMPATIBILITY.getDefault();

		this.groupLevel = new GroupValue(this, "supernova.config.group.enchantment.level", mc.font);
		this.blastpro = ConfigMenu.createEnchantment(this, "enchantment.minecraft.blast_protection", mc.font, Configuration.MAX_LEVEL_BLASTPROTECTION);
		this.featherfalling = ConfigMenu.createEnchantment(this, "enchantment.minecraft.feather_falling", mc.font, Configuration.MAX_LEVEL_FEATHERFALLING);
		this.firepro = ConfigMenu.createEnchantment(this, "enchantment.minecraft.fire_protection", mc.font, Configuration.MAX_LEVEL_FIREPROTECTION);
		this.fortune = ConfigMenu.createEnchantment(this, "enchantment.minecraft.fortune", mc.font, Configuration.MAX_LEVEL_FORTUNE);
		this.looting = ConfigMenu.createEnchantment(this, "enchantment.minecraft.looting", mc.font, Configuration.MAX_LEVEL_LOOTING);
		this.loyalty = ConfigMenu.createEnchantment(this, "enchantment.minecraft.loyalty", mc.font, Configuration.MAX_LEVEL_LOYALTY);
		this.lucksea = ConfigMenu.createEnchantment(this, "enchantment.minecraft.luck_of_the_sea", mc.font, Configuration.MAX_LEVEL_LUCKOFTHESEA);
		this.power = ConfigMenu.createEnchantment(this, "enchantment.minecraft.power", mc.font, Configuration.MAX_LEVEL_POWER);
		this.projectilepro = ConfigMenu.createEnchantment(this, "enchantment.minecraft.projectile_protection", mc.font, Configuration.MAX_LEVEL_PROJECTILEPROTECTION);
		this.protection = ConfigMenu.createEnchantment(this, "enchantment.minecraft.protection", mc.font, Configuration.MAX_LEVEL_PROTECTION);
		this.sharpness = ConfigMenu.createEnchantment(this, "enchantment.minecraft.sharpness", mc.font, Configuration.MAX_LEVEL_SHARPNESS);
		this.sweeping = ConfigMenu.createEnchantment(this, "enchantment.minecraft.sweeping", mc.font, Configuration.MAX_LEVEL_SWEEPING);
		this.unbreaking = ConfigMenu.createEnchantment(this, "enchantment.minecraft.unbreaking", mc.font, Configuration.MAX_LEVEL_UNBREAKING);
		this.groupLevel.group.add(this.blastpro);
		this.groupLevel.group.add(this.featherfalling);
		this.groupLevel.group.add(this.firepro);
		this.groupLevel.group.add(this.fortune);
		this.groupLevel.group.add(this.looting);
		this.groupLevel.group.add(this.loyalty);
		this.groupLevel.group.add(this.lucksea);
		this.groupLevel.group.add(this.power);
		this.groupLevel.group.add(this.projectilepro);
		this.groupLevel.group.add(this.protection);
		this.groupLevel.group.add(this.sharpness);
		this.groupLevel.group.add(this.sweeping);
		this.groupLevel.group.add(this.unbreaking);

		this.groupEnchantment.group.add(this.repaircost);
		this.groupEnchantment.group.add(this.compatibility);
		this.groupEnchantment.group.add(this.groupLevel);

		this.groupEnderbow = new GroupValue(this, "supernova.config.group.enderbow", mc.font);
		this.enderbow = new BooleanValue(this, "supernova.config.enderbow", mc.font);
		this.enderbow.value = Configuration.ENDER_BOW.get();
		this.enderbow.resetValue = (value) -> ((BooleanValue) value).value = Configuration.ENDER_BOW.getDefault();
		this.skeleton = new DoubleValue(this, "supernova.confug.ender.skeleton", mc.font);
		this.skeleton.value(Configuration.ENDER_SKELETON_PROBABILITY.get());
		this.skeleton.resetValue = (value) -> ((DoubleValue) value).value(Configuration.ENDER_SKELETON_PROBABILITY.getDefault());
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
		array.push(this.blastpro);
		array.push(this.featherfalling);
		array.push(this.firepro);
		array.push(this.fortune);
		array.push(this.looting);
		array.push(this.loyalty);
		array.push(this.lucksea);
		array.push(this.power);
		array.push(this.projectilepro);
		array.push(this.protection);
		array.push(this.sharpness);
		array.push(this.sweeping);
		array.push(this.unbreaking);
		array.push(this.groupEnderbow);
		array.push(this.enderbow);
		array.push(this.skeleton);
		array.push(this.groupExplosion);
		array.push(this.explosion);
		this.addRenderableWidget(array);
	}

	@Override
	public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_)
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
		Configuration.MAX_LEVEL_BLASTPROTECTION.set(this.blastpro.value.intValue());
		Configuration.MAX_LEVEL_BLASTPROTECTION.save();
		Configuration.MAX_LEVEL_FEATHERFALLING.set(this.featherfalling.value.intValue());
		Configuration.MAX_LEVEL_FEATHERFALLING.save();
		Configuration.MAX_LEVEL_FIREPROTECTION.set(this.firepro.value.intValue());
		Configuration.MAX_LEVEL_FIREPROTECTION.save();
		Configuration.MAX_LEVEL_FORTUNE.set(this.fortune.value.intValue());
		Configuration.MAX_LEVEL_FORTUNE.save();
		Configuration.MAX_LEVEL_LOOTING.set(this.looting.value.intValue());
		Configuration.MAX_LEVEL_LOOTING.save();
		Configuration.MAX_LEVEL_LOYALTY.set(this.loyalty.value.intValue());
		Configuration.MAX_LEVEL_LOYALTY.save();
		Configuration.MAX_LEVEL_LUCKOFTHESEA.set(this.lucksea.value.intValue());
		Configuration.MAX_LEVEL_LUCKOFTHESEA.save();
		Configuration.MAX_LEVEL_POWER.set(this.power.value.intValue());
		Configuration.MAX_LEVEL_POWER.save();
		Configuration.MAX_LEVEL_PROJECTILEPROTECTION.set(this.projectilepro.value.intValue());
		Configuration.MAX_LEVEL_PROJECTILEPROTECTION.save();
		Configuration.MAX_LEVEL_PROTECTION.set(this.protection.value.intValue());
		Configuration.MAX_LEVEL_PROTECTION.save();
		Configuration.MAX_LEVEL_SHARPNESS.set(this.sharpness.value.intValue());
		Configuration.MAX_LEVEL_SHARPNESS.save();
		Configuration.MAX_LEVEL_SWEEPING.set(this.sweeping.value.intValue());
		Configuration.MAX_LEVEL_SWEEPING.save();
		Configuration.MAX_LEVEL_UNBREAKING.set(this.unbreaking.value.intValue());
		Configuration.MAX_LEVEL_UNBREAKING.save();
		Configuration.ENDER_BOW.set(this.enderbow.value);
		Configuration.ENDER_BOW.save();
		Configuration.ENDER_SKELETON_PROBABILITY.set(this.skeleton.value.doubleValue());
		Configuration.ENDER_SKELETON_PROBABILITY.save();
		Configuration.ENTITY_EXPLOSION.set(this.explosion.value());
		Configuration.ENTITY_EXPLOSION.save();
		Configuration.check();
	}

	public void close()
	{
		this.save();
		this.onClose();
	}

	public static LongValue createEnchantment(AbstractContainerEventHandler parent, String key, Font font, ForgeConfigSpec.IntValue config)
	{
		LongValue enchantment = new LongValue(parent, key, font);
		enchantment.min = 1;
		enchantment.max = 255;
		enchantment.value(config.get());
		enchantment.resetValue = (val) -> ((LongValue) val).value(config.getDefault());
		return enchantment;
	}
}
