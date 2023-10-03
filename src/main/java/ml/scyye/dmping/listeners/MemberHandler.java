package ml.scyye.dmping.listeners;

import ml.scyye.dmping.utils.S5AListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class MemberHandler extends S5AListener {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        // TODO: Remove following line, as dmPing will auto leave servers that arent Sub5Allts
        if (!isSub5Allts(event.getGuild())) return;
        if (event.getUser().getId().equals("569583404392382476")) return;
        event.getGuild().addRoleToMember(event.getMember(), Constants.Sub5Allts.guestTrue).complete();
    }

    @Deprecated(since = "5.1.1", forRemoval = true)
    private boolean isSub5Allts(Guild guild) {
        return guild.getId().equals(Constants.Sub5Allts.guild.getId());
    }


}
