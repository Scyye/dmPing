package ml.scyye.dmping.listeners;

import ml.scyye.dmping.utils.MessageUtils;
import ml.scyye.dmping.utils.S2AListener;
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
			builder.setDescription("""
					%s reacted to [your message](%s) in %s with %s
					""".formatted(reactor.getAsMention(),
					"https://discord.com/channels/"+event.getGuild().getId()+"/"+event.getChannel().getId()+"/"+event.getMessageId(),
					event.getChannel().getAsMention(), event.getReaction().getEmoji().getFormatted()));
			MessageUtils.sendPrivateMessage(user, builder.build());
		});
	}
}
