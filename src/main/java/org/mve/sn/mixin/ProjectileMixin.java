package org.mve.sn.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.mve.sn.EnderBow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin
{
	@Inject(
		method = "setOwner(Lnet/minecraft/world/entity/EntityReference;)V",
		at = @At(value = "RETURN")
	)
	public void init(@Nullable EntityReference<Entity> owner, CallbackInfo ci)
	{
		Projectile projectil = (Projectile) (Object) this;
		Level level = projectil.level();
		if (level == null)
			return;
		EnderBow.arrow(projectil, EntityReference.getEntity(owner, level));
	}
}
