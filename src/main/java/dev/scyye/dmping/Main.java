package dev.scyye.dmping;

import botcommons.config.Config;
import com.github.kaktushose.jda.commands.JDACommands;
import dev.scyye.dmping.listeners.*;
import dev.scyye.dmping.utils.SQLiteUtils;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;

public class Main {

	public static Main instance;

	public JDA jda;

	public static Config config;

	private Main() throws InterruptedException {
		config = Config.makeConfig(new HashMap<>(){{
			put("version", "VERSION");
			put("beta", false);
			put("webhookUrl", "WEBHOOK_URL");
		}}, "dmping");

		jda = JDABuilder.createDefault(config.get("token", String.class))
				.setActivity(Activity.of(Activity.ActivityType.CUSTOM_STATUS,
						// dmPing V5.4.5
						"dmPing V" +config.get("version")
								+

								(
										config.get("beta", Boolean.class)
												?
												"-b"
												:
												""
								)))


				.enableIntents(
						GatewayIntent.MESSAGE_CONTENT,
						GatewayIntent.GUILD_MEMBERS,
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_VOICE_STATES
				)
				.addEventListeners(
						new DMPing(),
						new Sub5AlltsOnlyListener(),
						new VcJoinListener(),
						new Antidelete(),
						new ReactionNotificationListener()
				)
				.build()
				.awaitReady();
	}

	public static void main(String[] args) throws InterruptedException {
		instance = new Main();

		JDACommands.start(instance.jda, instance.getClass(), "dev.scyye.dmping.commands");
		SQLiteUtils.init();

		Main.instance.jda.getGuilds().get(0).getSelfMember().modifyNickname
				// "dmPing" if not beta, "dmPing VERSION-beta" if beta
				("dmPing"+ (config.get("beta", Boolean.class)? " V"+config.get("version")+"-beta":""))
				.queue();
	}


	public static void print(String... strings) {
		System.out.println(String.join("\n", strings));
	}

}
