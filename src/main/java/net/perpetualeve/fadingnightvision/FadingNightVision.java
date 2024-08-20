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
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

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

	public FadingNightVision( ) {
		if (!FMLLoader.getDist( ).isClient( )) return;
		Config config = new Config("fadingnightvision");

		CONFIG = CarbonConfig.CONFIGS.createConfig(config, ConfigSettings.withConfigType(ConfigType.CLIENT)
			.withAutomations(AutomationType.AUTO_RELOAD, AutomationType.AUTO_SYNC, AutomationType.AUTO_LOAD)
			.withBaseFolder(CarbonConfig.CONFIGS.getBasePath( ).resolve("fadingnv")));

		ConfigSection values = new ConfigSection("values");

		FADE_IN_TIME	= values.addDouble("fade_in_time", 3d, "how fast it should fade in").setMax(10d).setMin(0.05d);
		FADE_OUT_TIME	= values.addDouble("fade_out_time", 1.4d, "how fast it should fade out").setMax(10d).setMin(0.05d);
		config.add(values);

		CONFIG.addLoadedListener(( ) ->
		{

		});
		CONFIG.register( );

		IEventBus bus = FMLJavaModLoadingContext.get( ).getModEventBus( );

		MinecraftForge.EVENT_BUS.register(this);
		bus.addListener(this::keyRegister);
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent e) {
		LocalPlayer player = Minecraft.getInstance( ).player;
		if (player == null || e.phase == Phase.END) return;
		nightVisionUpdate(player);

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

	public float nightVisionUpdate(Player player) {
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
