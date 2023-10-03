package ml.scyye.dmping;

import com.github.kaktushose.jda.commands.JDACommands;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ml.scyye.dmping.commands.CommandManager;
import ml.scyye.dmping.listeners.*;
import ml.scyye.dmping.utils.Config;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

	public static Main instance;

	public JDA jda;

	public static Config config;

	private Main() throws InterruptedException {
		jda = JDABuilder.createDefault(config.getToken())
				.setActivity(Activity.of(Activity.ActivityType.CUSTOM_STATUS, "dmPing V" +
						config.getVersion()+(config.isBeta()?"-b" : "")))
				.enableCache(CacheFlag.VOICE_STATE)
				.enableIntents(
						GatewayIntent.MESSAGE_CONTENT,
						GatewayIntent.GUILD_MEMBERS,
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_VOICE_STATES
				)
				.addEventListeners(
						new DMPing(),
						new Sub5AlltsOnlyListener(),
						new VcJoinListener()
				)
				.build()
				.awaitReady();
	}

	public static void main(String[] args) throws InterruptedException {
		loadConfig();
		instance=new Main();
		postInit();
	}

	static void loadConfig() {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final Path configFilePath = Path.of("src","main", "resources", "config.json");

		final File configFile = configFilePath.toFile();
		try {
			if (!configFile.exists()) {
				if (!configFile.createNewFile())
					loadConfig();
				Files.writeString(configFilePath, gson.toJson(Config.defaultConfig(), Config.class));
			}
			config = gson.fromJson(Files.readString(configFilePath), Config.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void postInit() {
		String commandsPackage = CommandManager.class.getPackageName();


		JDACommands.start(instance.jda, instance.getClass(), commandsPackage, commandsPackage+".music");

		Main.instance.jda.getGuilds().get(0).getSelfMember().modifyNickname("dmPing"+(config.isBeta()?" V"+config.getVersion()+"-beta":"")).queue();
	}



	public static void print(String... strings) {
		System.out.println(String.join("\n", strings));
	}

}
