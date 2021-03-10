package app.nakajo.a00100.job.a00100.export.job.request.report.takeOut;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;

import app.nakajo.a00100.job.a00100.export.job.request.report.Report;
import app.nakajo.a00100.job.a00100.export.job.request.report.ReportType;
import common.jdbc.JDBCUtils;
import common.poi.CellUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class Evaluation {
	static Evaluation m_instance;
	_Current m_current;

	Evaluation() {
	}

	static Evaluation getInstance() {
		return (m_instance == null ? m_instance = new Evaluation() : m_instance);
	}

	static _Current getCurrent() {
		return getInstance().m_current;
	}

	void execute() throws Exception {
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
	static class _Current {
		static final int FIRST_ROW_NUM = 1;
		static final int FIRST_CELL_NUM = CellReference.convertColStringToIndex("A");
		Sheet m_sheet;

		public Sheet getSheet() {
			if (m_sheet == null) {
				val book = Report.getCurrent().getWorkbook(ReportType.EVALUATION);
				m_sheet = book.getSheet("テイクアウト");
			}

			return m_sheet;
		}

		void execute() throws Exception {
			output();
		}

		void output() throws Exception {
			val sheet = getSheet();
			int rowNum = FIRST_ROW_NUM;
			val styles = new HashMap<Integer, CellStyle>() {
				{
					val row = CellUtil.getRow(FIRST_ROW_NUM, sheet);
					for (val cell : row) {
						val style = cell.getCellStyle();
						if (style != null) {
							put(cell.getColumnIndex(), style);
						}
					}
				}
			}.entrySet();

			for (val rec : query()) {
				val row = CellUtil.getRow(rowNum++, sheet);

				// --------------------------------------------------
				// スタイルを適応する
				// --------------------------------------------------
				for (val x : styles) {
					CellUtils.setCellStyle(row, x.getKey(), x.getValue());
				}

				// --------------------------------------------------
				// 値をセット
				// --------------------------------------------------
				int cellNum = FIRST_CELL_NUM;
				for (val x : rec) {
					CellUtils.setCellValue(row, cellNum++, x);
				}
			}
		}

		Collection<Object[]> query() throws Exception {
			String sql;
			sql = "SELECT t10.data_type,\n"
				+ "t10.evaluation,\n"
				+ "t10.usage_month,\n"
				+ "CASE\n"
					+ "WHEN t10.last_6_month = 0 THEN 0\n"
					+ "ELSE TRUNC( t10.after_30_day / t10.last_6_month, 3 )\n"
				+ "END,\n"
				+ "CASE\n"
					+ "WHEN t10.last_6_month = 0 THEN 0\n"
					+ "ELSE TRUNC( t10.after_60_day / t10.last_6_month, 3 )\n"
				+ "END,\n"
				+ "CASE\n"
					+ "WHEN t10.last_6_month = 0 THEN 0\n"
					+ "ELSE TRUNC( t10.after_90_day / t10.last_6_month, 3 )\n"
				+ "END,\n"
				+ "CASE\n"
					+ "WHEN t10.last_6_month = 0 THEN 0\n"
					+ "ELSE TRUNC( t10.after_120_day / t10.last_6_month, 3 )\n"
				+ "END,\n"
				+ "CASE\n"
					+ "WHEN t10.last_6_month = 0 THEN 0\n"
					+ "ELSE TRUNC( t10.after_150_day / t10.last_6_month, 3 )\n"
				+ "END,\n"
				+ "CASE\n"
					+ "WHEN t10.last_6_month = 0 THEN 0\n"
					+ "ELSE TRUNC( t10.after_180_day / t10.last_6_month, 3 )\n"
				+ "END,\n"
				+ "NULL,\n"
				+ "t10.last_6_month,\n"
				+ "t10.after_30_day,\n"
				+ "t10.after_60_day,\n"
				+ "t10.after_90_day,\n"
				+ "t10.after_120_day,\n"
				+ "t10.after_150_day,\n"
				+ "t10.after_180_day,\n"
				+ "CASE\n"
					+ "WHEN t10.last_month = 0 THEN 0\n"
					+ "ELSE TRUNC( t10.after_month / t10.last_month, 3 )\n"
				+ "END\n"
			+ "FROM\n"
			+ "(\n"
				+ "SELECT NULL::VARCHAR AS data_type,\n"
					+ "t10.evaluation,\n"
					+ "t10.usage_month,\n"
					+ "t10.last_6_month,\n"
					+ "t10.after_30_day,\n"
					+ "t10.after_60_day,\n"
					+ "t10.after_90_day,\n"
					+ "t10.after_120_day,\n"
					+ "t10.after_150_day,\n"
					+ "t10.after_180_day,\n"
					+ "t10.last_6_month\n"
						+ "+ COALESCE( LAG( t10.last_6_month, 1 ) OVER ( PARTITION BY t10.evaluation ORDER BY t10.usage_month ), 0 )\n"
						+ "+ COALESCE( LAG( t10.last_6_month, 2 ) OVER ( PARTITION BY t10.evaluation ORDER BY t10.usage_month ), 0 ) AS last_month,\n"
					+ "t10.after_60_day\n"
						+ "+ COALESCE( LAG( t10.after_60_day, 1 ) OVER ( PARTITION BY t10.evaluation ORDER BY t10.usage_month ), 0 )\n"
						+ "+ COALESCE( LAG( t10.after_60_day, 2 ) OVER ( PARTITION BY t10.evaluation ORDER BY t10.usage_month ), 0 ) AS after_month\n"
				+ "FROM tmp_repeat_report AS t10\n"
				+ "UNION ALL\n"
				+ "SELECT 'ALL',\n"
					+ "t10.evaluation,\n"
					+ "t10.usage_month,\n"
					+ "t10.all_last_6_month,\n"
					+ "t10.all_after_30_day,\n"
					+ "t10.all_after_60_day,\n"
					+ "t10.all_after_90_day,\n"
					+ "t10.all_after_120_day,\n"
					+ "t10.all_after_150_day,\n"
					+ "t10.all_after_180_day,\n"
					+ "t10.all_last_6_month\n"
						+ "+ COALESCE( LAG( t10.all_last_6_month, 1 ) OVER ( PARTITION BY t10.evaluation ORDER BY t10.usage_month ), 0 )\n"
						+ "+ COALESCE( LAG( t10.all_last_6_month, 2 ) OVER ( PARTITION BY t10.evaluation ORDER BY t10.usage_month ), 0 ),\n"
					+ "t10.all_after_60_day\n"
						+ "+ COALESCE( LAG( t10.all_after_60_day, 1 ) OVER ( PARTITION BY t10.evaluation ORDER BY t10.usage_month ), 0 )\n"
						+ "+ COALESCE( LAG( t10.all_after_60_day, 2 ) OVER ( PARTITION BY t10.evaluation ORDER BY t10.usage_month ), 0 )\n"
				+ "FROM tmp_repeat_report AS t10\n"
			+ ") AS t10\n"
			+ "ORDER BY t10.data_type NULLS FIRST,\n"
				+ "t10.evaluation,\n"
				+ "t10.usage_month\n";

			val rsh = new ArrayListHandler();
			return JDBCUtils.query(sql, rsh);
		}
	}
}
