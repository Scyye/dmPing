package ml.scyye.dmping.commands;

import com.github.kaktushose.jda.commands.annotations.interactions.Interaction;
import com.github.kaktushose.jda.commands.annotations.interactions.Param;
import com.github.kaktushose.jda.commands.annotations.interactions.SlashCommand;
import com.github.kaktushose.jda.commands.dispatching.commands.CommandEvent;
import ml.scyye.dmping.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

@Interaction
public class BlacklistCommands {
    @SlashCommand(value = "blacklist add", desc = "Adds a user to the blacklist.", ephemeral = true, enabledFor = Permission.BAN_MEMBERS)
    public void onBlacklistAdd(CommandEvent event, @Param("The user to add to the blacklist") User target) {
        if (target.getId().equalsIgnoreCase(event.getUser().getId()) || Main.config.getBlacklist().contains(target.getId())){
            event.reply("There was an error adding `" + target.getId() + "` ("+target.getName()+") to the blacklist, are you sure they exist? Maybe it's you, or the person is already blacklisted! do /blacklist list to view the blacklist!");
            return;
        }
        Main.config.getBlacklist().add(target.getId());
        event.reply("Added " + target.getAsMention() + " to the blacklist. Keep in mind you have to re-add them every reboot.");
    }

    @SlashCommand(value = "blacklist remove", desc = "Removes a user from the blacklist.", ephemeral = true, enabledFor = Permission.BAN_MEMBERS)
    public void onBlacklistRemove(CommandEvent event, @Param("The user to remove from the blacklist") User target) {
        if (target.getId().equalsIgnoreCase(event.getUser().getId())){
            event.reply("There was an error removing `"+target.getId()+"` ("+target.getName()+") from the blacklist, are you sure they exist? Maybe it's you, or the person isn't blacklisted? do /blacklist list to view the blacklist"); return;
        }
        Main.config.getBlacklist().remove(target.getId());
        event.reply("Removed " + target.getName() + " from the blacklist!");
    }

    @SlashCommand(value = "blacklist list", desc = "Lists the blacklist.", ephemeral = true, enabledFor = Permission.BAN_MEMBERS)
    public void onBlacklistList(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        if (Main.config.getBlacklist().isEmpty()) {
            builder.addField("No Blacklisted Users", "The blacklist is empty!", false);
        } else {
            for (String id : Main.config.getBlacklist()) {
                User user = event.getGuild().getMemberById(id).getUser();
                builder.addField(user.getName(), id, false);
            }
        }
        builder.setAuthor(Constants.Scyye.name)
                .setTitle("**__Blacklist__**");
        event.reply(builder);
    }
}
