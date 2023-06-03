package ml.Scyye.dmPing;

import ml.Scyye.dmPing.listeners.*;
import ml.Scyye.dmPing.commands.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.*;

import java.util.*;

import static ml.Scyye.dmPing.ScyyeThings.*;

public class Main extends ListenerAdapter {

    public static Main instance;

    public JDA jda;

    public UUID cacheClearCode;

    public UUID randomizeCacheCode() {
        cacheClearCode=UUID.randomUUID();
        return cacheClearCode;
    }

    public AudioManager audioManager;

    public Main() {
        jda = JDABuilder.createLight(TOKEN)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.listening("your DMs"))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .addEventListeners(
                        new dmPing(),
                        new CommandManager(),
                        new MemberHandler(),
                        new Antidelete(),
                        new PrivateMessageHandler(),
                        this
                )
                .build();


        cacheClearCode = UUID.randomUUID();
    }

    public static void main(String[] args) {
        instance = new Main();
        Timer timer = new Timer();
        TimerTask task = new Deleter();

        timer.schedule(task, 5000, 100);  // 2000 - delay (can set to 0 for immediate execution), 5000 is a frequency.
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        if (!event.getGuild().equals(Sub5Allts.guild)) return;
        audioManager = ScyyeThings.Sub5Allts.guild.getAudioManager();
        audioManager.openAudioConnection(ScyyeThings.Sub5Allts.guild.getVoiceChannelById(1057178334766841856L));
    }
}
