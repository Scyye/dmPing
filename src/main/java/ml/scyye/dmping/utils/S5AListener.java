package ml.scyye.dmping.utils;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static ml.scyye.dmping.utils.LoggingUtils.printStartup;

public class S5AListener extends ListenerAdapter {
    public S5AListener() {
        printStartup(this.getClass().getName()+" enabled and listening.");
    }
}
