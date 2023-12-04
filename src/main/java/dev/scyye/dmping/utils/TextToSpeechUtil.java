package dev.scyye.dmping.utils;

import com.sun.speech.freetts.*;
import com.sun.speech.freetts.audio.*;
import dev.scyye.dmping.Main;
import dev.scyye.dmping.commands.music.PlayerManager;

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

		PlayerManager.instance.loadAndPlay(Main.instance.jda.getGuildById(Main.config.get("guildId", String.class)), "dmping-assets/temp/audio-"+m+".wav");
	}

	// TODO: Automatically translate to auto-ignore case regex
	public static String correctPronunciation(String message) {
		return message.replaceAll("scyye", "sigh")
				.replaceAll("\\.\\.\\.", "dot dot dot")
				.replaceAll("brb", "be right back")
				.replaceAll("gtg", "got to go")
				// replace all instances of "fn" with a space on either side with "fortnite"
				.replaceAll("fn ", "fort night")
				.replaceAll("fnf", "a really fucking shitty game with some blue haired emo");
	}
}
