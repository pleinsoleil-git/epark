package app.nakajo.a00100.job.a00100.load.job.request.load.report;

import java.util.ArrayList;
import java.util.Collection;

import common.jdbc.JDBCUtils;
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
		public void execute() throws Exception {
			insert();
		}

		void insert() throws Exception {
			String sql;
			sql = "INSERT INTO t_repeat_report\n"
				+ "(\n"
					+ "data_type,\n"
					+ "service,\n"
					+ "usage_month,\n"
					+ "evaluation,\n"
					+ "usage_type,\n"
					+ "usage_within_last_2_year_count,\n"
					+ "usage_within_last_1_year_count,\n"
					+ "usage_within_last_6_month_count,\n"
					+ "usage_within_after_30_day_count,\n"
					+ "usage_within_after_60_day_count,\n"
					+ "usage_within_after_90_day_count,\n"
					+ "usage_within_after_120_day_count,\n"
					+ "usage_within_after_150_day_count,\n"
					+ "usage_within_after_180_day_count,\n"
					+ "usage_within_after_1_year_count,\n"
					+ "usage_within_after_2_year_count,\n"
					+ "all_usage_within_last_6_month_count,\n"
					+ "all_usage_within_after_30_day_count,\n"
					+ "all_usage_within_after_60_day_count,\n"
					+ "all_usage_within_after_90_day_count,\n"
					+ "all_usage_within_after_120_day_count,\n"
					+ "all_usage_within_after_150_day_count,\n"
					+ "all_usage_within_after_180_day_count\n"
				+ ")\n"
				+ "SELECT t10.data_type,\n"
					+ "t10.service,\n"
					+ "t10.usage_month,\n"
					+ "t10.evaluation,\n"
					+ "t10.usage_type,\n"
					+ "SUM( t10.usage_within_last_2_year ),\n"
					+ "SUM( t10.usage_within_last_1_year ),\n"
					+ "SUM( t10.usage_within_last_6_month ),\n"
					+ "SUM( t10.usage_within_after_30_day ),\n"
					+ "SUM( t10.usage_within_after_60_day ),\n"
					+ "SUM( t10.usage_within_after_90_day ),\n"
					+ "SUM( t10.usage_within_after_120_day ),\n"
					+ "SUM( t10.usage_within_after_150_day ),\n"
					+ "SUM( t10.usage_within_after_180_day ),\n"
					+ "SUM( t10.usage_within_after_1_year ),\n"
					+ "SUM( t10.usage_within_after_2_year ),\n"
					+ "SUM( t10.all_usage_within_last_6_month ),\n"
					+ "SUM( t10.all_usage_within_after_30_day ),\n"
					+ "SUM( t10.all_usage_within_after_60_day ),\n"
					+ "SUM( t10.all_usage_within_after_90_day ),\n"
					+ "SUM( t10.all_usage_within_after_120_day ),\n"
					+ "SUM( t10.all_usage_within_after_150_day ),\n"
					+ "SUM( t10.all_usage_within_after_180_day )\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT t10.data_type,\n"
						+ "t10.service,\n"
						+ "FIRST_DAY( t10.usage_date ) AS usage_month,\n"
						+ "t10.member_id,\n"
						+ "CASE WHEN t10.evaluation IN ( 'とても満足', 'まあまあ満足' ) THEN '満足'\n"
							 + "WHEN t10.evaluation IN ( 'どちらでもない', 'やや不満足', '大変不満足' ) THEN '不満足'\n"
							 + "WHEN t10.evaluation IS NULL THEN '無回答'\n"
							 + "ELSE t10.evaluation\n"
						+ "END AS evaluation,\n"
						+ "CASE WHEN t10.usage_type LIKE '%問い合わせ%' THEN '問い合わせ'\n"
							 + "ELSE t10.usage_type\n"
						+ "END AS usage_type,\n"
						+ "SIGN( SUM( t10.usage_within_last_2_year ) ) AS usage_within_last_2_year,\n"
						+ "SIGN( SUM( t10.usage_within_last_1_year ) ) AS usage_within_last_1_year,\n"
						+ "SIGN( SUM( t10.usage_within_last_6_month ) ) AS usage_within_last_6_month,\n"
						+ "SIGN( SUM( CASE WHEN t10.usage_within_last_6_month > 0 THEN t10.usage_within_after_30_day ELSE 0 END ) ) AS usage_within_after_30_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.usage_within_last_6_month > 0 THEN t10.usage_within_after_60_day ELSE 0 END ) ) AS usage_within_after_60_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.usage_within_last_6_month > 0 THEN t10.usage_within_after_90_day ELSE 0 END ) ) AS usage_within_after_90_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.usage_within_last_6_month > 0 THEN t10.usage_within_after_120_day ELSE 0 END ) ) AS usage_within_after_120_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.usage_within_last_6_month > 0 THEN t10.usage_within_after_150_day ELSE 0 END ) ) AS usage_within_after_150_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.usage_within_last_6_month > 0 THEN t10.usage_within_after_180_day ELSE 0 END ) ) AS usage_within_after_180_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.usage_within_last_6_month > 0 THEN t10.usage_within_after_1_year ELSE 0 END ) ) AS usage_within_after_1_year,\n"
						+ "SIGN( SUM( CASE WHEN t10.usage_within_last_6_month > 0 THEN t10.usage_within_after_2_year ELSE 0 END ) ) AS usage_within_after_2_year,\n"
						+ "SIGN( SUM( t10.all_usage_within_last_6_month ) ) AS all_usage_within_last_6_month,\n"
						+ "SIGN( SUM( CASE WHEN t10.all_usage_within_last_6_month > 0 THEN t10.all_usage_within_after_30_day ELSE 0 END ) ) AS all_usage_within_after_30_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.all_usage_within_last_6_month > 0 THEN t10.all_usage_within_after_60_day ELSE 0 END ) ) AS all_usage_within_after_60_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.all_usage_within_last_6_month > 0 THEN t10.all_usage_within_after_90_day ELSE 0 END ) ) AS all_usage_within_after_90_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.all_usage_within_last_6_month > 0 THEN t10.all_usage_within_after_120_day ELSE 0 END ) ) AS all_usage_within_after_120_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.all_usage_within_last_6_month > 0 THEN t10.all_usage_within_after_150_day ELSE 0 END ) ) AS all_usage_within_after_150_day,\n"
						+ "SIGN( SUM( CASE WHEN t10.all_usage_within_last_6_month > 0 THEN t10.all_usage_within_after_180_day ELSE 0 END ) ) AS all_usage_within_after_180_day\n"
					+ "FROM t_usage_history AS t10\n"
					+ "GROUP BY 1, 2, 3, 4, 5, 6\n"
				+ ") AS t10\n"
				+ "GROUP BY 1, 2, 3, 4, 5\n";

			val rowNums = JDBCUtils.execute(sql);
			JDBCUtils.commit();

			log.info(String.format("Insert t_repeat_report[count=%s]", rowNums));
		}
	}
}
