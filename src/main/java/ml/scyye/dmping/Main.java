package ml.scyye.dmping;

import com.github.kaktushose.jda.commands.JDACommands;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ml.scyye.dmping.commands.CommandManager;
import ml.scyye.dmping.listeners.*;
import ml.scyye.dmping.utils.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

public class Main {

    public static Main instance;

    public JDA jda;

    public UUID cacheClearCode;
    public boolean dmLink;

    public void randomizeCacheCode() {
        cacheClearCode=UUID.randomUUID();
    }

    public Antidelete.CacheManager cacheManager;


    public static boolean CACHE = true;

    public static Config config;

    private Main()  {
        jda = JDABuilder.createDefault(config.getToken())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.of(Activity.ActivityType.PLAYING, "dmPing V"+config.getVersion()+(config.isBeta()?"-b":"")))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(Arrays.stream(CacheFlag.values()).toList())
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .addEventListeners(
                        new DMPing(),
                        new Antidelete(),
                        // TODO: Remove
                        new AntiJustin(),
                        VoicePingManager.instance,
                        new Sub5AlltsOnlyListener()
                )
                .build();
    }

    public static void main(String[] args) {
        loadConfig();
        instance=new Main();
    }

    static void loadConfig() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final Path configFilePath = Path.of("src","main", "resources", "config.json");

        final File configFile = configFilePath.toFile();
        try {
            if (!configFile.exists()) {
                if (!configFile.createNewFile())
                    loadConfig();
                Files.writeString(configFilePath, gson.toJson(Config.defaultConfig(), Config.class));
            }
            config = gson.fromJson(Files.readString(configFilePath), Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void postInit() {
        String commandsPackage = CommandManager.class.getPackageName();;
        try {
            Main.instance.randomizeCacheCode();
            Main.instance.dmLink=false;
            if (!JDACommands.isActive()) {
                print("Enabling commands in " + commandsPackage);
                JDACommands.start(instance.jda, instance.getClass(), commandsPackage, commandsPackage+".music");
                print("Enabled commands in " + commandsPackage);
            }

            Main.instance.cacheManager=new Antidelete.CacheManager();

            Main.instance.jda.getGuilds().get(0).getSelfMember().modifyNickname("dmPing"+(config.isBeta()?" V"+config.getVersion()+"-beta":"")).queue();
        } catch (IllegalArgumentException e) {
            try {
                Thread.sleep(2000);
                System.out.println("Waiting 2 seconds");
            } catch (Exception er) {
                er.printStackTrace();
            }
            postInit();
        }
    }

    public static void print(String... strings) {
        for (String st : strings) {
            System.out.println(st);
        }
    }

}
