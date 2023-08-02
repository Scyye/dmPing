package ml.scyye.uniping.listeners;

import ml.scyye.dmping.utils.Constants;
import ml.scyye.uniping.UniThings;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static ml.scyye.dmping.utils.DMPingUtils.mentionUsers;

public class UniListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getChannelType()!= ChannelType.TEXT || event.getGuild().getName().equalsIgnoreCase("Sub5Allts")) {
            return;
        }

        // Gets all users it should mention
        mentionUsers(event);

        // Logs everything
        UniThings.printMessage(event);
    }
}
