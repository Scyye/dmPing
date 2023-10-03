package ml.scyye.dmping.utils;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.receive.ReadonlyMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ml.scyye.dmping.utils.APIGetNotNullUtils.ensureGetWebhookByName;
import static ml.scyye.dmping.utils.DMPingUtils.getFilesFromAttachments;

public class MessageUtils {
    // TODO: Remove, unused
    @Deprecated(since = "5.4.3", forRemoval = true)
    public static Message sendPrivateMessage(User user, String content) {
        return user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .complete();
    }

    // TODO: Remove, unused
    @Deprecated(since = "5.4.3", forRemoval = true)
    public static void sendPrivateMessage(User user, MessageEmbed content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(content))
                .complete();
    }

    public static void sendTempMessage(MessageChannel channel, MessageCreateData data, long delay) {
        channel.sendMessage(data).queue(message -> message.delete().queueAfter(delay, TimeUnit.MILLISECONDS));
    }

    /**
     * NO PARAMETER CAN BE NULL, THEY CAN BE EMPTY, BUT NOT NULL.
     *
     * @param channel     The channel to send the message to
     * @param message     The message content (can be empty)
     * @param author      The webhook's username (can be any String)
     * @param attachments A list of attachments to add to the message (can be empty as long as "message" is provided)
     */

    @ParametersAreNonnullByDefault
    public static void sendWebhookMessage(TextChannel channel, String message, MessageAuthor author, Message.Attachment... attachments) {
        List<Message.Attachment> attachmentList = List.of(attachments);
        Webhook webhook = ensureGetWebhookByName(channel, author.name);

        WebhookClientBuilder webhookClientBuilder = new WebhookClientBuilder(webhook.getUrl());
        JDAWebhookClient client = webhookClientBuilder.buildJDA();

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(author.name)
                .setAvatarUrl(author.url)
                ;

        if (!message.isEmpty())
            builder.setContent(message);

        if (!attachmentList.isEmpty())
            getFilesFromAttachments(attachmentList).forEach(builder::addFile);

        var m = client.send(builder.build());
        ReadonlyMessage readonlyMessage = null;

        try {
            readonlyMessage = m.get();
        } catch (Exception e) {
            e.printStackTrace();
        }


        assert readonlyMessage != null;
    }

    // TODO: Find a better way to do this
    public static class MessageAuthor {
        public String name;
        public String url;


        public MessageAuthor(String name, String url) {
            this.name=name;
            this.url=url;
        }
    }
}
