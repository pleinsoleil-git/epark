package common.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import common.lang.StandardCharsets;

public class InputStreamUtils {
	public static InputStreamReader openInputStreamReader(final InputStream input, final Charset charset) throws Exception {
		return new InputStreamReader(input, charset.name());
	}

	public static InputStreamReader openInputStreamReader(final InputStream input) throws Exception {
		return openInputStreamReader(input, StandardCharsets.UTF_8);
	}

	public static BufferedReader openBufferedReader(final InputStream input, final Charset charset) throws Exception {
		return new BufferedReader(openInputStreamReader(input, charset));
	}

	public static BufferedReader openBufferedReader(final InputStream input) throws Exception {
		return openBufferedReader(input, StandardCharsets.UTF_8);
	}
}
