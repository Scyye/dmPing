package ml.scyye.dmping.utils;

import lombok.Getter;

import javax.swing.text.Style;
import java.util.ArrayList;
import java.util.List;

public class Config {
    @Getter
    private String token;
    @Getter
    private String uniToken;
    @Getter
    private String guildId;
    @Getter
    private String ownerId;

    @Getter
    private String version;
    @Getter
    private boolean beta;
    @Getter
    private boolean devMode;

    @Getter
    private List<String> blacklist;

    private Config(String token, String uniToken, String guildId, String version, boolean beta, boolean devMode, List<String> blacklist) {
        this.token = token;
        this.uniToken = uniToken;
        this.guildId = guildId;
        this.version = version;
        this.beta = beta;
        this.devMode = devMode;
        this.blacklist = blacklist;
    }

    public static Config defaultConfig() {
        return new Config(
                "MTExMjc1ODMyNDQxMzM0NTgwMg.G4y60_.bi1veaufhLhpBvl0iA2sfOylzAM01CeWa2htsE",
                "MTEwNDQzMjk3OTk2ODgwNjk4Mw.Gzms4p.1vcaFxiJHwTKx8S1ARDIthzl62SvDdGi6JpEbM",
                "1139672380109762630",
                "0.0.0",
                false,
                false,
                new ArrayList<>()
        );
    }
}
