package net.perpetualeve.fadingnightvision.mixins;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.perpetualeve.fadingnightvision.FadingNightVision;

@Mixin(value = GameRenderer.class, priority = 123153)
public class GameRendererMixin {

	@WrapMethod(method = "Lnet/minecraft/client/renderer/GameRenderer;getNightVisionScale(Lnet/minecraft/world/entity/LivingEntity;F)F")
	private static float fnv$getNightVisionScale(LivingEntity p_109109_, float pNanoTime, Operation<Float> original) {
		FadingNightVision.active = p_109109_.hasEffect(MobEffects.NIGHT_VISION);
		return FadingNightVision.visionProgress;
	}
}

