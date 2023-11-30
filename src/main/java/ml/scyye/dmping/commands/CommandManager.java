package ml.scyye.dmping.commands;

import com.github.kaktushose.jda.commands.annotations.interactions.*;
import com.github.kaktushose.jda.commands.dispatching.commands.CommandEvent;
import ml.scyye.dmping.Main;
import net.dv8tion.jda.api.*;

import java.awt.*;
import java.io.*;

@Interaction
public class CommandManager {

	@SlashCommand(value = "shutdown", desc = "Shuts the bot down.", ephemeral = true)
	public void onShutdown(CommandEvent event) {
		if (!event.getUser().getId().equals(Main.config.get("ownerId", String.class))) {
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




	@SlashCommand(value = "original-js", desc = "Displays the original JavaScript version of the bot written by root", ephemeral = true)
	public void onJs(CommandEvent event) throws FileNotFoundException {
		var reader = new BufferedReader(new FileReader("dmping.txt"));
		StringBuilder builder = new StringBuilder();
		for (var str : reader.lines().toList()) {
			builder.append(str).append("\n");
		}
		event.reply("```js\n"+builder+"```");
	}

	@SlashCommand(value = "version", desc = "Tells you the current version of the java port of dmPing currently being used.", ephemeral = true)
	public void onVersion(CommandEvent event) {
		event.reply(Main.config.get("version")+(Main.config.get("beta", Boolean.class)?"-beta":""));
	}

	@SlashCommand(value = "source", desc = "Sends you the github to view the code of the bot.", ephemeral = true)
	public void onSource(CommandEvent event) {
		event.reply("https://github.com/Scyye/dmPing");
	}

	@SlashCommand(value = "github", desc = "Sends you the github to view the code of the bot.", ephemeral = true)
	public void onGithub(CommandEvent event) {
		onSource(event);
	}

	@SlashCommand(value = "say", desc = "Says something as the bot", ephemeral = true)
	public void onSay(CommandEvent event, @Param("message") String message) {
		event.getTextChannel().sendMessage(message).queue();
		event.reply("Sent message:\n"+message);
	}

	@SlashCommand(value = "crazy", desc = "CRAZY")
	public void onCrazy(CommandEvent event) {
		event.reply("Crazy? I was crazy once, they locked me in a room, a rubber room with rats, rats drive me crazy!");
	}

	@SlashCommand(value = "rats", desc = "CRAZY")
	public void onRats(CommandEvent event) {
		onCrazy(event);
	}

	@SlashCommand(value = "changelog", desc = "View the entire history of the bot, with all updates and everything!", ephemeral = true)
	public void onChangeLog(CommandEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder
				.addField("1.0.0", "Added all functionality from root's dmPing bot (do /original-js to view the original code for dmPing by Tessy)", false)
				// .addField("1.1.0", limitWords("Fixed many bugs, including spamming breaking the bot, and the bot infinitely pining itself.", charLimit), false)
				// .addField("2.0.0", limitWords("Added a 'Deleter' which automatically deletes messages sent by the bot every 2-3 minutes, to prevent the bot from clogging channels.", charLimit), false)
				// .addField("2.0.1 - 2.0.16", limitWords("Fixed bugs", charLimit), true)
				.addField("3.0.0", "Implemented the blacklist. /blacklist-add <User> to blacklist someone. /blacklist-remove <User> also works.", false)
				.addField("4.0.0", "Added the Anti-Delete Functionality. Where the bot caches messages, and prevents deletion of messages.", false)
				// .addField("4.1.0", limitWords("Redid most of the commands, added a command that allows listing blacklisted users, as well as responses for trying to add/remove yourself from the blacklist.", charLimit), false)
				.addField("4.2.0", "Added /changelog, /version, and /original-js allowing you to view the entire history of the bot.", false)
				.addField("4.2.1", "Updated TOKEN (thanks <@460904913732370434> bitch), cleaned up the code a bit, and added a MemberHandler that automatically gives <@&1058256598084096070>", false)
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
				.addField("5.4.4", "Make dmping function - no antidelete currently", true)
		;

		builder
				.setAuthor("Scyye")
				.setColor(Color.BLUE)
				.setTitle("**Changelog**");

		Main.instance.jda.retrieveUserById(Main.config.get("ownerId", String.class)).queue(user -> {
			builder.setThumbnail(user.getAvatarUrl());
		});

		event.reply(builder);
	}
}
