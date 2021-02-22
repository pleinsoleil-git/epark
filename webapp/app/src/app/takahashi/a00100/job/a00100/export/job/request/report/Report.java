package app.takahashi.a00100.job.a00100.export.job.request.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import app.takahashi.a00100.job.a00100.export.job.request.Request;
import app.takahashi.a00100.job.a00100.export.job.request.report.compare.Compare;
import app.takahashi.a00100.job.a00100.export.job.request.report.match.Match;
import app.takahashi.a00100.job.a00100.export.job.request.report.reserve.Reserve;
import app.takahashi.a00100.job.a00100.export.job.request.report.top.Top;
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
		Workbook m_book;

		public Workbook getBook() {
			if (m_book == null) {
				try (val is = getClass().getResourceAsStream("Report.xlsx")) {
					m_book = WorkbookFactory.create(is);
				} catch (Exception e) {
					log.error("", e);
				}
			}

			return m_book;
		}

		public void execute() throws Exception {
			output();
		}

		void output() throws Exception {
			try (val book = getBook()) {
				top();
				reserve();
				compare();
				match();
				WorkbookUtils.save(book, new File(Request.getCurrent().getOutputPath()));
			}
		}

		void top() throws Exception {
			Top.getInstance().execute();
		}

		void reserve() throws Exception {
			Reserve.getInstance().execute();
		}

		void compare() throws Exception {
			Compare.getInstance().execute();
		}

		void match() throws Exception {
			Match.getInstance().execute();
		}
	}
}
