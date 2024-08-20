package net.perpetualeve.fadingnightvision.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.renderer.FogRenderer;
import net.perpetualeve.fadingnightvision.FadingNightVision;

@Mixin(value = FogRenderer.class, priority = 23)
public class FogRendererMixin {

	@ModifyExpressionValue(method = "Lnet/minecraft/client/renderer/FogRenderer;setupColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IF)V", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 15))
	private static float fnv$nightVisionEffect(float original) {
		return Math.max(FadingNightVision.visionProgress, original);
	}
}
