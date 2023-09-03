package ml.scyye.dmping.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    public static PlayerManager instance = new PlayerManager();

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager manager;

    private PlayerManager() {
        this.musicManagers=new HashMap<>();
        this.manager=new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.manager);
        AudioSourceManagers.registerLocalSource(this.manager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId)->{
            final GuildMusicManager musicManager = new GuildMusicManager(this.manager);
            guild.getAudioManager().setSendingHandler(musicManager.sendHandler);
            return musicManager;
        });
    }

    public void loadAndPlay(Guild guild, String trackUrl) {
        GuildMusicManager musicManager = this.getMusicManager(guild);

        this.manager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.player.setVolume(200);
                musicManager.player.startTrack(audioTrack, false);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

}
