package ml.Scyye.dmPing.listeners;

import ml.Scyye.dmPing.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.TimerTask;

public class Deleter extends TimerTask {
    public void run()
    {
        // For all the guilds dmPing is in
        for (Guild guild : Main.instance.jda.getGuilds()) {

            // For all the text channels in said guild
            for (TextChannel channel : guild.getTextChannels()) {
                // If the channel is a text channel
                if (channel.getType()==ChannelType.TEXT) {

                    for (Message message : channel.getHistory().getRetrievedHistory()) {
                        if (Main.instance.jda.getSelfUser().getIdLong()==message.getAuthor().getIdLong()) {
                            // Deletes every message, sent by dmPing in every server its in.
                            message.delete().complete();
                        }
                    }
                }
            }
        }
    }

}
