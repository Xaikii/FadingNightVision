package net.perpetualeve.fadingnightvision;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FadingNightVision.MODID)
public class FadingNightVision {

	public static final String MODID = "fadingnightvision";

	public static float time;
	public static int layer;
	DoubleValue fade_time;
	IntValue callLayer;
	public static ForgeConfigSpec CONFIG;

	public FadingNightVision() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("general");
		builder.comment("How many seconds the fade should take");
		fade_time = builder.defineInRange("fade_time", 5D, 0.01D, Double.MAX_VALUE);
		builder.comment("DO NOT TOUCH IF YOU DON'T KNOW WHAT THIS DOES. When there is a mod that adds another layer of calls to NightVision/or removes one");
		callLayer = builder.defineInRange("layer", 4, 1, Integer.MAX_VALUE);
		builder.pop();
		CONFIG = builder.build();
		ModLoadingContext.get().registerConfig(Type.COMMON, CONFIG, "FadingNightVision.toml");

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::onLoad);
		bus.addListener(this::onFileChange);
	}

	public void onLoad(ModConfig.Loading configEvent) {
		Minecraft instance = Minecraft.getInstance();
		layer = ((IGameRendererMixin) instance.gameRenderer).overwriteLayer(callLayer.get().intValue());
		time = ((IGameRendererMixin) instance.gameRenderer).overwriteTime(20*layer*fade_time.get().floatValue());
	}

	public void onFileChange(ModConfig.Reloading configEvent) {
		Minecraft instance = Minecraft.getInstance();
		layer = ((IGameRendererMixin) instance.gameRenderer).overwriteLayer(callLayer.get().intValue());
		time = ((IGameRendererMixin) instance.gameRenderer).overwriteTime(20*layer*fade_time.get().floatValue());
	}
}
