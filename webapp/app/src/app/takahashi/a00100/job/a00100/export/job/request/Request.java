package app.takahashi.a00100.job.a00100.export.job.request;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import app.takahashi.a00100.job.a00100.export.job.request.report.Report;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Request {
	static Request m_instance;
	_Current m_current;

	Request() {
	}

	public static Request getInstance() {
		return (m_instance == null ? m_instance = new Request() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
		try {
			for (val x : query()) {
				(m_current = x).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	Collection<_Current> query() throws Exception {
		return new ArrayList<_Current>() {
			{
				add(new _Current() {
					{
						m_outputPath = new File("D:/Temp", "a.xlsx").getPath();
					}
				});
			}
		};
	}

	@Data
	public static class _Current {
		String m_outputPath;

		public void execute() throws Exception {
			report();
		}

		void report() throws Exception {
			Report.getInstance().execute();
		}
	}
}
