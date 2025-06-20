package dev.scyye.dmping.utils;

import dev.scyye.dmping.listeners.DMPing;
import dev.scyye.dmping.listeners.Sub5AlltsOnlyListener;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.io.*;
import java.util.*;

import static dev.scyye.dmping.Main.*;
import static dev.scyye.dmping.utils.MessageUtils.sendTempMessage;

public class DMPingUtils {
    public static void forwardAttachments(MessageReceivedEvent event, TextChannel channel) {
        if (event.isWebhookMessage()) return;
        if (event.getMessage().getContentRaw().isEmpty() && event.getMessage().getAttachments().isEmpty()) return;

        Message.Attachment[] attachments = new Message.Attachment[event.getMessage().getAttachments().size()];
        event.getMessage().getAttachments().toArray(attachments);

        MessageUtils.sendWebhookMessage(channel, event.getMessageId(), event.getAuthor().getEffectiveName(),
                event.getAuthor().getEffectiveAvatarUrl(), attachments);
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
                byte[] bytes = new byte[8192];
                int n;
                while (-1 != (n = stream.read(bytes)))
                    fos.write(bytes, 0, n);}
            catch (IOException e) {
                e.printStackTrace();
            }
            files.add(tempFile);
        });

        return files;
    }

    public static void logMessage(MessageReceivedEvent event, String pingString, String pingNames) {
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

    public static DMPing.MentionUsers mentionUsers(MessageReceivedEvent event) {
        System.out.println("Mentioning users");
        Message message = event.getMessage();

        if (event.getAuthor().isBot()) {
            System.out.println("Author is bot");
            return new DMPing.MentionUsers("", "");
        }

        List<Member> ping = new ArrayList<>(Sub5AlltsOnlyListener.sub5AlltsMembers);

        System.out.println(ping.size());


        ping.removeIf(member -> message.getContentRaw().contains(member.getUser().getName()) || message.getMentions().getMembers().contains(member));
        ping.removeIf(member -> member.getUser().isBot());

        System.out.println(ping.size());

        if (message.getContentRaw().contains("@everyone")) {
            System.out.println("Contains everyone");
            return new DMPing.MentionUsers("", "");
        }

        // Removes the message sender from the ArrayList
        ping.remove(event.getMember());

        // Removes dmPing
        ping.remove(event.getGuild().getSelfMember());

        // If there's no one to ping, it does nothing
        if (ping.isEmpty()) return new DMPing.MentionUsers("", "");

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

        String messageContent = event.getMessage().getContentDisplay();

        // Pings the members that it should
        sendTempMessage(event.getChannel(),
                MessageCreateData.fromContent(messageContent + "\n" + pingStringBuilder),
                0);

        return new DMPing.MentionUsers(pingNamesBuilder.toString(), pingStringBuilder.toString());
    }
}
