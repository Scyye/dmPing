package ml.Scyye.dmPing.listeners;

import ml.Scyye.dmPing.Main;
import ml.Scyye.dmPing.ScyyeThings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class PrivateMessageHandler extends ListenerAdapter {

    HashMap<String, LinkChannel> linkedChannels = new HashMap<>();

    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild: event.getJDA().getGuilds()) {
            this.guildReady(new GuildReadyEvent(event.getJDA(), 0, guild));
        }
    }

    public void guildReady(GuildReadyEvent event) {
        ScyyeThings.sendMessage(ScyyeThings.Scyye.user, event.getGuild().getName()).addReaction(Emoji.fromUnicode("U+2714")).complete();
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        if (event.getChannelType() != ChannelType.PRIVATE) return;
        if (!event.getUser().getId().equalsIgnoreCase(ScyyeThings.Scyye.id)) return;
        if (event.getEmoji().getType() != Emoji.Type.UNICODE) return;
        if (!event.getEmoji().asUnicode().getAsCodepoints().equalsIgnoreCase("U+2714")) return;

        MessageChannelUnion channelUnion = event.getChannel();
        PrivateChannel channel = channelUnion.asPrivateChannel();
        var guildChannel = ScyyeThings.ensureGetChannel(Main.instance.jda.getGuildsByName(message.getContentRaw(), true).get(0), "general", true);

        channel.sendMessage("Linked to: " + guildChannel.getGuild().getName()).complete();
        linkedChannels.put(guildChannel.getGuild().getId(), new LinkChannel(event.getChannel().asPrivateChannel(), guildChannel));
        Main.instance.jda.addEventListener(linkedChannels.get(guildChannel.getGuild().getId()));
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        if (event.getChannelType() != ChannelType.PRIVATE) return;
        if (!event.getUser().getId().equalsIgnoreCase(ScyyeThings.Scyye.id)) return;
        if (event.getEmoji().getType() != Emoji.Type.UNICODE) return;
        if (!event.getEmoji().asUnicode().getAsCodepoints().equalsIgnoreCase("U+2714")) return;

        MessageChannelUnion channelUnion = event.getChannel();
        PrivateChannel channel = channelUnion.asPrivateChannel();
        var guildChannel = ScyyeThings.ensureGetChannel(Main.instance.jda.getGuildsByName(message.getContentRaw(), true).get(0), "general", true);

        channel.sendMessage("Unlinked from: " + guildChannel.getGuild().getName()).complete();
        Main.instance.jda.removeEventListener(linkedChannels.get(guildChannel.getGuild().getId()));
    }




    private static class LinkChannel extends ListenerAdapter{
        private final PrivateChannel privateChannel;
        private final TextChannel textChannel;

        public LinkChannel(PrivateChannel privateChannel, TextChannel textChannel) {
            this.privateChannel = privateChannel;
            this.textChannel = textChannel;
        }

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            if (event.getAuthor().isBot()) return;
            if (event.getChannelType()==ChannelType.PRIVATE) {
                if (event.getMessage().getContentRaw().isEmpty() && event.getMessage().getAttachments().isEmpty()) return;
                if (!event.getMessage().getContentRaw().isEmpty()) textChannel.sendMessage(event.getMessage().getContentRaw()).queue();

                if (!event.getMessage().getAttachments().isEmpty()) {
                    ScyyeThings.forwardAttachments(event, textChannel);
                }
            } else {
                privateChannel.sendMessage(event.getMessage().getContentRaw()).queue();
                if (!event.getMessage().getAttachments().isEmpty()) {
                    ScyyeThings.forwardAttachments(event, privateChannel);
                }
            }
        }
    }
}
