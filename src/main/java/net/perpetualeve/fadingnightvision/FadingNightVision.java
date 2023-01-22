package net.perpetualeve.fadingnightvision;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("resource")
@Mod(FadingNightVision.MODID)
public class FadingNightVision {

	public static final String MODID = "fadingnightvision";

	public float time;
	DoubleValue fade_time;
	public static ForgeConfigSpec CONFIG;
	
	public static float visionProgress = 0.0f;
	public float i = 0;

	public FadingNightVision() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("general");
		builder.comment("How many seconds the fade should take");
		fade_time = builder.defineInRange("fade_time", 5D, 0.01D, Double.MAX_VALUE);
		builder.pop();
		CONFIG = builder.build();
		ModLoadingContext.get().registerConfig(Type.COMMON, CONFIG, "FadingNightVision.toml");

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::onLoad);
		bus.addListener(this::onFileChange);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent e) {
		LocalPlayer player = Minecraft.getInstance().player;
		if(player == null || e.phase == Phase.END) return;
		nightVisionUpdate(player);
	}
	
	public float nightVisionUpdate(Player player) {
		MobEffectInstance mei = player.getEffect(MobEffects.NIGHT_VISION);
		if(mei == null) return visionProgress = 0.0f;
		float duration = mei.getDuration();
		float t = 20*time;
		if(duration > t) {
			return visionProgress = (i = Math.min(++i, t))/t;
		} else if(duration <= 1.25f) {
			return visionProgress = 0.0f;
		} 
		return visionProgress = (i = Math.min(--i, t))/t;
	}

	public void onLoad(ModConfigEvent.Loading configEvent) {
		time = fade_time.get().floatValue();
	}

	public void onFileChange(ModConfigEvent.Reloading configEvent) {
		time = fade_time.get().floatValue();
	}
}
