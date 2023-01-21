package net.perpetualeve.fadingnightvision.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.perpetualeve.fadingnightvision.IGameRendererMixin;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IGameRendererMixin {
	
	private static float i = 0.0f;
	private static int l;
	private static float t;
	
	@Inject(method = "getNightVisionScale", at = @At("Invoke"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private static void getNightVisionScaleRed(LivingEntity p_109109_, float p_109110_, CallbackInfoReturnable<Float> ci) {
		float time = p_109109_.getEffect(MobEffects.NIGHT_VISION).getDuration();
		if(time >= t/l) {
			ci.setReturnValue(Math.min(++i/t, 1.0f)); 
		} else if(time <= 1.25f) {
			ci.setReturnValue(i = 0);
		} else {
			ci.setReturnValue((1f+(i = Mth.clamp(--i, 0.0f, t))/(1f+t)));
		}
	}

	@Override
	public float overwriteTime(float num) {
		return t = num;
	}

	@Override
	public int overwriteLayer(int num) {
		return l = num;
	}
	
	
}
