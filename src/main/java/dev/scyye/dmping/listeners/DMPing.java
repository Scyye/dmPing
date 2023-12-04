package dev.scyye.dmping.listeners;

import lombok.Getter;
import dev.scyye.dmping.utils.S2AListener;
import dev.scyye.dmping.utils.TextToSpeechUtil;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static dev.scyye.dmping.utils.APIGetNotNullUtils.*;
import static dev.scyye.dmping.utils.DMPingUtils.*;

public class DMPing extends S2AListener {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getChannelType()==ChannelType.VOICE) {
			String message = event.getMessage().getContentDisplay();
			TextToSpeechUtil.play("%s said %s".formatted(event.getAuthor().getName(), message));
			return;
		}


		if (event.getAuthor().isBot()) {
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



