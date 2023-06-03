package ml.Scyye.dmPing.listeners;

import ml.Scyye.dmPing.ScyyeThings;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberHandler extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (!event.getGuild().equals(ScyyeThings.Sub5Allts.guild)) return;
        event.getGuild().addRoleToMember(event.getMember(), ScyyeThings.Sub5Allts.guestTrue).complete();
    }


}
