package dev.scyye.dmping.utils;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static dev.scyye.dmping.utils.DMPingUtils.getFilesFromAttachments;

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
        String finalMessage = message;
        channel.retrieveWebhooks().queue(webhooks -> {
            if (webhooks.stream().noneMatch(webhook -> webhook.getName().equals("DMPing")))
                channel.createWebhook("DMPing").queue(webhook -> {
                    WebhookClientBuilder webhookClientBuilder = new WebhookClientBuilder(webhook.getUrl());
                    JDAWebhookClient client = webhookClientBuilder.buildJDA();

                    WebhookMessageBuilder builder = new WebhookMessageBuilder()
                            .setUsername(author)
                            .setAvatarUrl(avatarUrl)
                            ;

                    if (!finalMessage.isEmpty())
                        builder.setContent(finalMessage);

                    if (!attachmentList.isEmpty())
                        getFilesFromAttachments(attachmentList).forEach(builder::addFile);

                    client.send(builder.build());
                });
        });
    }

    static HashMap<String, String[]> proxies = new HashMap<>(){{
       put("invadertedious", new String[]{","});
       put("scyye", new String[]{"syl.", "s.", "S.", ";;"});
    }};
}
