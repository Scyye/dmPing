package ml.scyye.uniping;

import com.github.kaktushose.jda.commands.JDACommands;
import ml.scyye.dmping.Main;
import ml.scyye.dmping.utils.Constants;
import ml.scyye.uniping.listeners.UniListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;

public class Uni {

    public static Uni instance = new Uni();
    public JDA uniJDA;

    public static final String VERSION = "1.0.0";

    public Uni() {
        uniJDA = JDABuilder.createLight(Main.config.getUniToken())
                .setActivity(Activity.listening("uniPing V"+VERSION))
                .setStatus(OnlineStatus.ONLINE)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .addEventListeners(
                    new UniListener()
                )


                .build();
    }

    public static void main(String[] args) {
        instance.uniJDA.getSelfUser();
        JDACommands.start(instance.uniJDA, instance.getClass(), "ml.scyye.uniping.listeners");
    }

}
