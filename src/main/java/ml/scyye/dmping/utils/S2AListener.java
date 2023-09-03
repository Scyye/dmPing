package ml.scyye.dmping.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static ml.scyye.dmping.Main.print;

public class S2AListener extends ListenerAdapter {
    public S2AListener() {
        print(this.getClass().getName()+" enabled and listening.");
    }
}
