package ml.scyye.dmping;

import com.github.kaktushose.jda.commands.JDACommands;
import dev.scyye.botcommons.config.Config;
import ml.scyye.dmping.commands.CommandManager;
import ml.scyye.dmping.listeners.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.HashMap;

public class Main {

	public static Main instance;

	public JDA jda;

	public static Config config;

	private Main() throws InterruptedException {
		config = Config.makeConfig(new HashMap<>(){{
			put("token", "TOKEN");
			put("guildId", "GUILD_ID");
			put("ownerId", "OWNER_ID");
			put("version", "VERSION");
			put("beta", false);
			put("devMode", false);
			put("blacklist", new String[]{});
		}}, "dmping-assets/config");

		jda = JDABuilder.createDefault(config.get("token", String.class))
				.setActivity(Activity.of(Activity.ActivityType.CUSTOM_STATUS, "dmPing V" +
						config.get("version")+(config.get("beta", Boolean.class) ?"-b" : "")))
				.enableCache(
						CacheFlag.VOICE_STATE
				)
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
		instance=new Main();
		String commandsPackage = CommandManager.class.getPackageName();


		JDACommands.start(instance.jda, instance.getClass(), commandsPackage, commandsPackage+".music");

		Main.instance.jda.getGuilds().get(0).getSelfMember().modifyNickname
				// "dmPing" if not beta, "dmPing VERSION-beta" if beta
				("dmPing"+(config.get("beta", Boolean.class)?" V"+config.get("version")+"-beta":"")).queue();
	}


	public static void print(String... strings) {
		System.out.println(String.join("\n", strings));
	}

}
