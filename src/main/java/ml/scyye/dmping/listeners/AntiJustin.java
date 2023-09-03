package ml.scyye.dmping.listeners;

import ml.scyye.dmping.utils.S2AListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import static ml.scyye.dmping.Main.print;

public class AntiJustin extends S2AListener {

    /**
     * Deprecated as spamming Dms isn't advised for bots.
     * Will be removed next update.
     * @param event
     */
    @Override
    @Deprecated(since = "5.4.0", forRemoval = true)
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().toLowerCase().contains("justin") || event.getAuthor().isBot())  return;
        print("AntiJustin must be disabled by the next update.");
        for (int i = 0; i < 5; i++) {
            event.getAuthor().openPrivateChannel().complete().sendMessage(new MessageCreateBuilder().addFiles()
                    .build()).queue();
        }

    }
}
