package ml.scyye.dmping.utils;

import lombok.Getter;
import lombok.Setter;

@Deprecated(since = "5.4.5", forRemoval = true)
public class Config {
    @Getter
    private final String token;

    @Getter
    private final String guildId;
    @Getter
    private final String ownerId;

    @Getter
    private final String version;
    @Getter
    private final boolean beta;
    @Getter
    private final boolean devMode;


    private Config(String token, String guildId, String ownerId, String version, boolean beta, boolean devMode) {
        this.token = token;
        this.guildId = guildId;
        this.ownerId=ownerId;
        this.version = version;
        this.beta = beta;
        this.devMode = devMode;
    }

    @Deprecated(since = "5.4.5", forRemoval = true)
    public static Config defaultConfig() {
        return new Config(
                "MTExMjc1ODMyNDQxMzM0NTgwMg.G4y60_.bi1veaufhLhpBvl0iA2sfOylzAM01CeWa2htsE",
                "1139672380109762630",
                "553652308295155723",
                "0.0.0",
                false,
                false
        );
    }
}
