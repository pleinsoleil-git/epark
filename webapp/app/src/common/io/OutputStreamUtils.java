package common.io;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import common.lang.StandardCharsets;

public class OutputStreamUtils {
	public static OutputStreamWriter openOutputStreamWriter(final OutputStream output, final Charset charset) throws Exception {
		return new OutputStreamWriter(output, charset.name());
	}

	public static OutputStreamWriter openOutputStreamWriter(final OutputStream output) throws Exception {
		return openOutputStreamWriter(output, StandardCharsets.UTF_8);
	}

	public static BufferedWriter openBufferedWriter(final OutputStream output, final Charset charset) throws Exception {
		return new BufferedWriter(openOutputStreamWriter(output, charset));
	}

	public static BufferedWriter openBufferedWriter(final OutputStream output) throws Exception {
		return openBufferedWriter(output, StandardCharsets.UTF_8);
	}
}
