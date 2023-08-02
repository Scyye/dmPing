package ml.scyye.dmping.utils;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.receive.ReadonlyMessage;
import club.minnced.discord.webhook.send.MessageAttachment;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static ml.scyye.dmping.Main.instance;
import static ml.scyye.dmping.utils.APIGetNotNullUtils.ensureGetWebhookByName;
import static ml.scyye.dmping.utils.LoggingUtils.print;
import static ml.scyye.dmping.utils.MessageUtils.sendTempMessage;
import static ml.scyye.dmping.utils.MessageUtils.sendTempWebhookMessage;
import static ml.scyye.dmping.utils.StringUtils.*;

public class DMPingUtils {
    public static WebhookMessage forwardAttachments(MessageReceivedEvent event, TextChannel channel) {
        if (event.isWebhookMessage()) return null;
        if (event.getMessage().getContentRaw().isEmpty() && event.getMessage().getAttachments().isEmpty()) return null;


        String messageContent = event.getMessage().getContentRaw();
        for (var word : messageContent.split(" ")) {
            if (word.startsWith("<@")) {
                messageContent = messageContent.replace(word, replacePingWithName(word));
            }
        }

        Message.Attachment[] attachments = new Message.Attachment[event.getMessage().getAttachments().size()];
        event.getMessage().getAttachments().toArray(attachments);


        return MessageUtils.sendWebhookMessage(channel, event.getMessage().getContentRaw(), new MessageUtils.MessageAuthor(convertToMediaWebhookName(event.getAuthor()),
                event.getAuthor().getEffectiveAvatarUrl()), attachments).message;
    }

    public static List<File> getFilesFromAttachments(List<Message.Attachment> attachments) {
        List<File> files = new ArrayList<>();
        attachments.forEach(attachment -> {
            InputStream stream = null;
            try {
                stream = attachment.getProxy().download().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String fileName = attachment.getFileName();

            String[] parts = fileName.split("\\.");
            String extension = parts[parts.length-1];

            File tempFile = null;
            try {
                tempFile=File.createTempFile(parts[0], "."+extension);
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert tempFile!=null;
            tempFile.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempFile)){
                assert stream != null;
                IOUtils.copy(stream, fos);
            } catch (IOException ignored) {}

            files.add(tempFile);
        });

        return files;
    }

    public static void logMessage(MessageReceivedEvent event, String pingString, String pingNames) {
        if (instance.dmLink) return;
        print(
                pingString.replace("<", "").replace(">", "").replace("@", ""),
                // Logs the members' Names that it pinged
                pingNames,

                // Logs who sent the message and the guild it was in
                event.getAuthor().getName()+" sent a message in " + event.getGuild().getName(),
                "",

                // Prints some more info about the message
                "MESSAGE INFO:",
                "Message ID: " + event.getMessageId(),
                "Message Time: " + event.getMessage().getTimeCreated().toLocalTime() + event.getMessage().getTimeCreated().getDayOfYear() + " at " + event.getMessage().getTimeCreated().getHour() + ":" + event.getMessage().getTimeCreated().getMinute() + ":" + event.getMessage().getTimeCreated().getSecond(),
                "Channel Type: " + event.getChannelType(),
                "Channel ID: " + event.getChannel().getId(),
                "Channel Name: " + event.getChannel().getName(),
                "Author Name: " + event.getAuthor().getName(),
                "Author ID: " + event.getAuthor().getId(),
                "MESSAGE CONTENT: " + event.getMessage().getContentRaw(),

                // Adds spaces in the logs for formatting and readability purposes
                "",
                line(120, "="),
                ""
        );
    }

    public static Constants.MentionUsers mentionUsers(MessageReceivedEvent event) {
        Message message = event.getMessage();

        if (event.getAuthor().isBot()) return new Constants.MentionUsers("", "");

        List<Member> ping = new ArrayList<>(event.getGuild().getMembers());

        for (Member member : event.getGuild().getMembers()) {
            if (message.getContentRaw().contains(member.getUser().getName()) || message.getMentions().getMembers().contains(member)) {
                ping.remove(member);
            }
        }

        if (message.getContentRaw().contains("@everyone")) {
            return new Constants.MentionUsers("", "");
        }

        // Removes the message sender from the ArrayList
        ping.remove(event.getGuild().getMemberById(event.getAuthor().getId()));

        // Removes dmPing
        ping.remove(message.getGuild().getMemberById(instance.jda.getSelfUser().getId()));

        // If there's no one to ping, it does nothing
        if (ping.isEmpty()) return new Constants.MentionUsers("", "");

        StringBuilder pingStringBuilder = new StringBuilder();
        StringBuilder pingNamesBuilder = new StringBuilder();

        // Gets all the members that it should ping as a mention (ping) and adds them to a string
        for (Member member : ping) {
            pingStringBuilder.append(member.getAsMention()).append(" ");
        }

        // Gets all the members that it should ping as a name (@wumpus) for logging purposes
        for (Member member : ping) {
            pingNamesBuilder.append(member.getEffectiveName()).append(" | ");
        }

        String messageContent = event.getMessage().getContentRaw();
        for (String word : messageContent.split(" ")) {
            if (word.startsWith("<@")) {
                messageContent = messageContent.replace(word, replacePingWithName(word));
            }
        }

        // Pings the members that it should
        sendTempMessage(event.getChannel().asTextChannel(), messageContent+"\n\n"+pingStringBuilder, 0);

        return new Constants.MentionUsers(pingNamesBuilder.toString(), pingStringBuilder.toString());
    }
}
