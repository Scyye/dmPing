package ml.scyye.dmping.utils;

import com.sun.speech.freetts.*;
import com.sun.speech.freetts.audio.*;
import ml.scyye.dmping.Main;
import ml.scyye.dmping.commands.music.PlayerManager;

import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.nio.file.Path;

public class TextToSpeechUtil {
	public static void play(String message) {
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		Voice voice = VoiceManager.getInstance().getVoice("kevin16");
		voice.allocate();
		File audioFileTempFolder = Path.of("dmping-assets", "temp").toFile();

		audioFileTempFolder.mkdirs();
		var m = System.currentTimeMillis();
		AudioPlayer player = new SingleFileAudioPlayer(Path.of("dmping-assets", "temp", "audio-"+m).toString(), AudioFileFormat.Type.WAVE);
		voice.setAudioPlayer(player);

		boolean status = voice.speak(correctPronunciation(message));
		player.close();
		voice.deallocate();

		PlayerManager.instance.loadAndPlay(Main.instance.jda.getGuildById(Main.config.getGuildId()), "dmping-assets/temp/audio-"+m+".wav");
	}

	// TODO: Automatically translate to auto-ignore case regex
	public static String correctPronunciation(String message) {
		return message.replaceAll("[S-s][C-c][Y-y][Y-y][E-e]", "sigh")
				.replaceAll("\\.\\.\\.", "dot dot dot")
				.replaceAll("[B-b][R-r][B-b]", "be right back")
				.replaceAll("[G-g][T-t][G-g]", "got to go")
				// replace all instances of "fn" with a space on either side with "fortnite"
				.replaceAll("[F-f][N-n] ", "fort night")
				.replaceAll("[F-f][N-n][F-f]", "a really fucking shitty game with some blue haired emo");
	}
}
