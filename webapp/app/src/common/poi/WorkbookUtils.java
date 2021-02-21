package common.poi;

import java.io.File;

import org.apache.poi.ss.usermodel.Workbook;

import common.io.FileUtils;
import lombok.val;

public class WorkbookUtils {
	public static void save(final Workbook book, final File file) throws Exception {
		try (val stream = FileUtils.openOutputStream(file)) {
			book.write(stream);
			stream.flush();
		}
	}
}
