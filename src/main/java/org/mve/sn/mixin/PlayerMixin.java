package org.mve.sn.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.mve.sn.Configuration;
import org.mve.sn.KeyboardHandler;
import org.mve.sn.Supernova;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin implements KeyboardHandler
{
	@Shadow
	public abstract void startFallFlying();

	@Unique
	private boolean keyUp = false;
	@Unique
	private boolean keyClimbing = false;

	@Inject(
		method = "getProjectile",
		at = @At("RETURN"),
		cancellable = true
	)
	public void getProjectile(ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir)
	{
		if (!Configuration.INFINITY_FIX.get()) return;
		Player player = (Player) (Object) this;
		if (player.level().isClientSide) return;
		if (cir.getReturnValue() != null && !cir.getReturnValue().isEmpty()) return;
		ItemStack item = Items.ARROW.getDefaultInstance();
		Holder.Reference<Enchantment> infinity = player.level()
			.registryAccess()
			.lookupOrThrow(Registries.ENCHANTMENT)
			.getOrThrow(Enchantments.INFINITY);
		if (itemStack.getEnchantments().getLevel(infinity) > 0)
			cir.setReturnValue(item);
	}

	@Inject(
		method = "tryToStartFallFlying",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Player;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;",
			ordinal = 0
		),
		cancellable = true
	)
	public void tryToStartFallFlying$is0(CallbackInfoReturnable<Boolean> cir)
	{
		Player player = (Player) (Object) this;
		if (!Supernova.glidable(player)) return;
		this.startFallFlying();
		cir.setReturnValue(true);
	}

	@Inject(
		method = "aiStep",
		at = @At("HEAD")
	)
	public void aiStep0(CallbackInfo ci)
	{
		this.supernova$gliding();
	}

	@ModifyArg(
		method = "updatePlayerPose",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Player;setPose(Lnet/minecraft/world/entity/Pose;)V",
			ordinal = 0
		)
	)
	public Pose updatePlayerPose$setPose(Pose par1)
	{
		if (this.keyClimbing()) return Pose.SWIMMING;
		return par1;
	}

	@Override
	public boolean keyUp()
	{
		return this.keyUp;
	}

	@Override
	public void keyUp(boolean v)
	{
		this.keyUp = v;
	}

	@Override
	public boolean keyClimbing()
	{
		return this.keyClimbing;
	}

	@Override
	public void keyClimbing(boolean v)
	{
		this.keyClimbing = v;
	}

	@Unique
	private void supernova$gliding()
	{
		Player player = (Player) (Object) this;
		if (!player.isFallFlying()) return;
		if (!Supernova.glidable(player)) return;
		if (!this.keyUp()) return;
		Vec3 vec3 = player.getLookAngle();
		double d = 1.5;
		double e = 0.1;
		Vec3 vec32 = player.getDeltaMovement();
		Vec3 vec33 = vec3.multiply(e, e, e);
		Vec3 vec34 = vec3.multiply(d, d, d);
		player.setDeltaMovement(vec32.add(vec33.add(vec34.subtract(vec32)).multiply(0.5, 0.5, 0.5)));
	}
}
