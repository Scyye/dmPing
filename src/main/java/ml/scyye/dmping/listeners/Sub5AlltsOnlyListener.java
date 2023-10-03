package ml.scyye.dmping.listeners;

import ml.scyye.dmping.Main;
import ml.scyye.dmping.commands.music.PlayerManager;
import ml.scyye.dmping.utils.S2AListener;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

public class Sub5AlltsOnlyListener extends S2AListener {
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        if (Main.config.isDevMode()) {
            Main.instance.jda.getGuildById(Main.config.getGuildId()).getAudioManager()
                    .openAudioConnection(Main.instance.jda.getGuildById(Main.config.getGuildId()).getVoiceChannels().get(0));
            PlayerManager.instance.getMusicManager(event.getGuild()).player.setVolume(50);
            PlayerManager.instance.loadAndPlay(event.getGuild(), "https://cdn.discordapp.com/attachments/1141458821248716970/1149862431179419688/dmping_join_sound.mp3");
        }
    }
}
