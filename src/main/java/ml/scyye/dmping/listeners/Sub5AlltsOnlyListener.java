package ml.scyye.dmping.listeners;

import ml.scyye.dmping.Main;
import ml.scyye.dmping.commands.music.PlayerManager;
import ml.scyye.dmping.utils.Config;
import ml.scyye.dmping.utils.Constants;
import ml.scyye.dmping.utils.S2AListener;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

import static ml.scyye.dmping.utils.APIGetNotNullUtils.ensureGetChannelByName;

public class Sub5AlltsOnlyListener extends S2AListener {
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        if (Main.config.isDevMode()) {
            Constants.DmPingGuild.guild.getAudioManager().openAudioConnection(Constants.DmPingGuild.guild.getVoiceChannels().get(0));
            PlayerManager.instance.loadAndPlay(event.getGuild(), "https://cdn.discordapp.com/attachments/1034268358507515914/1137165444281815090/dmping_join_sound.mp3");
        }
        Main.postInit();
    }
}
