package dev.scyye.dmping.listeners;

import dev.scyye.dmping.utils.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.*;
import org.jetbrains.annotations.NotNull;

// TODO: Make this actually work lol
public class Antidelete extends S2AListener {
	public static class CachedMessage {
		public String  messageId;
		public String  authorId;
		public String  content;
		public String[] attachments;

		public CachedMessage(String messageId, String authorId, String content, String... attachments) {
			this.messageId=messageId;
			this.authorId = authorId;
			this.content = content;
			this.attachments = attachments;
		}

		public CachedMessage() {
			this.messageId= "";
			this.authorId = "";
			this.content = "";
			this.attachments = new String[0];
		}
	}

	@Override
	public void onMessageDelete(@NotNull MessageDeleteEvent event) {
		if (event.isFromType(ChannelType.TEXT) || event.isFromType(ChannelType.PRIVATE)) return;

		CachedMessage cachedMessage = SQLiteUtils.findMessageById(event.getMessageId());
		if (cachedMessage.authorId==null || cachedMessage.authorId.isEmpty()) return;

		event.getGuild().retrieveMemberById(cachedMessage.authorId).queue(member -> {
			MessageUtils.sendWebhookMessage(event.getChannel().asTextChannel(), cachedMessage.content,
					member.getEffectiveName()+" (Deleted Message)", member.getEffectiveAvatarUrl());
		});
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		if (event.getMessage().getContentRaw().isEmpty()) return;
		SQLiteUtils.insertEntry(event.getMessageId(), event.getAuthor().getId(), event.getMessage().getContentRaw());
	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		if (event.isFromType(ChannelType.PRIVATE)) return;
		if (!event.getMessage().isEdited()) return;

		SQLiteUtils.updateEntry(event.getMessageId(), event.getAuthor().getId(), event.getMessage().getContentRaw());
	}
}
