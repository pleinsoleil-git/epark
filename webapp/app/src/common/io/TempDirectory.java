package common.io;

import java.io.File;
import java.nio.file.Files;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = true)
public class TempDirectory implements AutoCloseable {
	@Getter
	File m_directory;

	public TempDirectory() {
	}

	public TempDirectory(final File dir) {
		m_directory = dir;
	}

	public static TempDirectory create() throws Exception {
		return new TempDirectory(null);
	}

	public static TempDirectory create(final String prefix) throws Exception {
		return new TempDirectory(Files.createTempDirectory(prefix).toFile());
	}

	public File attach(final File dir) {
		try {
			return m_directory;
		} finally {
			m_directory = dir;
		}
	}

	public File detach() {
		try {
			return m_directory;
		} finally {
			m_directory = null;
		}
	}

	@Override
	public void close() throws Exception {
		if (m_directory != null) {
			try {
				if (m_directory.exists() == true) {
					FileUtils.forceDelete(m_directory);
				}
			} finally {
				log.debug(String.format("Delete directory[%s]", m_directory.getPath()));
			}
		}
	}
}
