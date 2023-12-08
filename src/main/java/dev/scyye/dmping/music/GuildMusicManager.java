package dev.scyye.dmping.music;

import com.sedmelluq.discord.lavaplayer.player.*;

public class GuildMusicManager {
    public final AudioPlayer player;;
    public final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        sendHandler=new AudioPlayerSendHandler(player);
    }
}

