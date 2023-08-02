package ml.scyye.dmping.listeners;

import ml.scyye.dmping.utils.S5AListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

public class AntiJustin extends S5AListener {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().toLowerCase().contains("justin") || event.getAuthor().isBot())  return;
        for (int i = 0; i < 5; i++) {
            event.getAuthor().openPrivateChannel().complete().sendMessage(new MessageCreateBuilder().addFiles()
                    .build()).queue();
        }

    }
}
