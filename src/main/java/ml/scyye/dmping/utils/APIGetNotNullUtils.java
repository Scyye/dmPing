package ml.scyye.dmping.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class APIGetNotNullUtils {
    public static Role ensureGetRoleByName(@NotNull Guild guild, String roleName, boolean ignoreCase) {
        var roles = guild.getRolesByName(roleName, ignoreCase);
        if (!roles.isEmpty()) return roles.get(0);
        return guild.createRole().setName(roleName).complete();
    }

    public static TextChannel ensureGetChannelByName(@NotNull Guild guild, String channelName) {
        return ensureGetChannelByName(guild, channelName, true);
    }

    public static TextChannel ensureGetChannelByName(@NotNull Guild guild, String channelName, boolean ignoreCase) {
        return guild.getTextChannelsByName(channelName, ignoreCase).isEmpty()
                ? guild.createTextChannel(channelName).complete() :
                guild.getTextChannelsByName(channelName, ignoreCase).get(0);
    }

    public static TextChannel ensureGetChannelByName(@NotNull Guild guild, String channelName, String category, boolean ignoreCase) {
        List<TextChannel> channels = guild.getTextChannelsByName(channelName, ignoreCase);
        List<Category> categories = guild.getCategoriesByName(category, ignoreCase);
        if (!channels.isEmpty() && !categories.isEmpty()) return channels.get(0);
        if (channels.isEmpty()) return categories.get(0).createTextChannel(channelName).complete();
        return guild.createCategory(category).complete().createTextChannel(channelName).complete();
    }

    public static Webhook ensureGetWebhookByName(TextChannel channel, String name) {
        Webhook webhook = null;
        for (var w : channel.retrieveWebhooks().complete()) {
            if (w.getName().equals(name)) { webhook = w; break; }
        }
        return webhook!=null?webhook:channel.createWebhook(name).complete();
    }
}
