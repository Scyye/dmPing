package ml.Scyye.dmPing.listeners;

import ml.Scyye.dmPing.ScyyeThings;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static ml.Scyye.dmPing.ScyyeThings.*;

public class dmPing extends ListenerAdapter {

    /*
    THINGS I WANT TO ADD:
    1: VC Text chat that dmPing only pings people in the vc
     */


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannelType() == ChannelType.PRIVATE) return;
        if (ScyyeThings.blacklist.contains(event.getMember().getId())) {
            logMessage(event, "", "");
            event.getMessage().delete().complete();
            return;
        }
        if (event.getChannelType().equals(ChannelType.VOICE)) return;
        if (event.getChannelType() == ChannelType.GUILD_PUBLIC_THREAD) return;
        if (event.getChannel().asTextChannel().getName().equalsIgnoreCase("media")) {
            TextChannel textChannel = ScyyeThings.ensureGetChannel(event.getGuild(), "general", "General Channels", true);
            if (event.getMessage().getAttachments().isEmpty()) event.getMessage().delete().complete();

            ScyyeThings.mentionProperUsers(new MessageReceivedEvent(event.getJDA(), 0L, ScyyeThings.forwardAttachments(event, textChannel)));
        }


        // Variables
        User AUTHOR = event.getAuthor();

        // If dmPing sent the message, it does nothing.
        if (AUTHOR.isBot()) return;

        // Adds all the members that werent already pinged in the message to an ArrayList
        MentionUsers mentionUsers = ScyyeThings.mentionProperUsers(event);

        // Logs everything
        ScyyeThings.logMessage(event, mentionUsers.getPingst(), mentionUsers.getPingtags());
    }
}


