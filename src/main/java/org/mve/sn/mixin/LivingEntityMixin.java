package org.mve.sn.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.mve.sn.Configuration;
import org.mve.sn.Sounds;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getJumpPower()F", cancellable = true)
	public void getJumpPower(CallbackInfoReturnable<Float> cir)
	{
		if (!Configuration.SUPER_JUMP.get())
			return;
		LivingEntity _this = (LivingEntity) (Object) this;
		if (!(_this instanceof Player player))
			return;
		float jumpPower = cir.getReturnValue();
		if (player.isShiftKeyDown())
			cir.setReturnValue(jumpPower * Configuration.SUPER_JUMP_SCALE.get().floatValue());
	}

	@Inject(
		method = "dropExperience()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
		),
		cancellable = true,
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void dropExperience(CallbackInfo ci, int reward)
	{
		if (!Configuration.KILLER_PICKUP.get())
			return;
		LivingEntity _this = (LivingEntity) (Object) this;
		LivingEntity killer = _this.getKillCredit();
		if (!(killer instanceof ServerPlayer player))
			return;
		ItemStack item0 = player.getMainHandItem();
		ItemStack item1 = player.getOffhandItem();
		if (!(Supernova.check(item0, Supernova.SUPERNOVA_KILLER_PICKUP) || Supernova.check(item1, Supernova.SUPERNOVA_KILLER_PICKUP)))
			return;
		// Supernova.LOGGER.info("Dropping experience {}", reward);
		player.giveExperiencePoints(reward);
		float pitch = (Sounds.RANDOM.nextFloat() - Sounds.RANDOM.nextFloat()) * 0.35F + 0.9F;
		Sounds.play(player, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, pitch);
		ci.cancel();
	}

	@Inject(
		method = "dropAllDeathLoot",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V"
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void dropAllDeathLoot(DamageSource p_21192_, CallbackInfo ci, Entity entity, int i, boolean flag, Collection<ItemEntity> drops)
	{
		if (!Configuration.KILLER_PICKUP.get())
			return;
		if (!(entity instanceof ServerPlayer player))
			return;
		ItemStack item0 = player.getMainHandItem();
		ItemStack item1 = player.getOffhandItem();
		if (!(Supernova.check(item0, Supernova.SUPERNOVA_KILLER_PICKUP) || Supernova.check(item1, Supernova.SUPERNOVA_KILLER_PICKUP)))
			return;
		for (ItemEntity drop : drops)
			ItemHandlerHelper.giveItemToPlayer(player, drop.getItem());
		drops.clear();
	}
}
