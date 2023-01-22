package net.perpetualeve.fadingnightvision.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.perpetualeve.fadingnightvision.FadingNightVision;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	
	@Inject(method = "getNightVisionScale", at = @At("Invoke"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private static void getNightVisionScaleRed(LivingEntity p_109109_, float p_109110_, CallbackInfoReturnable<Float> ci) {
		ci.setReturnValue(FadingNightVision.visionProgress);
	}

}
