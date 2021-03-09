package app.nakajo.a00100.job.a00100.export.job.request.report.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;

import app.nakajo.a00100.job.a00100.export.job.Job;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import common.lang.StringUtils;
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

		void execute() throws Exception {
			output();
		}

		void output() throws Exception {
			val captions = new String[] {
					"すべて",
					"無回答",
					"満足",
					"不満足",
			};
			val evaluations = new String[][] {
					{ "無回答", "満足", "不満足", },
					{ "無回答", },
					{ "満足", },
					{ "不満足", },
			};
			val sheet = Member.getCurrent().getSheet();
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

			for (int i = 0; i < captions.length; i++) {
				for (val rec : query(captions[i], evaluations[i])) {
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
		}

		Collection<Object[]> query(final String caption, final String[] evaluations) throws Exception {
			String sql;
			sql = "WITH RECURSIVE s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "ARRAY\n"
						+ "[\n"
							+ StringUtils.repeat("?::VARCHAR", ",\n", evaluations.length)
						+ "] AS evaluation\n"
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
				+ "SELECT 'ALL',\n"
					+ "CASE\n"
						+ "WHEN ARRAY_LENGTH( t20.evaluation, 1 ) = 1 THEN t20.evaluation[ 1 ]\n"
						+ "ELSE 'すべて'\n"
					+ "END,\n"
					+ "t10.usage_month,\n"
					+ "TRUNC( t30.all_usage_within_after_30_day / t30.all_usage_within_last_6_month, 3 ),\n"
					+ "TRUNC( t30.all_usage_within_after_60_day / t30.all_usage_within_last_6_month, 3 ),\n"
					+ "TRUNC( t30.all_usage_within_after_90_day / t30.all_usage_within_last_6_month, 3 ),\n"
					+ "TRUNC( t30.all_usage_within_after_120_day / t30.all_usage_within_last_6_month, 3 ),\n"
					+ "TRUNC( t30.all_usage_within_after_150_day / t30.all_usage_within_last_6_month, 3 ),\n"
					+ "TRUNC( t30.all_usage_within_after_180_day / t30.all_usage_within_last_6_month, 3 ),\n"
					+ "NULL,\n"
					+ "t30.all_usage_within_last_6_month,\n"
					+ "t30.all_usage_within_after_30_day,\n"
					+ "t30.all_usage_within_after_60_day,\n"
					+ "t30.all_usage_within_after_90_day,\n"
					+ "t30.all_usage_within_after_120_day,\n"
					+ "t30.all_usage_within_after_150_day,\n"
					+ "t30.all_usage_within_after_180_day,\n"
					+ "TRUNC( t30.all_usage_within_after_month / (t30.all_usage_within_last_month, 3 )\n"
				+ "FROM s_usage_month AS t10\n"
				+ "CROSS JOIN s_params AS t20\n"
				+ "LEFT JOIN\n"
				+ "(\n"
					+ "SELECT t10.usage_month,\n"
						+ "t10.all_usage_within_last_6_month,\n"
						+ "t10.all_usage_within_after_30_day,\n"
						+ "t10.all_usage_within_after_60_day,\n"
						+ "t10.all_usage_within_after_90_day,\n"
						+ "t10.all_usage_within_after_120_day,\n"
						+ "t10.all_usage_within_after_150_day,\n"
						+ "t10.all_usage_within_after_180_day,\n"
						+ "t10.all_usage_within_last_6_month\n"
							+ "+ COALESCE( LAG( t10.all_usage_within_last_6_month, 1 ) OVER ( ORDER BY t10.usage_month ), 0 )\n"
							+ "+ COALESCE( LAG( t10.all_usage_within_last_6_month, 2 ) OVER ( ORDER BY t10.usage_month ), 0 ) AS all_usage_within_last_month,\n"
						+ "t10.all_usage_within_after_60_day\n"
							+ "+ COALESCE( LAG( t10.all_usage_within_after_60_day, 1 ) OVER ( ORDER BY t10.usage_month ), 0 )\n"
							+ "+ COALESCE( LAG( t10.all_usage_within_after_60_day, 2 ) OVER ( ORDER BY t10.usage_month ), 0 ) AS all_usage_within_after_month\n"
					+ "FROM\n"
					+ "(\n"
						+ "SELECT t20.usage_month,\n"
							+ "SUM( t20.all_usage_within_last_6_month ) AS all_usage_within_last_6_month,\n"
							+ "SUM( t20.all_usage_within_after_30_day ) AS all_usage_within_after_30_day,\n"
							+ "SUM( t20.all_usage_within_after_60_day ) AS all_usage_within_after_60_day,\n"
							+ "SUM( t20.all_usage_within_after_90_day ) AS all_usage_within_after_90_day,\n"
							+ "SUM( t20.all_usage_within_after_120_day ) AS all_usage_within_after_120_day,\n"
							+ "SUM( t20.all_usage_within_after_150_day ) AS all_usage_within_after_150_day,\n"
							+ "SUM( t20.all_usage_within_after_180_day ) AS all_usage_within_after_180_day\n"
						+ "FROM s_params AS t10\n"
						+ "INNER JOIN tmp_repeat_report AS t20\n"
							+ "ON t20.evaluation = ANY( t10.evaluation )\n"
						+ "GROUP BY 1\n"
					+ ") AS t10\n"
				+ ") AS t30\n"
				+ "ON t30.usage_month = t10.usage_month\n"
				+ "ORDER BY t10.usage_month\n";
/*
			sql = "WITH RECURSIVE s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS caption,\n"
						+ "ARRAY\n"
						+ "[\n"
							+ StringUtils.repeat("?::VARCHAR", ",\n", evaluations.length)
						+ "] AS evaluation\n"
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
				+ "SELECT 'ALL',\n"
					+ "t20.caption,\n"
					+ "t10.usage_month,\n"
					+ "t30.all_usage_within_after_rate[ 1 ],\n"
					+ "t30.all_usage_within_after_rate[ 2 ],\n"
					+ "t30.all_usage_within_after_rate[ 3 ],\n"
					+ "t30.all_usage_within_after_rate[ 4 ],\n"
					+ "t30.all_usage_within_after_rate[ 5 ],\n"
					+ "t30.all_usage_within_after_rate[ 6 ],\n"
					+ "NULL,\n"
					+ "t30.all_usage_within_last[ 1 ],\n"
					+ "t30.all_usage_within_after[ 1 ],\n"
					+ "t30.all_usage_within_after[ 2 ],\n"
					+ "t30.all_usage_within_after[ 3 ],\n"
					+ "t30.all_usage_within_after[ 4 ],\n"
					+ "t30.all_usage_within_after[ 5 ],\n"
					+ "t30.all_usage_within_after[ 6 ],\n"
					+ "CASE WHEN t30.all_usage_within_last_2_month = 0 THEN 0\n"
						 + "ELSE TRUNC( t30.all_usage_within_after_2_month / t30.all_usage_within_last_2_month, 3 )\n"
					+ "END\n"
				+ "FROM s_usage_month AS t10\n"
				+ "CROSS JOIN s_params AS t20\n"
				+ "LEFT JOIN\n"
				+ "(\n"
					+ "SELECT t10.usage_month,\n"
						+ "ARRAY\n"
						+ "[\n"
							+ "CASE WHEN t10.all_usage_within_last[ 1 ] = 0 THEN 0\n"
								 + "ELSE TRUNC( t10.all_usage_within_after[ 1 ] / t10.all_usage_within_last[ 1 ], 3 )\n"
							+ "END,\n"
							+ "CASE WHEN t10.all_usage_within_last[ 1 ] = 0 THEN 0\n"
								 + "ELSE TRUNC( t10.all_usage_within_after[ 2 ] / t10.all_usage_within_last[ 1 ], 3 )\n"
							+ "END,\n"
							+ "CASE WHEN t10.all_usage_within_last[ 1 ] = 0 THEN 0\n"
								 + "ELSE TRUNC( t10.all_usage_within_after[ 3 ] / t10.all_usage_within_last[ 1 ], 3 )\n"
							+ "END,\n"
							+ "CASE WHEN t10.all_usage_within_last[ 1 ] = 0 THEN 0\n"
								 + "ELSE TRUNC( t10.all_usage_within_after[ 4 ] / t10.all_usage_within_last[ 1 ], 3 )\n"
							+ "END,\n"
							+ "CASE WHEN t10.all_usage_within_last[ 1 ] = 0 THEN 0\n"
								 + "ELSE TRUNC( t10.all_usage_within_after[ 5 ] / t10.all_usage_within_last[ 1 ], 3 )\n"
							+ "END,\n"
							+ "CASE WHEN t10.all_usage_within_last[ 1 ] = 0 THEN 0\n"
								 + "ELSE TRUNC( t10.all_usage_within_after[ 6 ] / t10.all_usage_within_last[ 1 ], 3 )\n"
							+ "END\n"
						+ "] AS all_usage_within_after_rate,\n"
						+ "t10.all_usage_within_last,\n"
						+ "t10.all_usage_within_after,\n"
						+ "t10.all_usage_within_after[ 2 ]\n"
							+ "+ COALESCE( LAG( t10.all_usage_within_after[ 2 ], 1 ) OVER ( ORDER BY t10.usage_month ), 0 )\n"
							+ "+ COALESCE( LAG( t10.all_usage_within_after[ 2 ], 2 ) OVER ( ORDER BY t10.usage_month ), 0 ) AS all_usage_within_after_2_month,\n"
						+ "t10.all_usage_within_last[ 1 ]\n"
							+ "+ COALESCE( LAG( t10.all_usage_within_last[ 1 ], 1 ) OVER ( ORDER BY t10.usage_month ), 0 )\n"
							+ "+ COALESCE( LAG( t10.all_usage_within_last[ 1 ], 2 ) OVER ( ORDER BY t10.usage_month ), 0 ) AS all_usage_within_last_2_month\n"
					+ "FROM\n"
					+ "(\n"
						+ "SELECT t20.usage_month,\n"
							+ "ARRAY\n"
							+ "[\n"
								+ "SUM( t20.all_usage_within_last_6_month )\n"
							+ "] AS all_usage_within_last,\n"
							+ "ARRAY\n"
							+ "[\n"
								+ "SUM( t20.all_usage_within_after_30_day ),\n"
								+ "SUM( t20.all_usage_within_after_60_day ),\n"
								+ "SUM( t20.all_usage_within_after_90_day ),\n"
								+ "SUM( t20.all_usage_within_after_120_day ),\n"
								+ "SUM( t20.all_usage_within_after_150_day ),\n"
								+ "SUM( t20.all_usage_within_after_180_day )\n"
							+ "] AS all_usage_within_after\n"
						+ "FROM s_params AS t10\n"
						+ "INNER JOIN tmp_repeat_report AS t20\n"
							+ "ON t20.evaluation = ANY( t10.evaluation )\n"
						+ "GROUP BY 1\n"
					+ ") AS t10\n"
				+ ") AS t30\n"
				+ "ON t30.usage_month = t10.usage_month\n"
				+ "ORDER BY t10.usage_month\n";
*/

			val rsh = new ArrayListHandler();
			return JDBCUtils.query(sql, rsh,
					new JDBCParameter() {
						{
							val job = Job.getCurrent();
							add(job.getId());

							for (val x : evaluations) {
								add(x);
							}
						}
					});
		}
	}
}
