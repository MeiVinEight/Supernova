package org.mve.sn.mixin;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import org.mve.sn.EnderBow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class PersistentProjectileEntityMixin
{
	@Inject(at = @At("HEAD"), method = "onHit")
	private void onCollision(HitResult hitResult, CallbackInfo ci)
	{
		EnderBow.collision((Projectile)(Object) this, hitResult);
	}
}
