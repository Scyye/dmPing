package dev.scyye.dmping.utils;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class S2AListener extends ListenerAdapter {
    public S2AListener() {
        System.out.println(this.getClass().getName()+" enabled and listening.");
    }
}
