package ml.scyye.dmping.utils;

public class LoggingUtils {
    // Easy print util
    public static void print(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String st : strings) {
            builder.append(st).append("\n");
            System.out.println(st);
        }

        Constants.Sub5Allts.controlPanel.sendMessage(builder.toString()).queue();
        // if (Scyye.s5aMember==null) {
        //  Scyye.s5aMember = Sub5Allts.guild.getMemberById(553652308295155723L);
        // }
        // if (!builder.toString().isEmpty()) sendMessage(Scyye.user, builder.toString());
    }

    public static void printStartup(String... strings) {
        for (String st : strings) {
            System.out.println(st);
        }
    }
}
