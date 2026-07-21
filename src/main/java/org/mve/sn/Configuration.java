package org.mve.sn;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Configuration
{
	public static final ForgeConfigSpec.BooleanValue REPAIR_COST;
	public static final ForgeConfigSpec.BooleanValue ENDER_BOW;
	public static final ForgeConfigSpec.DoubleValue ENDER_SKELETON_PROBABILITY;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_EXPLOSION;
	public static final ForgeConfigSpec.BooleanValue DIGGER_PICKUP;
	public static final ForgeConfigSpec.BooleanValue INFINITY_FIX;
	public static final ForgeConfigSpec.BooleanValue ESCAPE_ALLOWED;
	public static final ForgeConfigSpec SPECIFICATION;

	static
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		REPAIR_COST = builder
			.comment("Whether or not enable repair cost")
			.define("REPAIR_COST", true);
		ENDER_BOW = builder
			.comment("Whether or not enable ender bow")
			.define("ENDERBOW", false);
		ENDER_SKELETON_PROBABILITY = builder
			.comment("Probability for skeleton become to ender skeleton (with ender bow)")
			.defineInRange("ENDER_SKELETON_PROBABILITY", 0.0625, 0, 1);
		ENTITY_EXPLOSION = builder
			.comment("Entity in list explosion will not destroy blocks")
			.defineListAllowEmpty("ENTITY_EXPLOSION", List.of("minecraft:creeper"), o -> true);
		DIGGER_PICKUP = builder
			.comment("Auto pickup item after destroy block when holding shift")
			.define("DIGGER_PICKUP", false);
		INFINITY_FIX = builder
			.comment("Removes need to have an arrow in your inventory to use the Infinity enchant on your bow.")
			.define("INFINITY_FIX", false);
		ESCAPE_ALLOWED = builder
			.comment("Allow color char § in chat")
			.define("ESCAPE_ALLOWED", false);
		SPECIFICATION = builder.build();
	}
}
