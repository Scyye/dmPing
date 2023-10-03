package ml.scyye.dmping.utils;

import java.io.*;

public class IOUtils {
	public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] bytes = new byte[8192];
		int n;
		while (-1 != (n = inputStream.read(bytes))) {
			outputStream.write(bytes, 0, n);
		}
	}
}
