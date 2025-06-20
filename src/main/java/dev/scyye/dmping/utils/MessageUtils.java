package dev.scyye.dmping.utils;

import botcommons.config.Config;
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
    public static void sendWebhookMessage(@NonNull TextChannel channel, @NonNull String message, String author, String avatarUrl, Message.Attachment... attachments) {
        if (attachments==null) attachments=new Message.Attachment[0];

        List<Message.Attachment> attachmentList = List.of(attachments);
        channel.retrieveWebhooks().queue(webhooks -> {
            String webhookUrl = Config.getInstance().get("webhookUrl");
            for (var wh : webhooks) {
                if (wh.getUrl().equals(webhookUrl)) {
                    System.out.println("Match");
                    System.out.println(wh.getUrl());
                    System.out.println(webhookUrl);
                } else{
                    System.out.println("No match");
                    System.out.println(wh.getUrl());
                    System.out.println(webhookUrl);
                }
            }
            if (webhooks.stream().anyMatch(webhook -> webhook.getUrl().equals(webhookUrl))) {
                JDAWebhookClient client = new WebhookClientBuilder(webhookUrl).buildJDA();
                WebhookMessageBuilder builder = new WebhookMessageBuilder()
                        .setUsername(author)
                        .setAvatarUrl(avatarUrl)
                        .setContent(message);
                getFilesFromAttachments(attachmentList).forEach(builder::addFile);

                client.send(builder.build());
            }
        });
    }
}
