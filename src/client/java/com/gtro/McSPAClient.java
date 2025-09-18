package com.gtro;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class McSPAClient implements ClientModInitializer {
	public static MCSPAConfig CONFIG;
	public static String AESkey;
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		//createConfigFile();

		try {
			AutoConfig.register(MCSPAConfig.class,me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer::new);
			CONFIG = AutoConfig.getConfigHolder(MCSPAConfig.class).getConfig();
			Debug.isDebug=McSPAClient.CONFIG.debug;
		}catch (Exception e)
		{
			System.out.println("配置文件加载异常!请检查配置文件格式或内容");
			return;
		}

		try {
			AESkey=SecretKeyLoader.loadKey();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	public static void openConfigGUI(Screen parent) {
		MCSPAConfig config = AutoConfig.getConfigHolder(MCSPAConfig.class).getConfig();

		ConfigBuilder builder = ConfigBuilder.create()
				.setTitle(Text.literal("MCSPA 配置"))
				.setParentScreen(parent);

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		builder.getOrCreateCategory(Text.literal("常规设置"))
				.addEntry(entryBuilder.startTextField(Text.literal("连接码"), config.code)
						.setDefaultValue("minecraft")
						.setSaveConsumer(newValue -> config.code = newValue)
						.build());

		// 打开 GUI
		MinecraftClient.getInstance().setScreen(builder.build());
	}


//	public void createConfigFile()
//	{
//		Path configFile = Path.of("config/mcspa.toml");
//		if (!Files.exists(configFile)) {
//			try (InputStream in = getClass().getClassLoader().getResourceAsStream("mcspa.toml")) {
//				if (in != null) {
//					Files.createDirectories(configFile.getParent());
//					Files.copy(in, configFile);
//					System.out.println("已生成默认配置文件 mcspa.toml");
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}