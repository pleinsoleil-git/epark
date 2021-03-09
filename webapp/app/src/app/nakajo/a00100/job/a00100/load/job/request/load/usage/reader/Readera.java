package app.nakajo.a00100.job.a00100.load.job.request.load.usage.reader;

import java.io.File;

import org.apache.commons.compress.utils.FileNameUtils;

import app.nakajo.a00100.job.a00100.load.job.request.Request;
import app.nakajo.a00100.job.a00100.load.job.request.load.usage.Record;
import lombok.val;

public abstract class Readera implements AutoCloseable {
	public static Readera getInstance() {
		val request = Request.getCurrent();
		val file = new File(request.getInputFile());

		switch (FileNameUtils.getExtension(file.getName())) {
		case "txt":
		case "csv":
		case "tsv":
			return null;
		case "xlsx":
			return null;
		default:
			break;
		}

		throw new IllegalArgumentException();
	}

	public abstract Record read() throws Exception;
}
