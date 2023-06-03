package ml.Scyye.dmPing.commands;

import ml.Scyye.dmPing.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.*;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

import static ml.Scyye.dmPing.ScyyeThings.*;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String ranCommand = event.getName();

        switch (ranCommand) {
            case "shutdown" -> {
                boolean worked = event.getUser().getId().equals(Scyye.id);
                if (!worked){ event.reply("You can't do that!").complete(); print(event.getUser().getAsTag()+ " Attempted to shutdown the bot, but wasn't cool enough!"); return;}
                print(event.getUser().getAsTag()+ " Successfully shutdown the bot, they must be cool!");
                event.getJDA().getHttpClient().connectionPool().evictAll();
                event.getJDA().getHttpClient().dispatcher().executorService().shutdown();
                event.getJDA().shutdown();
            }
            case "blacklist" -> {
                User user = Objects.requireNonNull(event.getOption("user")).getAsUser();
                if (user.getId().equalsIgnoreCase(event.getUser().getId())){ event.reply("Why in the fucking world would you wanna blacklist yourself? You stupid fucking human.").setEphemeral(true).complete(); return;}
                if (blacklist.contains(user.getId())){ event.reply("Couldn't add " + user.getAsTag() +" because they're already blacklisted!").setEphemeral(true).complete(); return;}
                if (!blacklist.contains(user.getId()))blacklist.add(user.getId());
                event.reply("Added " + user.getAsMention() + " to the blacklist. keep in mind you have to readd them every reboot.").setEphemeral(true).queue();
            }
            case "convert-to-short" -> {
                int num = Objects.requireNonNull(event.getOption("num")).getAsInt();
                String sh = convertToShort(num);
                print(sh);
                event.deferReply().queue();
                event.getHook().sendMessage(sh).setEphemeral(true).queue();
            }
            case "unblacklist" -> {
                User user = Objects.requireNonNull(event.getOption("user1")).getAsUser();
                if (user.getId().equalsIgnoreCase(event.getUser().getId())){ event.reply("Kid really thought he could do that lmfao").complete(); return;}
                if (!blacklist.remove(user.getId())) event.reply("User isn't blacklisted!").setEphemeral(true).queue();
                event.reply("Removed " + user.getAsTag() + " from the blacklist!").setEphemeral(true).queue();
            }
            case "cache" -> {
                if (!event.getUser().getAsTag().contains("Scyye")) return;
                boolean status = event.getOption("status")==null ? !cache : event.getOption("status").getAsBoolean();
                cache = status;
                event.reply("Set status of caching to " + status).setEphemeral(true).queue();
            }
            case "list-blacklist" -> {
                EmbedBuilder builder = new EmbedBuilder();
                if (blacklist.isEmpty()) {
                    builder.addField("No Blacklisted Users", "The blacklist is empty!", false);
                } else {
                    for (String id : blacklist) {
                        User user = event.getGuild().getMemberById(id).getUser();
                        builder.addField(user.getAsTag(), id, false);
                    }
                }
                MessageEmbed embed = builder
                        .setAuthor(Scyye.name)
                        .setTitle("**__Blacklist__**")
                        .build();
                event.replyEmbeds(embed).queue();
            }
            case "changelog" -> {
                EmbedBuilder builder = new EmbedBuilder();
                builder.addField("1.0.0", limitWords("Added all functionality from root's dmPing bot (do /original-js to view the original code for dmPing by Tessy)", 45), false)
                        .addField("1.1.0", limitWords("Fixed many bugs, including spamming breaking the bot, and the bot infinitely pining itself.", 45), false)
                        .addField("2.0.0", limitWords("Added a 'Deleter' which automatically deletes messages sent by the bot every 2-3 minutes, to prevent the bot from clogging channels.", 45), false)
                        .addField("3.0.0 - 4.1.0", limitWords("Fixed bugs", 45), true)
                        .addField("5.0.0", limitWords("Implemented the blacklist. /blacklist <User> to blacklist someone. /unblacklist <User> also works.", 45), false)
                        .addField("6.0.0", limitWords("Added the Anti-Delete Functionality. Where the bot caches messages, and prevents deletion of messages.", 45), false)
                        .addField("6.1.0", limitWords("Redid most of the commands, added a command that allows listing blacklisted users, as well as responses for trying to blacklist/unblacklist yourself.", 45), false)
                        .addField("6.2.0", limitWords("Added /changelog, /version, and /original-js allowing you to view the entire history of the bot.", 45), false)
                        .addField("6.2.1", limitWords("Updated TOKEN (thanks <@460904913732370434> bitch), cleaned up the code a bit, and added a MemberHandler that automatically gives <@&1058256598084096070>", 45), false)
                        .addField("6.3.0", limitWords("Removed a bunch of spaghetti code, cleaned up the code even more. Added /github (/source) that sends you the github for the bot. Added a check for everyone pings (double pinging used to happen. As well as starting to add support for other guilds to use the bot; coming in the next full update.", 45), false)
                        .addField("6.4.0", limitWords("Added edit logging to the AntiDelete functionality (CACHING MUST BE ENABLED FOR THIS TO WORK). Added a few more things to prepare for public release 1. The bot now joins general in Sub5Allts whenever it goes online.", 45), false)
                        .addField("6.4.1", limitWords("Added more things preparing for the public release. It will all be tested soon, and 7.0 will come out then.", 45), false)


                ;

                MessageEmbed embed = builder
                        .setAuthor("Scyye + root")
                        .setColor(Color.BLUE)
                        .setImage(Scyye.user.getAvatarUrl())
                        .setTitle("**Changelog**")
                        .build();

                event.replyEmbeds(embed).setEphemeral(true).queue();
            }
            case "original-js" -> event.reply(
                    """
                            ```js
                            const Discord = require('discord.js');
                            const client = new Discord.Client();

                            client.on('ready', () => {
                            \tconsole.log('running');
                            }),

                            client.on('message', msg => {
                              if (msg.author == client.user.id) {
                                msg.delete();
                              }else if (!msg.author.bot){
                                var ping = "";
                                msg.channel.members.filter(u => u.id != msg.author.id && !msg.mentions.has(u.user)).each(u => {ping += `${u.toString()} `});
                                if(ping == "")return;
                                msg.channel.send(ping);
                                console.log(ping);
                              }
                            \tconsole.log(msg.id);
                            });
                            ```
                            """).setEphemeral(true).queue();
            case "version" -> event.reply("6.4.1").setEphemeral(true).queue();
            case "clear-cache" -> {
                TextInput clearId = TextInput.create("clear-id", "Clear Id", TextInputStyle.SHORT)
                        .setPlaceholder("Enter your UUID that was provided when enabling the bot.")
                        .setRequiredRange(36, 36)
                        .build();
                Modal modal = Modal.create("cache-clear", "Cache Clear")
                        .addComponents(ActionRow.of(clearId))
                        .build();

                event.replyModal(modal).queue();
            }
            case "id" ->  {
                if (event.getUser().equals(Scyye.user)) {
                    event.reply(Main.instance.cacheClearCode.toString()).setEphemeral(true).queue();
                }
                else event.reply("Only " + Scyye.id + " can do that!").queue();
            }
            case "source", "github" -> event.reply("https://github.com/Scyye/dmPing/").setEphemeral(true).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals("cache-clear")) return;
        try {
            if (UUID.fromString(event.getValue("clear-id").getAsString()).equals(Main.instance.cacheClearCode)) {
                for (var m : Sub5Allts.cachingChannel.getIterableHistory()) {
                    m.delete().complete();
                }
                event.reply("Successfully cleared cache.").setEphemeral(true).queue();
                print(Main.instance.randomizeCacheCode().toString());
            } else {
                event.getHook().sendMessage("Invalid UUID.").setEphemeral(true).queue();
            }
        } catch (IllegalArgumentException e) {
            event.getHook().sendMessage("Invalid UUID.").setEphemeral(true).queue();
        }

    }

    // Guild Commands =-= instant update (max 100)

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        List<CommandData> commandData = new ArrayList<>();

        commandData.add(Commands.slash("shutdown", "shutsdown the bot"));

        OptionData optionData = new OptionData(OptionType.USER, "user", "the user to blacklist", true);
        commandData.add(Commands.slash("blacklist", "blacklists a user").addOptions(optionData));

        OptionData optionData1 = new OptionData(OptionType.INTEGER, "num", "the num to convert", true);
        commandData.add(Commands.slash("convert-to-short", "converts a number to short form").addOptions(optionData1));

        OptionData optionData2 = new OptionData(OptionType.USER, "user1", "the user to unblacklist", true);
        commandData.add(Commands.slash("unblacklist", "unblacklists a user").addOptions(optionData2));

        OptionData optionData3 = new OptionData(OptionType.BOOLEAN, "status", "Turn boolean on or off?");
        commandData.add(Commands.slash("cache", "toggle cache").addOptions(optionData3));

        commandData.add(Commands.slash("list-blacklist", "Lists all users in the blacklist."));

        commandData.add(Commands.slash("changelog", "Shows (most) versions of dmPing, and all info about them."));

        commandData.add(Commands.slash("original-js", "Shows you the original javascript version of the bot, written by root."));

        commandData.add(Commands.slash("version", "displays the current version of the bot."));

        commandData.add(Commands.slash("clear-cache", "clears the cache"));

        commandData.add(Commands.slash("id", "the id for cache clearing"));

        commandData.add(Commands.slash("source", "Sends you the source code"));
        commandData.add(Commands.slash("github", "Sends you the source code"));

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
