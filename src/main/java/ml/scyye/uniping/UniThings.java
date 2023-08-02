package ml.scyye.uniping;

import ml.scyye.dmping.utils.Constants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static ml.scyye.dmping.utils.StringUtils.formatTime;
import static ml.scyye.dmping.utils.StringUtils.line;

public class UniThings {
    public static void printMessage(MessageReceivedEvent event) {
        System.out.println(line(250, "="));
        String st = String.format("%s (%s): %s\n%s", event.getAuthor().getEffectiveName(), event.getGuild().getName(), event.getMessage().getContentRaw(), formatTime(event.getMessage().getTimeCreated().toEpochSecond()));
        System.out.print(st+"\n");
        System.out.println(line(250,"="));
    }
}
