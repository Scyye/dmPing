package ml.scyye.dmping.utils;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ml.scyye.dmping.utils.APIGetNotNullUtils.ensureGetWebhookByName;
import static ml.scyye.dmping.utils.DMPingUtils.getFilesFromAttachments;

public class MessageUtils {
    public static void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue();
    }

    public static void sendPrivateMessage(User user, MessageEmbed content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(content))
                .queue();
    }

    public static void sendTempMessage(MessageChannel channel, MessageCreateData data, long delay) {
        channel.sendMessage(data).queue(message -> message.delete().queueAfter(delay, TimeUnit.MILLISECONDS));
    }

    /**
     * @param channel     The channel to send the message to
     * @param message     The message content (can be empty)
     * @param author      The webhook's username (can be any String)
     * @param attachments A list of attachments to add to the message (can be empty as long as "message" is provided)
     */

    public static void sendWebhookMessage(@NonNull TextChannel channel, String message, String author, String avatarUrl, Message.Attachment... attachments) {
        if (message==null) message="";
        if (attachments==null) attachments=new Message.Attachment[0];

        List<Message.Attachment> attachmentList = List.of(attachments);
        Webhook webhook = ensureGetWebhookByName(channel, author);

        WebhookClientBuilder webhookClientBuilder = new WebhookClientBuilder(webhook.getUrl());
        JDAWebhookClient client = webhookClientBuilder.buildJDA();

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(author)
                .setAvatarUrl(avatarUrl)
                ;

        if (!message.isEmpty())
            builder.setContent(message);

        if (!attachmentList.isEmpty())
            getFilesFromAttachments(attachmentList).forEach(builder::addFile);

        client.send(builder.build());
    }
}
