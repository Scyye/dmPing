package ml.scyye.dmping.listeners;

import ml.scyye.dmping.utils.S2AListener;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static ml.scyye.dmping.utils.DMPingUtils.logMessage;
import static ml.scyye.dmping.utils.DMPingUtils.mentionUsers;

public class VoicePingManager extends S2AListener {

    List<Member> inVoice = new ArrayList<>();

    public static VoicePingManager instance = new VoicePingManager();

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        if (event.getChannelJoined() != null) {
            inVoice.add(event.getMember());
        }
        if (event.getChannelLeft() != null) {
            inVoice.remove(event.getMember());
        }
    }

    public void handle(@NotNull MessageReceivedEvent event) {
        Constants.MentionUsers users = mentionUsers(event, member -> inVoice.contains(member));

        logMessage(event, users.getPingString(), users.getPingNames());
    }
}
