package dev.scyye.dmping.utils;

import com.google.gson.Gson;
import dev.scyye.dmping.listeners.Antidelete;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// TODO: Readd SQLiteUtils and make it work lol
public class SQLiteUtils {
	static List<Antidelete.CachedMessage> cache = new ArrayList<>();

	public static void init() {
		try {
			cache = new Gson().fromJson(Files.readString(Path.of("dmping-assets/cache.json")), List.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void save() {
		try {
			Files.writeString(Path.of("dmping-assets/cache.json"), new Gson().toJson(cache));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static List<Antidelete.CachedMessage> findAllCachedMessages() {
        return cache;
    }

	public static Antidelete.CachedMessage findMessageById(String id) {
		if (id==null)
			return new Antidelete.CachedMessage();
		return findAllCachedMessages().stream().filter(cachedMessage -> cachedMessage.messageId.equals(id))
				.findFirst().orElse(new Antidelete.CachedMessage());
	}

	public static void insertEntry(String messageId, String authorId, String content) {
		cache.add(new Antidelete.CachedMessage(messageId, authorId, content));
		save();
	}

	public static void insertEntry(Antidelete.CachedMessage cachedMessage) {
		cache.add(cachedMessage);
		save();
	}

	public static void removeEntry(String messageId) {
		cache.remove(findMessageById(messageId));
		save();
	}

	public static void updateEntry(String messageId, String authorId, String content) {
		Antidelete.CachedMessage cachedMessage = findMessageById(messageId);
		cache.remove(cachedMessage);
		cachedMessage.authorId = authorId;
		cachedMessage.content = content;
		cache.add(cachedMessage);
		save();
	}
}
