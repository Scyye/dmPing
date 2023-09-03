package ml.scyye.dmping.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static ml.scyye.dmping.Main.instance;
import static ml.scyye.dmping.Main.print;
import static ml.scyye.dmping.utils.MessageUtils.sendTempMessage;

public class DMPingUtils {
    public static void forwardAttachments(MessageReceivedEvent event, TextChannel channel) {
        if (event.isWebhookMessage()) return;
        if (event.getMessage().getContentRaw().isEmpty() && event.getMessage().getAttachments().isEmpty()) return;


        String messageContent = event.getMessage().getContentRaw();
        for (var word : messageContent.split(" ")) {
            if (word.startsWith("<@")) {
                messageContent = messageContent.replace(word, replacePingWithName(word));
            }
        }

        Message.Attachment[] attachments = new Message.Attachment[event.getMessage().getAttachments().size()];
        event.getMessage().getAttachments().toArray(attachments);


        MessageUtils.sendWebhookMessage(channel, event.getMessage().getContentRaw(), new MessageUtils.MessageAuthor(event.getAuthor().getEffectiveName(),
                event.getAuthor().getEffectiveAvatarUrl()), attachments);
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
                "MESSAGE CONTENT: " + (!event.getMessage().getContentRaw().isEmpty()?event.getMessage().getContentRaw():event.getMessage().getAttachments().size()+" attachments"),

                // Adds spaces in the logs for formatting and readability purposes
                "",
                "=".repeat(120),
                ""
        );
    }

    public static Constants.MentionUsers mentionUsers(MessageReceivedEvent event, @Nullable Predicate<Member> predicate) {
        Message message = event.getMessage();

        if (event.getAuthor().isBot()) return new Constants.MentionUsers("", "");

        List<Member> ping = new ArrayList<>();

        for (Member member : event.getGuild().getMembers()) {
            if (predicate==null) {
                ping.addAll(event.getGuild().getMembers());
                break;
            }
            if (predicate.test(member))
                ping.add(member);
        }

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

    private static String replacePingWithName(String s) {
        return instance.jda.getUserById(s.replace("<", "").replace("@", "").replace(">", "")).getName();
    }

}
