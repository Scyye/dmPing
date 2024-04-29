package dev.scyye.dmping.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.scyye.dmping.listeners.Antidelete;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		try {
			TypeToken<List<Antidelete.CachedMessage>> typeToken = new TypeToken<>() {

			};
			return new Gson().fromJson(Files.readString(Path.of("dmping-assets/cache.json")), typeToken.getType());
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static Antidelete.CachedMessage findMessageById(String id) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		if (id == null)
			return new Antidelete.CachedMessage();

		List<Antidelete.CachedMessage> cachedMessages = findAllCachedMessages()
				.stream()
				.filter(cachedMessage -> cachedMessage.messageId.equals(id))
				.collect(Collectors.toList());

		System.out.println(gson.toJson(cachedMessages));

		return cachedMessages.isEmpty() ? new Antidelete.CachedMessage() : cachedMessages.get(0);
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
