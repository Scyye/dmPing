package ml.scyye.dmping.listeners;

import ml.scyye.dmping.Main;
import ml.scyye.dmping.utils.S2AListener;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import static ml.scyye.dmping.Main.config;
import static ml.scyye.dmping.utils.APIGetNotNullUtils.*;
import static ml.scyye.dmping.utils.Constants.*;
import static ml.scyye.dmping.utils.DMPingUtils.*;

public class DMPing extends S2AListener {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (config.getBlacklist().contains(event.getAuthor().getId())) {
            event.getMessage().delete().queue();
            logMessage(event, "**USER IS BLACKLISTED**", "**USER IS BLACKLISTED**");
        }
        if (event.getChannelType()==ChannelType.VOICE) {
            VoicePingManager.instance.handle(event);
            return;
        }
        if (event.getAuthor().isBot()  || Main.config.getBlacklist().contains(event.getAuthor().getId())) {
            return;
        }

        if (!event.getMessage().getAttachments().isEmpty()) {
            forwardAttachments(event, ensureGetChannelByName(event.getGuild(), "media-stuff"));
            return;
        }

        // Gets all users it should mention
        MentionUsers mentionUsers = mentionUsers(event, null);

        // Logs everything
        logMessage(event, mentionUsers.getPingString(), mentionUsers.getPingNames());
    }
}


