package common.io;

import java.io.File;
import java.nio.file.Files;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = true)
public class TempFile implements AutoCloseable {
	@Getter
	File m_file;

	public TempFile() {
	}

	public TempFile(final File file) {
		m_file = file;
	}

	public static TempFile create() throws Exception {
		return create(null, null);
	}

	public static TempFile create(final String prefix, final String suffix) throws Exception {
		return new TempFile(Files.createTempFile(prefix, suffix).toFile());
	}

	public File attach(final File file) {
		try {
			return m_file;
		} finally {
			m_file = file;
		}
	}

	public File detach() {
		try {
			return m_file;
		} finally {
			m_file = null;
		}
	}

	@Override
	public void close() throws Exception {
		if (m_file != null) {
			try {
				if (m_file.exists() == true) {
					FileUtils.forceDelete(m_file);
				}
			} finally {
				log.debug(String.format("Delete file[%s]", m_file.getPath()));
			}
		}
	}
}
