package net.perpetualeve.fadingnightvision;

import org.lwjgl.glfw.GLFW;

import carbonconfiglib.CarbonConfig;
import carbonconfiglib.api.ConfigType;
import carbonconfiglib.config.Config;
import carbonconfiglib.config.ConfigEntry.DoubleValue;
import carbonconfiglib.config.ConfigHandler;
import carbonconfiglib.config.ConfigSection;
import carbonconfiglib.config.ConfigSettings;
import carbonconfiglib.utils.AutomationType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(FadingNightVision.MODID)
public class FadingNightVision {

	public static final String MODID = "fadingnightvision";

	public static ConfigHandler	CONFIG;
	public static DoubleValue	FADE_IN_TIME;
	public static DoubleValue	FADE_OUT_TIME;

	public static KeyMapping KEY = new KeyMapping("key.fadingnightvision.toggle_night_vision",
		GLFW.GLFW_KEY_V, "key.categories.misc");

	public static float		visionProgress	= 0.0f;
	public static boolean	active			= false;
	public static boolean	enabled			= true;

	private static boolean pressed = false;

	public FadingNightVision(IEventBus bus, ModContainer modContainer) {
		if (!FMLLoader.getDist( ).isClient( )) return;
		Config config = new Config("fadingnightvision");
		CONFIG = CarbonConfig.CONFIGS.createConfig(config, ConfigSettings.withConfigType(ConfigType.SERVER)
			.withAutomations(AutomationType.AUTO_RELOAD, AutomationType.AUTO_SYNC, AutomationType.AUTO_LOAD));

		ConfigSection values = new ConfigSection("values");

		FADE_IN_TIME	= values.addDouble("fade_in_time", 3d, "how fast it should fade in").setMax(10d).setMin(0.05d);
		FADE_OUT_TIME	= values.addDouble("fade_out_time", 1.4d, "how fast it should fade out").setMax(10d).setMin(0.05d);
		config.add(values);

		CONFIG.addLoadedListener(( ) ->
		{

		});
		CONFIG.register( );

		NeoForge.EVENT_BUS.register(this);
		bus.addListener(this::keyRegister);
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent.Post e) {
		LocalPlayer player = Minecraft.getInstance( ).player;
		if (player == null) return;
		nightVisionUpdate();

		if (KEY.isDown( ) && !pressed) {
			enabled	= !enabled;
			pressed	= true;
		}
		else if (!KEY.isDown( )) {
			pressed = false;
		}
	}

	public void keyRegister(RegisterKeyMappingsEvent event) {
		event.register(KEY);
	}

	public float nightVisionUpdate() {
		if (active && enabled) {
			active = false;
			if (visionProgress >= 1.0f) {
				return 1.0f;
			}
			return (visionProgress = Math.min(visionProgress + (1f / (20f * FADE_IN_TIME.getValue( ).floatValue( ))), 1.0f));
		}
		return (visionProgress = Math.max(visionProgress - (1f / (20f * FADE_OUT_TIME.getValue( ).floatValue( ))), 0.0f));
	}
}
