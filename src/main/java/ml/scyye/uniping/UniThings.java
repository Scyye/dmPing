package ml.scyye.uniping;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class UniThings {
    public static void printMessage(MessageReceivedEvent event) {
        System.out.println("=".repeat(250));
        String st = String.format("%s (%s): %s", event.getAuthor().getEffectiveName(), event.getGuild().getName(), event.getMessage().getContentRaw());
        System.out.print(st+"\n");
        System.out.println("=".repeat(250));
    }
}
