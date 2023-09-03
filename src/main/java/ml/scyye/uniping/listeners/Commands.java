package ml.scyye.uniping.listeners;

import com.github.kaktushose.jda.commands.annotations.interactions.Interaction;
import com.github.kaktushose.jda.commands.annotations.interactions.SlashCommand;
import com.github.kaktushose.jda.commands.dispatching.commands.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

@Interaction
public class Commands {
    @SlashCommand(value = "changelog", desc = "View the entire history of the bot, with all updates and everything!", ephemeral = true)
    public void onChangeLog(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("1.0.0", "Added the base functionality of Scyye's Java dmPing Port. More info coming soon.", false)
        ;

        builder
                .setAuthor("Scyye")
                .setColor(Color.BLUE)
                .setTitle("**Changelog**");

        event.reply(builder);
    }
}
