package dev.scyye.dmping.commands;

import botcommons.commands.Command;
import botcommons.commands.CommandHolder;
import botcommons.commands.GenericCommandEvent;
import botcommons.commands.Param;
import dev.scyye.dmping.Main;
import net.dv8tion.jda.api.*;

import java.awt.*;
import java.io.*;

@CommandHolder
@SuppressWarnings("unused")
public class CommandManager {

	@Command(name = "shutdown", help = "Shuts the bot down.")
	public static void onShutdown(GenericCommandEvent event) {
		if (!event.getUser().getId().equals(Main.config.get("owner-id", String.class))) {
			event.reply("You can't do that!");
			System.out.println(event.getUser().getEffectiveName()+ " Attempted to shutdown the bot, but wasn't cool enough!");
			return;
		}
		System.out.println(event.getUser().getEffectiveName()+ " Successfully shutdown the bot, they must be cool!");
		event.getJDA().getPresence().setStatus(OnlineStatus.OFFLINE);
		event.getJDA().getHttpClient().connectionPool().evictAll();
		event.getJDA().getHttpClient().dispatcher().executorService().shutdown();
		event.getJDA().shutdown();
	}

	@Command(name = "restart", help = "Restarts the bot.")
	public static void onRestart(GenericCommandEvent event,
								 @Param(description = "params") String params) throws InterruptedException {
		onShutdown(event);

		if (event.getJDA().awaitShutdown()) {
			Main.main(params.split(" "));
		}
	}

	@Command(name = "original-js", help = "Displays the original JavaScript version of the bot written by root")
	public static void onJs(GenericCommandEvent event) throws FileNotFoundException {
		var reader = new BufferedReader(new FileReader("dmping-assets/dmping.txt"));
		StringBuilder builder = new StringBuilder();
		for (var str : reader.lines().toList()) {
			builder.append(str).append("\n");
		}
		event.reply("```js\n"+builder+"```").ephemeral().finish();
	}

	@Command(name = "version", help = "Tells you the current version of the java port of dmPing currently being used.")
	public static void onVersion(GenericCommandEvent event) {
		event.reply(Main.config.get("version")+(Main.config.get("beta", Boolean.class)?"-beta":"")).ephemeral().finish();
	}

	@Command(name = "source", help = "Sends you the github to view the code of the bot.")
	public static void onSource(GenericCommandEvent event) {
		event.reply("https://github.com/Scyye/dmPing").ephemeral().finish();
	}

	@Command(name = "github", help = "Sends you the github to view the code of the bot.")
	public static void onGithub(GenericCommandEvent event) {
		onSource(event);
	}

	@Command(name = "say", help = "Says something as the bot")
	public static void onSay(GenericCommandEvent event, @Param(description = "message") String message) {
		event.getChannel().sendMessage(message).queue();
		event.reply("Sent message:\n"+message).ephemeral().finish();
	}

	@Command(name = "crazy", help = "CRAZY")
	public static void onCrazy(GenericCommandEvent event) {
		event.reply("Crazy? I was crazy once, they locked me in a room, a rubber room with rats, rats drive me crazy!").finish();
	}

	@Command(name = "rats", help = "CRAZY")
	public static void onRats(GenericCommandEvent event) {
		onCrazy(event);
	}

	@Command(name = "changelog", help = "View the entire history of the bot, with all updates and everything!")
	public static void onChangeLog(GenericCommandEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder
				.addField("1.0.0", "Added all functionality from root's dmPing bot (do /original-js to view the original code for dmPing by Tessy)", false)
				// .addField("1.1.0", limitWords("Fixed many bugs, including spamming breaking the bot, and the bot infinitely pining itself.", charLimit), false)
				// .addField("2.0.0", limitWords("Added a 'Deleter' which automatically deletes messages sent by the bot every 2-3 minutes, to prevent the bot from clogging channels.", charLimit), false)
				// .addField("2.0.1 - 2.0.16", limitWords("Fixed bugs", charLimit), true)
				// .addField("3.0.0", "Implemented the blacklist. /blacklist-add <User> to blacklist someone. /blacklist-remove <User> also works.", false)
				.addField("4.0.0", "Added the Anti-Delete Functionality. Where the bot caches messages, and prevents deletion of messages.", false)
				// .addField("4.1.0", limitWords("Redid most of the commands, added a command that allows listing blacklisted users, as well as responses for trying to add/remove yourself from the blacklist.", charLimit), false)
				.addField("4.2.0", "Added /changelog, /version, and /original-js allowing you to view the entire history of the bot.", false)
				.addField("4.2.1", "Updated TOKEN (thanks <@460904913732370434> bitch), cleaned up the code a bit.", false)
				// .addField("4.3.0", limitWords("Removed a bunch of spaghetti code, cleaned up the code even more. Added /github (/source) that sends you the github for the bot. Added a check for everyone pings (double pinging used to happen. As well as starting to add support for other guilds to use the bot; coming in the next full update.", charLimit), false)
				// .addField("4.4.0", limitWords("Added edit logging to the AntiDelete functionality (CACHING MUST BE ENABLED FOR THIS TO WORK). Added a few more things to prepare for public release 1. The bot now joins general in Sub5Allts whenever it goes online.", charLimit), false)
				// .addField("4.4.1", limitWords("Added more things preparing for the public release. It will all be tested soon, and 5.0 will come out then.", charLimit), false)
				// .addField("4.4.2", limitWords("Updated a few things to do with caching. Removed all support for the public release, dmPing Universal will be it's own separate bot. Added a private message link, allowing me to link myself to the bot through DMs, this will be added to over the next few updates. Started work on moving to a new command framework.", charLimit), false)
				.addField("4.5.0", "Changed all commands to use a new framework. Updated caching and fixed it to work properly. Removed root from the author list. Removed the Deleter added in 2.0, as it was obsolete.", false)
				// .addField("5.0.0", limitWords("Added music functionality! Do /audio for more info. All future changelogs regarding UniPing (Universal dmPing) will be through the /changelog command in that bot.", charLimit), false)
				// .addField("5.1.0", limitWords("Scrap that last update: /audio is now removed from the bot, as i feel it doesn't fit dmPing.", charLimit), false)
				// .addField("5.1.1", limitWords("Fixed a typo, and fixed a problem with the /blacklist remove command not working.", charLimit), false)
				// .addField("5.2.0", "Made changelog more readable; removed obsolete versions. Reworked media. /help command is coming soon.", false)
				.addField("5.3.0", "Changed deleted messages to webhooks. Changed the way the version system works. Started reworking caching. Started work on removing the guest system.", false)
				//.addField("5.4.0", "Moved to new guild. Changed caching to use a database, removed guests. Next update will add /help.", false)
				//.addField("5.4.1/5.4.2", "Bug fixes, cleaning code, etc.", false)
				.addField("5.4.3", "Updated JDA, added custom status, cleaned code a bit, deprecated stuff", false)
				.addField("5.4.4", "Make dmping function - no antidelete currently, removed blacklist.", true)
				.addField("5.4.5", "Readd anti-delete, make pinging work with media, updated dependencies.", true)
				.addField("6.0.0", "Moved to BotCommons for commands; Fixed antidelete and other issues", false)
		;

		builder
				.setAuthor("Scyye")
				.setColor(Color.BLUE)
				.setTitle("**Changelog**");

		Main.instance.jda.retrieveUserById(Main.config.get("owner-id", String.class)).queue(user ->
				builder.setThumbnail(user.getAvatarUrl()));

		event.replyEmbed(builder).ephemeral().finish();
	}
}
