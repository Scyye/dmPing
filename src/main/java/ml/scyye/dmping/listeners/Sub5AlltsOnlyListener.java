package ml.scyye.dmping.listeners;

import ml.scyye.dmping.Main;
import ml.scyye.dmping.utils.Constants;
import ml.scyye.dmping.utils.S5AListener;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

import static ml.scyye.dmping.utils.APIGetNotNullUtils.ensureGetChannel;

public class Sub5AlltsOnlyListener extends S5AListener {
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        if (!event.getGuild().getId().equals("990029740016541768")) {
            ensureGetChannel(event.getGuild(), "general", true)
                    .sendMessage("dmPing only works with Sub5Allts! For more info, see: " + "TO BE ADDED SOON, WEBSITE EXPLAINING THIS.").queue();
            event.getGuild().leave().queue();
            return;
        }
        if (Main.DEV_MODE)
            Constants.Sub5Allts.guild.getAudioManager().openAudioConnection(Constants.Sub5Allts.guild.getVoiceChannels().get(0));
        Main.postInit();
    }
}
