package ml.scyye.dmping.listeners;

import ml.scyye.dmping.utils.S5AListener;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static ml.scyye.dmping.utils.APIGetNotNullUtils.*;
import static ml.scyye.dmping.utils.Constants.*;
import static ml.scyye.dmping.utils.DMPingUtils.*;
import static ml.scyye.dmping.utils.LoggingUtils.*;

public class DMPing extends S5AListener {

    /*
    TODO: VC Text chat that dmPing only pings people in the vc
     */


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (blacklist.contains(event.getAuthor().getId())) {
            event.getMessage().delete().queue();
            logMessage(event, "**USER IS BLACKLISTED**", "**USER IS BLACKLISTED**");
        }
        if (event.getAuthor().isBot() || event.getChannelType()!=ChannelType.TEXT || blacklist.contains(event.getAuthor().getId())) {
            return;
        }

        if (event.getChannel().getName().equalsIgnoreCase("media")) {
            TextChannel textChannel = ensureGetChannel(event.getGuild(), "general", "General Channels", true);
            if (event.getMessage().getAttachments().isEmpty()) {event.getMessage().delete().queue(); return;}

            forwardAttachments(event, textChannel);
            return;
        }

        // Gets all users it should mention
        MentionUsers mentionUsers = mentionUsers(event);

        // Logs everything
        logMessage(event, mentionUsers.getPingString(), mentionUsers.getPingNames());
    }
}


