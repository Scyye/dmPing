package dev.scyye.dmping.listeners;

import dev.scyye.dmping.utils.S2AListener;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

import java.util.*;

public class Sub5AlltsOnlyListener extends S2AListener {
    public static List<Member> sub5AlltsMembers = new ArrayList<>();

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().loadMembers().onSuccess(members -> {
            sub5AlltsMembers.addAll(members);
        });
    }
}
