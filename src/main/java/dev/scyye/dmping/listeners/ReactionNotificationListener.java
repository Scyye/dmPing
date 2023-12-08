package dev.scyye.dmping.listeners;

import dev.scyye.dmping.utils.MessageUtils;
import dev.scyye.dmping.utils.S2AListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;

public class ReactionNotificationListener extends S2AListener {
	@Override
	public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
		event.getJDA().retrieveUserById(event.getMessageAuthorId()).queue(user -> {
			User reactor = event.getUser();
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Reaction Notification");
			builder.setAuthor(reactor.getEffectiveName(), reactor.getAvatarUrl(), reactor.getAvatarUrl());
			event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
				builder.setDescription("%s reacted to [your message](%s) in %s with %s".formatted(
						reactor.getAsMention(),
						message.getJumpUrl(),
						event.getChannel().getAsMention(), event.getReaction().getEmoji().getFormatted()));
				MessageUtils.sendPrivateMessage(user, builder.build());
			});
		});
	}
}
