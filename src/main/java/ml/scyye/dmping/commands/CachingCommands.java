package ml.scyye.dmping.commands;

import com.github.kaktushose.jda.commands.annotations.interactions.Interaction;
import com.github.kaktushose.jda.commands.annotations.interactions.Param;
import com.github.kaktushose.jda.commands.annotations.interactions.SlashCommand;
import com.github.kaktushose.jda.commands.dispatching.commands.CommandEvent;
import ml.scyye.dmping.*;
import ml.scyye.dmping.utils.Constants;
import net.dv8tion.jda.api.Permission;

import java.util.UUID;

import static ml.scyye.dmping.Main.CACHE;
import static ml.scyye.dmping.utils.Constants.*;
import static ml.scyye.dmping.utils.LoggingUtils.printStartup;

@Interaction
public class CachingCommands {
    @SlashCommand(value = "cache toggle", desc = "Toggles caching.", ephemeral = true)
    public void onCacheToggle(CommandEvent event) {
        CACHE = !CACHE;
        event.reply("Set status of caching to `" + CACHE+"`");
    }

    @SlashCommand(value = "cache status", desc = "Tells you weather caching is on or off.", ephemeral = true)
    public void onCache(CommandEvent event) {
        event.reply("Caching is " + (CACHE?"`Enabled`":"`Disabled`"));
    }

    @SlashCommand(value = "cache id", desc = "Tells you the current UUID for cache clearing", ephemeral = true, enabledFor = Permission.BAN_MEMBERS)
    public void onCacheId(CommandEvent event) {
        event.reply(Main.instance.cacheClearCode.toString());
    }

    @SlashCommand(value = "cache clear", desc = "Clears the cache if you provide the ID", ephemeral = true)
    public void onCacheClear(CommandEvent event, @Param("clear id") String id) {
        try {
            if (UUID.fromString(id).equals(Main.instance.cacheClearCode)) {
                for (var m : Constants.Sub5Allts.cachingChannel.getIterableHistory()) {
                    m.delete().queue();
                }
                event.reply("Successfully cleared cache.");
                printStartup(Main.instance.randomizeCacheCode().toString());
            } else {
                event.reply("Invalid UUID");
            }
        } catch (IllegalArgumentException e) {
            event.reply("Invalid UUID");
        }
    }
}
