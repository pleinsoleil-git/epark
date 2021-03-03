package common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class FileUtils extends org.apache.commons.io.FileUtils {
	public static FileInputStream openInputStream(final String path) throws Exception {
		return openInputStream(new File(path));
	}

	public static InputStreamReader openInputReader(final String path, final Charset charset) throws Exception {
		return openInputReader(openInputStream(path), charset);
	}

	public static InputStreamReader openInputReader(final File file, final Charset charset) throws Exception {
		return openInputReader(openInputStream(file), charset);
	}

	public static InputStreamReader openInputReader(final InputStream stream, final Charset charset) throws Exception {
		return new InputStreamReader(stream, charset.name());
	}
}
