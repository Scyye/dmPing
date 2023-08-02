package ml.scyye.dmping;

import com.github.kaktushose.jda.commands.JDACommands;
import ml.scyye.dmping.commands.CommandManager;
import ml.scyye.dmping.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Arrays;
import java.util.UUID;

import static ml.scyye.dmping.listeners.Antidelete.clearOldCache;
import static ml.scyye.dmping.utils.Constants.TOKEN;
import static ml.scyye.dmping.utils.LoggingUtils.printStartup;

public class Main {

    public static Main instance;

    public JDA jda;

    public UUID cacheClearCode;
    public boolean dmLink;

    public UUID randomizeCacheCode() {
        cacheClearCode=UUID.randomUUID();
        return cacheClearCode;
    }

    public Antidelete.CacheManager cacheManager;

    public static final String VERSION = "5.3.0";
    public static final boolean BETA = false;
    public static boolean CACHE = true;

    public static final boolean DEV_MODE = false;

    private Main() {
        try {
            jda = JDABuilder.createDefault(TOKEN)
                    .setStatus(OnlineStatus.ONLINE)
                    .setActivity(Activity.watching("dmPing V"+VERSION+(BETA?"-b":"")))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableCache(Arrays.stream(CacheFlag.values()).toList())
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                    .addEventListeners(
                            new DMPing(),
                            new MemberHandler(),
                            new Antidelete(),
                            new AntiJustin(),
                            new Sub5AlltsOnlyListener()
                    )
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        instance=new Main();
        preInit();
    }

    static void preInit() {
        Main.instance.randomizeCacheCode();
        Main.instance.dmLink=false;
    }

    public static void postInit() {
        String commandsPackage = CommandManager.class.getPackageName();;
        try {
            printStartup("Enabling commands in " + commandsPackage);
            JDACommands.start(instance.jda, instance.getClass(), commandsPackage);
            Main.instance.cacheManager=new Antidelete.CacheManager();

            Main.instance.jda.getGuilds().get(0).getSelfMember().modifyNickname("dmPing"+(BETA?" V"+VERSION+"-beta":"")).queue();
            clearOldCache();
        } catch (IllegalArgumentException e) {
            try {
                Thread.sleep(   2000);
                System.out.println("Waiting 2 seconds");
            } catch (Exception er) {
                er.printStackTrace();
            }
            postInit();
        }
    }


}
