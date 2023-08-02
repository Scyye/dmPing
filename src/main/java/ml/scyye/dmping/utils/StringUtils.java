package ml.scyye.dmping.utils;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static ml.scyye.dmping.Main.instance;

public class StringUtils {
    public static String convertToShort(int num) {
        if (num % 1000000 == 0) {
            return num/1000000+"m";
        } else
        if (num % 1000 == 0) {
            return num/1000+"k";
        } else {
            return Integer.toString(num);
        }
    }

    public static boolean isUrl(String st) {
        try {
            new URI(st);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public static String formatTime(long duration) {
        long hours = duration / TimeUnit.HOURS.toMillis(1);
        long minutes = duration / TimeUnit.MINUTES.toMillis(1);
        long seconds = duration % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }



    public static String replacePingWithName(String s) {
        return instance.jda.getUserById(s.replace("<", "").replace("@", "").replace(">", "")).getName();
    }

    // Makes a line out of object with specified length
    @NotNull
    public static String line(int length, @NotNull String object) {
        return object.repeat(Math.max(0, length));
    }

    public static String convertToMediaWebhookName(User user) {
        return String.format("%s (#media webhook)", user.getEffectiveName());
    }








    public static String limitWords(String originalText, int charLimit) {
        StringBuilder output = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();

        for (String word : originalText.split(" ")) {
            if (currentLine.length() + word.length() <= charLimit) {
                // If the word fits on the current line, add it to the line
                currentLine.append(word).append(" ");
            } else if (word.length() > charLimit) {
                // If the word is longer than the character limit, split it into multiple lines
                for (int i = 0; i < word.length(); i += charLimit) {
                    int endIndex = Math.min(i + charLimit, word.length());
                    String subWord = word.substring(i, endIndex);
                    if (currentLine.length() + subWord.length() > charLimit) {
                        output.append(currentLine).append("\n");
                        currentLine = new StringBuilder();
                    }
                    currentLine.append(subWord).append(" ");
                }
            } else {
                // If the word doesn't fit on the current line, finish the word and start a new line
                output.append(currentLine).append("\n");
                currentLine = new StringBuilder(word).append(" ");
            }
        }

        return output.append(currentLine).toString();
    }
}
