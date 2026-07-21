package org.mve.sn;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EnderBow
{
	/**
	 * [-7010448334308236634, 2339859691493267686]
	 * <p>
	 * [-1632247198, 89399974, 544791038, 129374438]
	 */
	public static final UUID UID = UUID.fromString("9EB5E262-0554-22A6-2078-D9FE07B618E6");

	public static void collision(Projectile entity, HitResult result)
	{
		if (!Configuration.ENDER_BOW.get()) return;
		if (entity.level().isClientSide) return;
		if (result.getType() == HitResult.Type.MISS) return;
		if (!Supernova.check(((SupernovaEntity) entity).supernova(), Supernova.SUPERNOVA_ENDERBOW)) return;

		Entity owner = entity.getOwner();
		if (owner == null) return;


		double prevX = owner.getX();
		double prevY = owner.getY();
		double prevZ = owner.getZ();

		ServerLevel world = (ServerLevel) entity.level();
		Vec3 pos = result.getLocation();

		SoundSource source = SoundSource.HOSTILE;
		SoundEvent sound = SoundEvents.ENDERMAN_TELEPORT;

		world.playSound(null, prevX, prevY, prevZ, sound, source, 1.0F, 1.0F);
		EnderBow.particle(world, owner);

		EnderBow.teleport(world, owner, pos);

		world.playSound(null, prevX, prevY, prevZ, sound, source, 1.0F, 1.0F);
		EnderBow.particle(world, owner);
	}

	public static void arrow(Entity arrow, Entity owner)
	{
		if (!Configuration.ENDER_BOW.get()) return;
		if (!(owner instanceof LivingEntity living)) return;
		SupernovaEntity sna = (SupernovaEntity) arrow;
		ItemStack stack = living.getMainHandItem();
		if (stack.isEmpty()) return;
		if (!(stack.getItem() instanceof ProjectileWeaponItem)) stack = living.getOffhandItem();
		if (stack.isEmpty()) return;
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		if (data == null) return;
		sna.supernova(data.copyTag().getIntArray(Supernova.SUPERNOVA));
	}

	private static void teleport(ServerLevel world, Entity entity, Vec3 pos)
	{
		if (entity instanceof Player)
		{
			entity.stopRiding();
		}
		while (entity.getVehicle() != null) entity = entity.getVehicle();
		entity.teleportTo(world, pos.x, pos.y, pos.z, Set.of(), entity.getYRot(), entity.getXRot());
	}

	private static void particle(ServerLevel world, Entity entity)
	{
		for (int i = 0; i < 2; i++)
		{
			ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(
				ParticleTypes.PORTAL,
				true,
				entity.getRandomX(0.5),
				entity.getRandomY() - 0.25,
				entity.getRandomZ(0.5),
				(float) ((entity.getRandom().nextDouble() - 0.5) * 2.0),
				(float) (-entity.getRandom().nextDouble()),
				(float) ((entity.getRandom().nextDouble() - 0.5) * 2.0),
				1F,
				100
			);
			List<ServerPlayer> players = world.players();
			for (ServerPlayer player : players)
			{
				world.sendParticles(player, true, packet.getX(), packet.getY(), packet.getZ(), packet);
			}
		}
	}

	static
	{
	}
}
