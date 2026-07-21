package org.mve.sn.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.mve.sn.EnderBow;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin
{
	@Inject(
		method = "setOwner",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/projectile/Projectile;cachedOwner:Lnet/minecraft/world/entity/Entity;",
			opcode = Opcodes.PUTFIELD,
			shift = At.Shift.AFTER
		)
	)
	public void init(Entity entity, CallbackInfo ci)
	{
		EnderBow.arrow((Entity) (Object) this, entity);
	}
}
