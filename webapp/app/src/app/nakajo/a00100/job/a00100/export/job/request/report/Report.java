package app.nakajo.a00100.job.a00100.export.job.request.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import app.nakajo.a00100.job.a00100.export.job.Job;
import app.nakajo.a00100.job.a00100.export.job.request.report.member.Member;
import app.nakajo.a00100.job.a00100.export.job.request.report.takeOut.TakeOut;
import common.poi.WorkbookUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Report {
	static Report m_instance;
	_Current m_current;

	Report() {
	}

	public static Report getInstance() {
		return (m_instance == null ? m_instance = new Report() : m_instance);
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
		Workbook m_workbook;

		public Workbook getWorkbook() {
			if (m_workbook == null) {
				try (val rs = getClass().getResourceAsStream("Report.xlsx")) {
					m_workbook = WorkbookFactory.create(rs);
				} catch (Exception e) {
					log.error("", e);
				}
			}

			return m_workbook;
		}

		public void execute() throws Exception {
			output();
		}

		void output() throws Exception {
			try (val book = getWorkbook()) {
				//member();
				takeOut();
				WorkbookUtils.save(book, new File(Job.getCurrent().getOutputFile()));
			}
		}

		void member() throws Exception {
			Member.getInstance().execute();
		}

		void takeOut() throws Exception {
			TakeOut.getInstance().execute();
		}
	}
}
