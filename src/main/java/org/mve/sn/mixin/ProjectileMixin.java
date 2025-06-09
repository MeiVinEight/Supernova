package org.mve.sn.mixin;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import org.mve.sn.EnderBow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin
{
	@Inject(at = @At("HEAD"), method = "onHit")
	public void onHit(HitResult p_37260_, CallbackInfo ci)
	{
		EnderBow.collision((Projectile)(Object) this, p_37260_);
	}
}
