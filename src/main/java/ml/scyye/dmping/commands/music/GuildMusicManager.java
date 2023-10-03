package ml.scyye.dmping.commands.music;

import com.sedmelluq.discord.lavaplayer.player.*;

public class GuildMusicManager {
    public final AudioPlayer player;;
    public final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.player = manager.createPlayer();
        this.sendHandler=new AudioPlayerSendHandler(this.player);
    }
}

