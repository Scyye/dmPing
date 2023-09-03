package ml.scyye.dmping.utils;

import ml.scyye.dmping.Main;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;

import java.util.*;

import static ml.scyye.dmping.Main.instance;
import static ml.scyye.dmping.utils.APIGetNotNullUtils.ensureGetChannelByName;
import static ml.scyye.dmping.utils.APIGetNotNullUtils.ensureGetRoleByName;

public class Constants {
    public static class MentionUsers{
        String pingNames;
        String pingString;

        public String getPingNames() {
            return pingNames;
        }
        public String getPingString() {
            return pingString;
        }
        public MentionUsers(String pingNames, String pingString) {
            this.pingNames = pingNames;
            this.pingString = pingString;
        }
    }

    public static class Scyye {
        public static User user = Main.instance.jda.retrieveUserById(Main.config.getOwnerId()).complete();
        public static String id = user.getId();
        public static String name = user.getName();
    }

    @Deprecated(since = "5.4.0")
    public static class DmPingGuild {
        public static Guild guild;
        static {
            while (guild==null) {
                guild=instance.jda.getGuildById(Main.config.getGuildId());
            }
        }
    }
}
