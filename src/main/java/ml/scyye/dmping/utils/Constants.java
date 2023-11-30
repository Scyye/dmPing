package ml.scyye.dmping.utils;

import ml.scyye.dmping.Main;
import net.dv8tion.jda.api.entities.*;

import static ml.scyye.dmping.Main.instance;

@Deprecated(since = "5.4.5", forRemoval = true)
public class Constants {
    @Deprecated(since = "5.4.3", forRemoval = true)
    public static class Scyye {
        public static User user = Main.instance.jda.retrieveUserById(Main.config.get("ownerId", String.class)).complete();
        public static String id = user.getId();
        public static String name = user.getName();
    }

    @Deprecated(since = "5.4.0", forRemoval = true)
    public static class DmPingGuild {
        public static Guild guild;
        static {
            while (guild==null) {
                guild=instance.jda.getGuildById(Main.config.get("guildId", String.class));
            }
        }
    }
}
