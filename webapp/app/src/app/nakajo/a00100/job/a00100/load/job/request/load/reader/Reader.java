package app.nakajo.a00100.job.a00100.load.job.request.load.reader;

import java.io.File;

import org.apache.commons.compress.utils.FileNameUtils;

import app.nakajo.a00100.job.a00100.load.job.request.Request;
import app.nakajo.a00100.job.a00100.load.job.request.load.Record;
import lombok.val;

public abstract class Reader implements AutoCloseable {
	public static Reader getInstance() {
		val request = Request.getCurrent();
		val file = new File(request.getInputFile());

		switch (FileNameUtils.getExtension(file.getName())) {
		case "csv":
			return new CSVReader();
		case "xlsx":
			return new ExcelReader();
		default:
			break;
		}

		throw new IllegalArgumentException();
	}

	public abstract Record read() throws Exception;
}
