package ml.scyye.dmping.listeners;

import ml.scyye.dmping.Main;
import ml.scyye.dmping.utils.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// TODO: Make this actually work lol
public class Antidelete extends S2AListener {
/*
	public static class CachedMessage {
		public String  messageId;
		public String  authorId;
		public String  content;

		public CachedMessage(String messageId, String authorId, String content) {
			this.messageId=messageId;
			this.authorId = authorId;
			this.content = content;
		}

		public CachedMessage() {}
	}

	@Override
	public void onMessageDelete(@NotNull MessageDeleteEvent event) {
		if (event.getChannelType() != ChannelType.TEXT || event.getChannelType() == ChannelType.PRIVATE) return;

		CachedMessage cachedMessage = SQLiteUtils.findByMessageId(event.getMessageId());
		if (cachedMessage.authorId==null)
			return;
		if (cachedMessage.authorId.isEmpty())
			return;

		Member member = event.getGuild().retrieveMemberById(cachedMessage.authorId).complete();

		MessageUtils.sendWebhookMessage(event.getChannel().asTextChannel(), cachedMessage.content,
				new MessageUtils.MessageAuthor(member.getEffectiveName()+" (Deleted Message)", member.getEffectiveAvatarUrl()));
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (Arrays.stream(Main.config.getBlacklist()).toList().contains(event.getAuthor().getId())) return;

		if (event.getAuthor().isBot()) {
			return;
		}


		if (event.getMessage().getContentRaw().equals("")) return;
		SQLiteUtils.insertEntry(event.getMessageId(), event.getAuthor().getId(), event.getMessage().getContentRaw());
		// cacheChannel.sendMessage(event.getMessageId() + "," + event.getAuthor().getId() + "," + content).queue();

		SQLiteUtils.insertEntry(event.getMessageId(), event.getAuthor().getId(), event.getMessage().getContentRaw());
	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		if (event.getChannelType()==ChannelType.PRIVATE) return;
		if (!event.getMessage().isEdited()) return;

		EmbedBuilder builder= new EmbedBuilder();
		String originalContent = SQLiteUtils.findByMessageId(event.getMessageId()).content;
		String newContent = event.getMessage().getContentRaw();
		User author = Main.instance.jda.getUserById(event.getAuthor().getId());

		assert author != null;
		builder.setAuthor(author.getEffectiveName())
				.setThumbnail(author.getAvatarUrl())
				.setColor(1)
				.addField("Original Content:", "```txt\n"+originalContent+"\n```", false)
				.addField("New Content:", "```\n"+newContent+"\n```", false)
				.appendDescription(event.getJumpUrl())
		;
	}

	public static class CacheManager {
		public List<CachedMessage> cachedMessages = new LinkedList<>();

		public CacheManager() {
			cachedMessages.addAll(SQLiteUtils.findAllCachedMessages());
		}

		private CachedMessage findMessageById(String id) {
			for (CachedMessage cachedMessage : cachedMessages) {
				if (cachedMessage.messageId.equalsIgnoreCase(id)) return cachedMessage;
			}
			return new CachedMessage();
		}

	}
 */

}
