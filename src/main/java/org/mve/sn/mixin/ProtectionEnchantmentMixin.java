package org.mve.sn.mixin;

import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.mve.sn.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin
{
	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	public void getMaxLevel(CallbackInfoReturnable<Integer> cir)
	{
		ProtectionEnchantment _this =  (ProtectionEnchantment)(Object) this;
		if (_this.type == ProtectionEnchantment.Type.ALL)
			cir.setReturnValue(Configuration.MAX_LEVEL_PROTECTION.get());
		if (_this.type == ProtectionEnchantment.Type.FIRE)
			cir.setReturnValue(Configuration.MAX_LEVEL_FIREPROTECTION.get());
		if (_this.type == ProtectionEnchantment.Type.FALL)
			cir.setReturnValue(Configuration.MAX_LEVEL_FEATHERFALLING.get());
		if (_this.type == ProtectionEnchantment.Type.EXPLOSION)
			cir.setReturnValue(Configuration.MAX_LEVEL_BLASTPROTECTION.get());
		if (_this.type == ProtectionEnchantment.Type.PROJECTILE)
			cir.setReturnValue(Configuration.MAX_LEVEL_PROJECTILEPROTECTION.get());
	}
}
