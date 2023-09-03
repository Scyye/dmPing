package ml.scyye.dmping.listeners;

import ml.scyye.database.SQLiteUtils;
import ml.scyye.dmping.Main;
import ml.scyye.dmping.utils.MessageUtils;
import ml.scyye.dmping.utils.S2AListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import static ml.scyye.dmping.Main.CACHE;
import static ml.scyye.dmping.utils.Constants.Scyye;

public class Antidelete extends S2AListener {

    public static class CachedMessage {
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
        CacheManager manager = Main.instance.cacheManager;

        CachedMessage cachedMessage = manager.findMessageById(event.getMessageId());
        if (cachedMessage.authorId==null)
            return;
        if (cachedMessage.authorId.isEmpty())
            return;

        Member member = event.getGuild().retrieveMemberById(cachedMessage.authorId).complete();

        MessageUtils.sendWebhookMessage(event.getChannel().asTextChannel(), cachedMessage.content,
                new MessageUtils.MessageAuthor(member.getEffectiveName()+" (Deleted Message)", member.getEffectiveAvatarUrl()));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!CACHE || Main.config.getBlacklist().contains(event.getAuthor().getId())) return;

        if (event.getAuthor().isBot()) {
            return;
        }


        if (event.getMessage().getContentRaw().equals("")) return;
        SQLiteUtils.insertEntry(event.getMessageId(), event.getAuthor().getId(), event.getMessage().getContentRaw());
        // cacheChannel.sendMessage(event.getMessageId() + "," + event.getAuthor().getId() + "," + content).queue();

        Main.instance.cacheManager.cachedMessages.add(new CachedMessage(event.getMessageId(), event.getAuthor().getId(), event.getMessage().getContentRaw()));
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
        public List<CachedMessage> cachedMessages = new LinkedList<>();

        public CacheManager() {
            cachedMessages.addAll(SQLiteUtils.findAllCachedMessages());
        }

        private CachedMessage findMessageById(String id) {
            for (CachedMessage cachedMessage : cachedMessages) {
                if (cachedMessage.messageId.equalsIgnoreCase(id)) return cachedMessage;
            }
            return new CachedMessage();
        }

    }


}
