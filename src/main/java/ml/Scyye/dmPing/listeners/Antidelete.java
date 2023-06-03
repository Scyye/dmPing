package ml.Scyye.dmPing.listeners;

import ml.Scyye.dmPing.Main;
import ml.Scyye.dmPing.ScyyeThings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static ml.Scyye.dmPing.ScyyeThings.*;

public class Antidelete extends ListenerAdapter {

    HashMap<String, CacheManager> cacheMap = new HashMap<>();



    static class CachedMessage {
        public String  content;
        public String  id;
        public String  authorId;

        public CachedMessage(String id, String authorId, String content) {
            this.content = content;
            this.id = id;
            this.authorId = authorId;
        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (!cache) return;
        String messageId = event.getMessageId();
        if (event.getChannelType() == ChannelType.PRIVATE) return;
        CacheManager manager = cacheMap.get(event.getGuild().getId());

        TextChannel cacheChannel = ScyyeThings.ensureGetChannel(event.getGuild(), "caching", true);
        if (event.getChannelType() == ChannelType.GUILD_PUBLIC_THREAD) return;
        if (manager.sentByBot.contains(messageId)) return;

        for (Message message : cacheChannel.getIterableHistory().complete()) {
            String[] data = message.getContentRaw().split(",");
            if (data.length < 2) return;
            String iterableId = data[0];
            String authorId = data[1];
            String content = data[2].replace("␂", ",");


            if (!messageId.equalsIgnoreCase(iterableId)) continue;

            event.getChannel().sendMessage("**Deleted Message!**\n" + Objects.requireNonNull(event.getGuild().getMemberById(authorId)).getUser().getAsTag()+": " + content).queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!cache) return;
        if (blacklist.contains(event.getAuthor().getId())) return;
        if (event.getChannelType()==ChannelType.PRIVATE) return;
        CacheManager manager = cacheMap.get(event.getGuild().getId());

        TextChannel cacheChannel = ensureGetChannel(event.getGuild(), "caching", true);

        if (event.getAuthor().isBot()) {
            manager.sentByBot.add(event.getMessageId());
            return;
        }
        String content = event.getMessage().getContentRaw();
        if (content.equals("")) return;
        content= content.replace(",", "␂");
        cacheChannel.sendMessage(event.getMessageId() + "," + event.getAuthor().getId() + "," + content).queue();


        manager.cachedMessages.add(new CachedMessage(event.getMessageId(), event.getAuthor().getId(), content));
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getChannelType()==ChannelType.PRIVATE) return;
        CacheManager manager = new CacheManager(event.getGuild());
        EmbedBuilder builder= new EmbedBuilder();
        String originalContent = manager.findMessageById(event.getMessageId()).content;
        String newContent = event.getMessage().getContentRaw();
        User author = Main.instance.jda.getUserById(event.getAuthor().getId());

        assert author != null;
        builder.setAuthor(author.getAsTag())
                .setThumbnail(author.getAvatarUrl())
                .setColor(1)
                .addField("Original Content:", "```txt\n"+originalContent+"\n```", false)
                .addField("New Content:", "```\n"+newContent+"\n```", false)
                .appendDescription(event.getJumpUrl())
                ;

        ScyyeThings.sendMessage(Scyye.user, builder.build());
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        cacheMap.put(event.getGuild().getId(), new CacheManager(event.getGuild()));
    }

    private static class CacheManager {
        Guild guild;

        public List<String> sentByBot = new LinkedList<>();
        public List<CachedMessage> cachedMessages = new LinkedList<>();

        public CacheManager(Guild guild) {
            this.guild=guild;
            this.initCacheList();
        }

        public void initCacheList() {
            for (Message message : ensureGetChannel(this.guild, "caching", true).getIterableHistory()) {
                if (message.getContentRaw().split(",").length < 3) continue;
                cachedMessages.add(new CachedMessage(
                        message.getContentRaw().split(",")[0],
                        message.getContentRaw().split(",")[1],
                        message.getContentRaw().split(",")[2]
                ));
            }
        }

        private CachedMessage findMessageById(String id) {
            for (CachedMessage message : cachedMessages) {
                if (message.id.equalsIgnoreCase(id)) return message;
            }
            return new CachedMessage("","", "");
        }

    }


}
