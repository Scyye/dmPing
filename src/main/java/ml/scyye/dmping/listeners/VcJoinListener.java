package ml.scyye.dmping.listeners;

import ml.scyye.dmping.utils.TextToSpeechUtil;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class VcJoinListener extends ListenerAdapter {
	@Override
	public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
		if (event.getChannelLeft()!=null) {
			if (event.getChannelLeft().getMembers().size()==1) {
				event.getGuild().getAudioManager().closeAudioConnection();
			}
		}
		if (event.getChannelJoined()==null || event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) return;
		event.getGuild().getAudioManager().openAudioConnection(event.getChannelJoined());
		TextToSpeechUtil.play("DM ping at your service");
	}
}
