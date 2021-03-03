package app.nakajo.a00100.job.a00100.export.job.request.report.member;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Sheet;

import app.nakajo.a00100.job.a00100.export.job.request.report.Report;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Member {
	static Member m_instance;
	_Current m_current;

	Member() {
	}

	public static Member getInstance() {
		return (m_instance == null ? m_instance = new Member() : m_instance);
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
				add(new _Current());
			}
		};
	}

	@Data
	public static class _Current {
		Sheet m_sheet;

		public Sheet getSheet() {
			if (m_sheet == null) {
				val book = Report.getCurrent().getWorkbook();
				m_sheet = book.getSheet("EPARK会員情報");
			}

			return m_sheet;
		}

		public void execute() throws Exception {
			all();
		}

		void all() throws Exception {
			All.getInstance().execute();
		}
	}
}