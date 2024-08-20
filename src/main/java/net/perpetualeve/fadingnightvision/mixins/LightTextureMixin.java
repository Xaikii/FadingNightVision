package net.perpetualeve.fadingnightvision.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.renderer.LightTexture;
import net.perpetualeve.fadingnightvision.FadingNightVision;

@Mixin(value = LightTexture.class, priority = 13)
public class LightTextureMixin {

	@ModifyExpressionValue(method = "Lnet/minecraft/client/renderer/LightTexture;updateLightTexture(F)V", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 1))
	public float fnv$nightVisionEffect(float original) {
		return Math.max(FadingNightVision.visionProgress, original);
	}
	
}
