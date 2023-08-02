package ml.scyye.dmping.listeners;

import ml.scyye.dmping.Main;
import ml.scyye.dmping.utils.Constants;
import ml.scyye.dmping.utils.MessageUtils;
import ml.scyye.dmping.utils.S5AListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;

import static ml.scyye.dmping.Main.CACHE;
import static ml.scyye.dmping.utils.APIGetNotNullUtils.ensureGetChannel;
import static ml.scyye.dmping.utils.Constants.*;
import static ml.scyye.dmping.utils.MessageUtils.sendPrivateMessage;

public class Antidelete extends S5AListener {


    static class CachedMessage {
        public String  messageId;
        public String  authorId;
        public String  content;

        public CachedMessage(String messageId, String authorId, String content) {
            this.messageId=messageId;
            this.authorId = authorId;
            this.content = content;
        }

        public CachedMessage() {}
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (!CACHE || event.getChannelType() != ChannelType.TEXT || event.getChannelType() == ChannelType.PRIVATE) return;
        String messageId = event.getMessageId();
        CacheManager manager = Main.instance.cacheManager;

        if (manager.sentByBot.contains(messageId)) return;

        CachedMessage cachedMessage = manager.findMessageById(event.getMessageId());
        if (cachedMessage.authorId.isEmpty())
            return;

        @NotNull
        var member = event.getGuild().retrieveMemberById(cachedMessage.authorId).complete();

        MessageUtils.sendWebhookMessage(event.getChannel().asTextChannel(), cachedMessage.content.replace('␂', ','),
                new MessageUtils.MessageAuthor(member.getEffectiveName()+" (Deleted Message)", member.getEffectiveAvatarUrl()));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!CACHE || blacklist.contains(event.getAuthor().getId())||!event.getChannel().getName().equalsIgnoreCase("general")) return;

        CacheManager manager = Main.instance.cacheManager;
        if (event.getAuthor().isBot()) {
            manager.sentByBot.add(event.getMessageId());
            return;
        }

        TextChannel cacheChannel = ensureGetChannel(event.getGuild(), "caching", true);


        String content = event.getMessage().getContentRaw();
        if (content.equals("")) return;
        content= content.replace(",", "␂");
        cacheChannel.sendMessage(event.getMessageId() + "," + event.getAuthor().getId() + "," + content).queue();


        manager.cachedMessages.add(new CachedMessage(event.getMessageId(), event.getAuthor().getId(), event.getMessage().getContentRaw()));
    }

    public static void clearOldCache() {
        for (var message : Sub5Allts.cachingChannel.getHistory().getRetrievedHistory()) {
            if (message.getTimeCreated().isBefore(OffsetDateTime.of(LocalDateTime.now().minusDays(7), ZoneOffset.of("EST")))) {
                message.delete().queue();
            }
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getChannelType()==ChannelType.PRIVATE) return;
        if (!event.getMessage().isEdited()) return;
        CacheManager manager = Main.instance.cacheManager;
        EmbedBuilder builder= new EmbedBuilder();
        String originalContent = manager.findMessageById(event.getMessageId()).content;
        String newContent = event.getMessage().getContentRaw();
        User author = Main.instance.jda.getUserById(event.getAuthor().getId());

        assert author != null;
        builder.setAuthor(author.getEffectiveName())
                .setThumbnail(author.getAvatarUrl())
                .setColor(1)
                .addField("Original Content:", "```txt\n"+originalContent+"\n```", false)
                .addField("New Content:", "```\n"+newContent+"\n```", false)
                .appendDescription(event.getJumpUrl())
                ;

        MessageUtils.sendPrivateMessage(Scyye.user, builder.build());
    }

    public static class CacheManager {
        public List<String> sentByBot = new LinkedList<>();
        public List<CachedMessage> cachedMessages = new LinkedList<>();

        public CacheManager() {
            this.initCacheList();
        }

        public void initCacheList() {
            for (Message message : Sub5Allts.cachingChannel.getIterableHistory()) {
                cachedMessages.add(new CachedMessage(message.getId(), message.getAuthor().getId(), message.getContentRaw()));
            }
        }

        private CachedMessage findMessageById(String id) {
            for (CachedMessage cachedMessage : cachedMessages) {
                if (cachedMessage.messageId.equalsIgnoreCase(id)) return cachedMessage;
            }
            return new CachedMessage();
        }

    }


}
