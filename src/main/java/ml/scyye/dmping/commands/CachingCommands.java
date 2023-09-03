package ml.scyye.dmping.commands;

import com.github.kaktushose.jda.commands.annotations.interactions.Interaction;
import com.github.kaktushose.jda.commands.annotations.interactions.SlashCommand;
import com.github.kaktushose.jda.commands.dispatching.commands.CommandEvent;

import static ml.scyye.dmping.Main.CACHE;

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
}
