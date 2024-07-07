package net.perpetualeve.fadingnightvision.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.perpetualeve.fadingnightvision.FadingNightVision;

@Mixin(value = GameRenderer.class, priority = 123153)
public class GameRendererMixin {

	@ModifyReturnValue(method = "getNightVisionScale", at = @At("RETURN"))
	private static float fnv$tNightVisionScale(float original, LivingEntity p_109109_, float pNanoTime) {
		FadingNightVision.active = true;
		return FadingNightVision.visionProgress;
	}
}
