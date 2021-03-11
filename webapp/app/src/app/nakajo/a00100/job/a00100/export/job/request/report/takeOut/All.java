package app.nakajo.a00100.job.a00100.export.job.request.report.takeOut;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;

import app.nakajo.a00100.job.a00100.export.job.Job;
import app.nakajo.a00100.job.a00100.export.job.request.report.Report;
import app.nakajo.a00100.job.a00100.export.job.request.report.ReportType;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import common.poi.CellUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class All {
	static All m_instance;
	_Current m_current;

	All() {
	}

	static All getInstance() {
		return (m_instance == null ? m_instance = new All() : m_instance);
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
				val book = Report.getCurrent().getWorkbook(ReportType.ALL);
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
			sql = "WITH RECURSIVE s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id\n"
				+ "),\n"
				+ "s_usage_month AS\n"
				+ "(\n"
					+ "SELECT FIRST_DAY( j10.usage_date[ 1 ] ) AS usage_month,\n"
						+ "LAST_DAY( j10.usage_date[ 2 ] ) AS usage_month_to\n"
					+ "FROM s_params AS t10\n"
					+ "INNER JOIN j_export_job AS j10\n"
						+ "ON j10.id = t10.job_id\n"
					+ "UNION ALL\n"
					+ "SELECT ADD_MONTHS( t10.usage_month, 1 ),\n"
						+ "t10.usage_month_to\n"
					+ "FROM s_usage_month AS t10\n"
					+ "WHERE ADD_MONTHS( t10.usage_month, 1 ) <= t10.usage_month_to\n"
				+ ")\n"
				+ "SELECT t10.data_type,\n"
					+ "t10.usage_month,\n"
					+ "t10.repeat_rate[ 1 ],\n"
					+ "t10.repeat_rate[ 2 ],\n"
					+ "t10.repeat_rate[ 3 ],\n"
					+ "t10.repeat_rate[ 4 ],\n"
					+ "t10.repeat_rate[ 5 ],\n"
					+ "t10.repeat_rate[ 5 ] - t10.repeat_rate[ 1 ],\n"
					+ "t10.repeat_rate[ 3 ] - t10.repeat_rate[ 1 ]\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT t10.data_type,\n"
						+ "t10.usage_month,\n"
						+ "ARRAY\n"
						+ "[\n"
							+ "CASE\n"
								+ "WHEN t10.last_6_month[ 1 ] = 0 THEN 0\n"
								+ "ELSE TRUNC( t10.after_60_day[ 1 ] / t10.last_6_month[ 1 ], 3 )\n"
							+ "END,\n"
							+ "CASE\n"
								+ "WHEN t10.last_6_month[ 2 ] = 0 THEN 0\n"
								+ "ELSE TRUNC( t10.after_60_day[ 2 ] / t10.last_6_month[ 2 ], 3 )\n"
							+ "END,\n"
							+ "CASE\n"
								+ "WHEN t10.last_6_month[ 3 ] = 0 THEN 0\n"
								+ "ELSE TRUNC( t10.after_60_day[ 3 ] / t10.last_6_month[ 3 ], 3 )\n"
							+ "END,\n"
							+ "CASE\n"
								+ "WHEN t10.last_6_month[ 4 ] = 0 THEN 0\n"
								+ "ELSE TRUNC( t10.after_60_day[ 4 ] / t10.last_6_month[ 4 ], 3 )\n"
							+ "END,\n"
							+ "CASE\n"
								+ "WHEN t10.last_6_month[ 5 ] = 0 THEN 0\n"
								+ "ELSE TRUNC( t10.after_60_day[ 5 ] / t10.last_6_month[ 5 ], 3 )\n"
							+ "END\n"
						+ "] AS repeat_rate\n"
					+ "FROM\n"
					+ "(\n"
						+ "SELECT t10.data_type,\n"
							+ "t10.usage_month,\n"
							+ "ARRAY\n"
							+ "[\n"
								+ "SUM( CASE WHEN t10.evaluation = 'サイレント' THEN t10.last_6_month ELSE 0 END ),\n"
								+ "SUM( CASE WHEN t10.evaluation = '無回答' THEN t10.last_6_month ELSE 0 END ),\n"
								+ "SUM( CASE WHEN t10.evaluation = '満足' THEN t10.last_6_month ELSE 0 END ),\n"
								+ "SUM( CASE WHEN t10.evaluation = '不満足' THEN t10.last_6_month ELSE 0 END ),\n"
								+ "SUM( CASE WHEN t10.evaluation != 'サイレント' THEN t10.last_6_month ELSE 0 END )\n"
							+ "] AS last_6_month,\n"
							+ "ARRAY\n"
							+ "[\n"
								+ "SUM( CASE WHEN t10.evaluation = 'サイレント' THEN t10.after_60_day ELSE 0 END ),\n"
								+ "SUM( CASE WHEN t10.evaluation = '無回答' THEN t10.after_60_day ELSE 0 END ),\n"
								+ "SUM( CASE WHEN t10.evaluation = '満足' THEN t10.after_60_day ELSE 0 END ),\n"
								+ "SUM( CASE WHEN t10.evaluation = '不満足' THEN t10.after_60_day ELSE 0 END ),\n"
								+ "SUM( CASE WHEN t10.evaluation != 'サイレント' THEN t10.after_60_day ELSE 0 END )\n"
							+ "] AS after_60_day\n"
						+ "FROM\n"
						+ "(\n"
							+ "SELECT NULL::VARCHAR AS data_type,\n"
								+ "t20.evaluation,\n"
								+ "t10.usage_month,\n"
								+ "t20.last_6_month\n"
									+ "+ COALESCE( LAG( t20.last_6_month, 1 ) OVER ( PARTITION BY t20.evaluation ORDER BY t20.usage_month ), 0 )\n"
									+ "+ COALESCE( LAG( t20.last_6_month, 2 ) OVER ( PARTITION BY t20.evaluation ORDER BY t20.usage_month ), 0 ) AS last_6_month,\n"
								+ "t20.after_60_day\n"
									+ "+ COALESCE( LAG( t20.after_60_day, 1 ) OVER ( PARTITION BY t20.evaluation ORDER BY t20.usage_month ), 0 )\n"
									+ "+ COALESCE( LAG( t20.after_60_day, 2 ) OVER ( PARTITION BY t20.evaluation ORDER BY t20.usage_month ), 0 ) AS after_60_day\n"
							+ "FROM s_usage_month AS t10\n"
							+ "LEFT JOIN tmp_repeat_report AS t20\n"
								+ "ON t20.usage_month = t10.usage_month\n"
							+ "UNION ALL\n"
							+ "SELECT 'ALL',\n"
								+ "t20.evaluation,\n"
								+ "t10.usage_month,\n"
								+ "t20.all_last_6_month\n"
									+ "+ COALESCE( LAG( t20.all_last_6_month, 1 ) OVER ( PARTITION BY t20.evaluation ORDER BY t20.usage_month ), 0 )\n"
									+ "+ COALESCE( LAG( t20.all_last_6_month, 2 ) OVER ( PARTITION BY t20.evaluation ORDER BY t20.usage_month ), 0 ),\n"
								+ "t20.all_after_60_day\n"
									+ "+ COALESCE( LAG( t20.all_after_60_day, 1 ) OVER ( PARTITION BY t20.evaluation ORDER BY t20.usage_month ), 0 )\n"
									+ "+ COALESCE( LAG( t20.all_after_60_day, 2 ) OVER ( PARTITION BY t20.evaluation ORDER BY t20.usage_month ), 0 )\n"
							+ "FROM s_usage_month AS t10\n"
							+ "LEFT JOIN tmp_repeat_report AS t20\n"
								+ "ON t20.usage_month = t10.usage_month\n"
						+ ") AS t10\n"
						+ "GROUP BY 1, 2\n"
					+ ") AS t10\n"
				+ ") AS t10\n"
				+ "ORDER BY t10.data_type NULLS FIRST,\n"
					+ "t10.usage_month\n";

			val rsh = new ArrayListHandler();
			return JDBCUtils.query(sql, rsh,
					new JDBCParameter() {
						{
							val job = Job.getCurrent();
							add(job.getId());
						}
					});
		}
	}
}
