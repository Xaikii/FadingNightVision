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
import net.minecraft.client.Minecraft;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLanguageProvider;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(FadingNightVision.MODID)
public class FadingNightVision {

	public static final String MODID = "fadingnightvision";

	public static ConfigHandler	CONFIG;
	public static DoubleValue	FADE_IN_TIME;
	public static DoubleValue	FADE_OUT_TIME;

	public static ToggleKeyMapping KEY = new ToggleKeyMapping("key.fadingnightvision.toggle_night_vision",
		GLFW.GLFW_KEY_V, "key.categories.misc", ( ) -> true);

	public static float		visionProgress	= 0.0f;
	public static boolean	active			= false;
	public static boolean	enabled			= true;

	public FadingNightVision( ) {

		if (!FMLLoader.getDist( ).isClient( )) return;
		Config config = new Config("fadingnightvision");
		CONFIG = CarbonConfig.CONFIGS.createConfig(config, ConfigSettings.withConfigType(ConfigType.SERVER)
			.withAutomations(AutomationType.AUTO_RELOAD, AutomationType.AUTO_SYNC, AutomationType.AUTO_LOAD));

		ConfigSection values = new ConfigSection("values");

		FADE_IN_TIME	= values.addDouble("fade_in_time", 5, "how fast it should fade in").setMax(10d).setMin(0.05d);
		FADE_OUT_TIME	= values.addDouble("fade_out_time", 3, "how fast it should fade out").setMax(10d).setMin(0.05d);
		config.add(values);

		MinecraftForge.EVENT_BUS.register(this);

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
	}

	public void keyRegister(RegisterKeyMappingsEvent event) {
		event.register(KEY);
	}

	@SubscribeEvent
	public void input(InputEvent.Key event) {
		if (event.getAction( ) != 0) return;
		if(KEY.getKey( ).getValue( ) == event.getKey( )) {
			System.out.println("bruh");
			enabled = !enabled;
		}
//		KEY.getKey( ).getNumericKeyValue( ).ifPresent(T ->
//		{
//			if (KEY.getKey( ).getValue( ) == event.getKey( ))
//				enabled = !enabled;
//			System.out.println("yo");
//		});
//		if (event.getKeyMapping( ).equals(KEY)) {
//			enabled = !enabled;
//		}
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
