package ml.scyye.dmping.listeners;

import lombok.Getter;
import ml.scyye.dmping.utils.S2AListener;
import ml.scyye.dmping.utils.TextToSpeechUtil;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

import static ml.scyye.dmping.Main.config;
import static ml.scyye.dmping.utils.APIGetNotNullUtils.*;
import static ml.scyye.dmping.utils.DMPingUtils.*;

public class DMPing extends S2AListener {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (Arrays.stream(config.get("blacklist", String[].class)).toList().contains(event.getAuthor().getId())) {
			event.getMessage().delete().queue();
			logMessage(event, "**USER IS BLACKLISTED**", "**USER IS BLACKLISTED**");
		}

		if (event.getChannelType()==ChannelType.VOICE) {
			String message = event.getMessage().getContentDisplay();
			TextToSpeechUtil.play("%s said %s".formatted(event.getAuthor().getName(), message));
			return;
		}


		if (event.getAuthor().isBot()  || Arrays.stream(config.get("blacklist", String[].class)).toList().contains(event.getAuthor().getId())) {
			return;
		}

		if (!event.getMessage().getAttachments().isEmpty()) {
			forwardAttachments(event, ensureGetChannelByName(event.getGuild(), "media-stuff"));
			return;
		}

		// Gets all users it should mention
		MentionUsers mentionUsers = mentionUsers(event);

		// Logs everything
		logMessage(event, mentionUsers.getPingString(), mentionUsers.getPingTags());
	}

	@Getter
	public static class MentionUsers{
		String pingTags;
		String pingString;

		public MentionUsers(String pingTags, String pingString) {
			this.pingTags = pingTags;
			this.pingString = pingString;
		}
	}
}



