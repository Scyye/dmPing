package ml.Scyye.dmPing;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static ml.Scyye.dmPing.Main.instance;

public class ScyyeThings {
    // The bot token
    public static String TOKEN = "";
    public static boolean cache = true;

    // Easy print util
    public static void print(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String st : strings) {
            builder.append(st).append("\n");
            System.out.println(st);
        }

        if (!builder.toString().isEmpty()) sendMessage(Scyye.user, builder.toString());
    }

    public static Role ensureGetRole(@NotNull Guild guild, String roleName, boolean ignoreCase) {
        var roles = guild.getRolesByName(roleName, ignoreCase);
        if (!roles.isEmpty()) return roles.get(0);
        return guild.createRole().setName(roleName).complete();
    }

    public static TextChannel ensureGetChannel(@NotNull Guild guild, String channelName, boolean ignoreCase) {
        var channels = guild.getTextChannelsByName(channelName, ignoreCase);
        if (!channels.isEmpty()) return channels.get(0);
        return guild.createTextChannel(channelName).complete();
    }

    public static TextChannel ensureGetChannel(@NotNull Guild guild, String channelName, String category, boolean ignoreCase) {
        List<TextChannel> channels = guild.getTextChannelsByName(channelName, ignoreCase);
        List<Category> categories = guild.getCategoriesByName(category, ignoreCase);
        if (!channels.isEmpty() && !categories.isEmpty()) return channels.get(0);
        if (channels.isEmpty()) return categories.get(0).createTextChannel(channelName).complete();
        return guild.createCategory(category).complete().createTextChannel(channelName).complete();
    }

    // The ping blacklist
    public static List<String> blacklist = new ArrayList<>();

    // Makes a line out of object with specified length
    @NotNull
    public static String line(int length, @NotNull String object) {
        return object.repeat(Math.max(0, length));
    }

    public static Message forwardAttachments(MessageReceivedEvent event, MessageChannel channelToSendTo) {
        MessageCreateAction messageCreateAction = null;
        for (Message.Attachment attachment:event.getMessage().getAttachments()) {
            InputStream attachmentStream = null;
            try {
                attachmentStream = attachment.getProxy().download().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            String fileName = attachment.getFileName();

            String[] parts = fileName.split("\\.");
            String extension = parts[parts.length - 1];

            File tempFile = null;
            try {
                tempFile = File.createTempFile(parts[0], "." + extension);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert tempFile != null;
            tempFile.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                assert attachmentStream != null;
                IOUtils.copy(attachmentStream, fos);
            } catch (IOException ignored) {
            }

            FileUpload fileUpload = FileUpload.fromData(tempFile);

            messageCreateAction = channelToSendTo.sendMessage(event.getAuthor().getAsTag() + ": " + event.getMessage().getContentDisplay()).addFiles(Collections.singletonList(fileUpload));
        }
        assert messageCreateAction != null;
        return messageCreateAction.complete();
    }


    public static String limitWords(String originalText, int charLimit) {
        StringBuilder output = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();

        for (String word : originalText.split(" ")) {
            if (currentLine.length() + word.length() <= charLimit) {
                // If the word fits on the current line, add it to the line
                currentLine.append(word).append(" ");
            } else if (word.length() > charLimit) {
                // If the word is longer than the character limit, split it into multiple lines
                for (int i = 0; i < word.length(); i += charLimit) {
                    int endIndex = Math.min(i + charLimit, word.length());
                    String subWord = word.substring(i, endIndex);
                    if (currentLine.length() + subWord.length() > charLimit) {
                        output.append(currentLine).append("\n");
                        currentLine = new StringBuilder();
                    }
                    currentLine.append(subWord).append(" ");
                }
            } else {
                // If the word doesn't fit on the current line, finish the word and start a new line
                output.append(currentLine).append("\n");
                currentLine = new StringBuilder(word).append(" ");
            }
        }

        return output.append(currentLine).toString();
    }

    public static Message sendMessage(User user, String content) {
        return user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .complete();
    }

    public static Message sendMessage(User user, MessageEmbed content) {
        return user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(content))
                .complete();
    }


    public static String convertToShort(int num) {
        if (num % 1000000 == 0) {
            return num/1000000+"m";
        } else
        if (num % 1000 == 0) {
            return num/1000+"k";
        } else {
            return Integer.toString(num);
        }
    }

    public static void logMessage(MessageReceivedEvent event, String pingst, String pingtags) {
        print(pingst,
                // Logs the members' Tags that it pinged
                pingtags,

                // Logs who sent the message and the guild it was in
                event.getAuthor().getAsTag()+" sent a message in " + event.getGuild().getName(),
                "",

                // Prints some more info about the message
                "MESSAGE INFO:",
                "Message ID: " + event.getMessageId(),
                "Message Time: " + event.getMessage().getTimeCreated().toLocalTime() + event.getMessage().getTimeCreated().getDayOfYear() + " at " + event.getMessage().getTimeCreated().getHour() + ":" + event.getMessage().getTimeCreated().getMinute() + ":" + event.getMessage().getTimeCreated().getSecond(),
                "Channel Type: " + event.getChannelType(),
                "Channel ID: " + event.getChannel().getId(),
                "Channel Name: " + event.getChannel().getName(),
                "MESSAGE CONTENT: " + event.getMessage().getContentRaw(),

                // Adds spaces in the logs for formatting and readability purposes
                "",
                line(120, "="),
                ""
                );
    }

    public static MentionUsers mentionProperUsers(MessageReceivedEvent event) {
        Message MESSAGE = event.getMessage();
        User AUTHOR = event.getAuthor();

        List<Member> ping = new ArrayList<>();

        for (Member notpinged : event.getGuild().getMembers()) {
            if (!MESSAGE.getMentions().getMembers().contains(notpinged)) {
                ping.add(notpinged);
            }
        }

        for (Member said : event.getGuild().getMembers()) {
            if (MESSAGE.getContentRaw().contains(said.getUser().getName())) ping.remove(said);
        }

        // Removes the message sender from the ArrayList
        ping.remove(event.getGuild().getMemberById(AUTHOR.getId()));

        // Removes dmPing
        ping.remove(MESSAGE.getGuild().getMemberById("1031753071589601332"));

        // If there's no one to ping, it does nothing
        if (ping.isEmpty()) return new MentionUsers("", "");

        String pingst = "";
        String pingtags = "";

        // Gets all the members that it should ping as a mention (ping) and adds them to a string
        for (Member member : ping) {
            pingst = pingst.concat(member.getAsMention());
            pingst = pingst.concat("");
        }

        // Gets all the members that it should ping as a tag (Wumpus#0000) for logging purposes
        for (Member member : ping) {
            pingtags = pingtags.concat(member.getUser().getAsTag());
            pingtags = pingtags.concat(" | ");
        }

        if (MESSAGE.getContentRaw().contains("@everyone")) {
            return new MentionUsers("", "");
        }

        // Pings the members that it should
        MessageCreateAction action = event.getChannel().asTextChannel().sendMessage(AUTHOR.getAsTag() + ": " + MESSAGE.getContentDisplay() + pingst);
        Message msg = action.complete();
        msg.delete().queue();

        return new MentionUsers(pingtags, pingst);
    }

    public static class MentionUsers{
        String pingtags;
        String pingst;

        public String getPingtags() {
            return pingtags;
        }
        public String getPingst() {
            return pingst;
        }
        public MentionUsers(String pingtags, String pingst) {
            this.pingtags = pingtags;
            this.pingst = pingst;
        }
    }

    public static class Scyye {
        public static Member s5aMember = Sub5Allts.guild.getMembersByName("scyye", true).get(0);
        public static User user = s5aMember.getUser();
        public static String id = user.getId();
        public static String tag = user.getAsTag();
        public static String name = user.getName();
        public static String strippedTag = user.getDiscriminator();
    }

    public static class Sub5Allts {
        public static Guild guild = instance.jda.getGuildById(990029740016541768L);
        static {
            while (guild==null) {
                guild=instance.jda.getGuildById(990029740016541768L);
            }
        }
        public static Role guestTrue = ScyyeThings.ensureGetRole(guild, "user.isGuest(true)", true);
        public static Role altTrue = ScyyeThings.ensureGetRole(guild, "user.isAlt(true)", true);
        public static Role altFalse = ScyyeThings.ensureGetRole(guild, "user.isAlt(false)", true);
        public static TextChannel cachingChannel = ScyyeThings.ensureGetChannel(guild, "caching", true);
        public static TextChannel controlPanel = ScyyeThings.ensureGetChannel(guild, "cp1", "Control Panel", true);
    }
}
