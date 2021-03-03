package app.nakajo.a00100.job.a00100.load.job.request.load.entry;

import java.util.ArrayList;
import java.util.Collection;

import app.nakajo.a00100.job.a00100.load.job.request.Request;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Entry {
	static Entry m_instance;
	_Current m_current;

	Entry() {
	}

	public static Entry getInstance() {
		return (m_instance == null ? m_instance = new Entry() : m_instance);
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
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::VARCHAR AS data_type\n"
				+ ")\n"
				+ "INSERT INTO t_usage_history\n"
				+ "(\n"
					+ "data_type,\n"
					+ "service,\n"
					+ "usage_date,\n"
					+ "member_id,\n"
					+ "evaluation,\n"
					+ "usage_type,\n"
					+ "usage_within_last_2_year,\n"
					+ "usage_within_last_1_year,\n"
					+ "usage_within_last_6_month,\n"
					+ "usage_within_after_30_day,\n"
					+ "usage_within_after_60_day,\n"
					+ "usage_within_after_90_day,\n"
					+ "usage_within_after_120_day,\n"
					+ "usage_within_after_150_day,\n"
					+ "usage_within_after_180_day,\n"
					+ "usage_within_after_1_year,\n"
					+ "usage_within_after_2_year,\n"
					+ "all_usage_within_last_6_month,\n"
					+ "all_usage_within_after_30_day,\n"
					+ "all_usage_within_after_60_day,\n"
					+ "all_usage_within_after_90_day,\n"
					+ "all_usage_within_after_120_day,\n"
					+ "all_usage_within_after_150_day,\n"
					+ "all_usage_within_after_180_day\n"
				+ ")\n"
				+ "SELECT t10.data_type,\n"
					+ "t10.service,\n"
					+ "t10.usage_date,\n"
					+ "t10.member_id,\n"
					+ "CASE WHEN t10.evaluation IN ( 'とても満足', 'まあまあ満足' ) THEN '満足'\n"
						 + "WHEN t10.evaluation IN ( 'どちらでもない', 'やや不満足', '大変不満足' ) THEN '不満足'\n"
						 + "WHEN t10.evaluation IS NULL THEN '無回答'\n"
						 + "ELSE t10.evaluation\n"
					+ "END AS evaluation,\n"
					+ "CASE WHEN t10.usage_type LIKE '%問い合わせ%' THEN '問い合わせ'\n"
						 + "ELSE t10.usage_type\n"
					+ "END AS usage_type,\n"
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
						+ "t20.service,\n"
						+ "t20.usage_date::DATE,\n"
						+ "t20.member_id::NUMERIC,\n"
						+ "t20.evaluation,\n"
						+ "t20.usage_type,\n"
						+ "COALESCE( t20.usage_within_last_2_year::NUMERIC, 0 ) AS usage_within_last_2_year,\n"
						+ "COALESCE( t20.usage_within_last_1_year::NUMERIC, 0 ) AS usage_within_last_1_year,\n"
						+ "COALESCE( t20.usage_within_last_6_month::NUMERIC, 0 ) AS usage_within_last_6_month,\n"
						+ "COALESCE( t20.usage_within_after_30_day::NUMERIC, 0 ) AS usage_within_after_30_day,\n"
						+ "COALESCE( t20.usage_within_after_60_day::NUMERIC, 0 ) AS usage_within_after_60_day,\n"
						+ "COALESCE( t20.usage_within_after_90_day::NUMERIC, 0 ) AS usage_within_after_90_day,\n"
						+ "COALESCE( t20.usage_within_after_120_day::NUMERIC, 0 ) AS usage_within_after_120_day,\n"
						+ "COALESCE( t20.usage_within_after_150_day::NUMERIC, 0 ) AS usage_within_after_150_day,\n"
						+ "COALESCE( t20.usage_within_after_180_day::NUMERIC, 0 ) AS usage_within_after_180_day,\n"
						+ "COALESCE( t20.usage_within_after_1_year::NUMERIC, 0 ) AS usage_within_after_1_year,\n"
						+ "COALESCE( t20.usage_within_after_2_year::NUMERIC, 0 ) AS usage_within_after_2_year,\n"
						+ "COALESCE( t20.all_usage_within_last_6_month::NUMERIC, 0 ) AS all_usage_within_last_6_month,\n"
						+ "COALESCE( t20.all_usage_within_after_30_day::NUMERIC, 0 ) AS all_usage_within_after_30_day,\n"
						+ "COALESCE( t20.all_usage_within_after_60_day::NUMERIC, 0 ) AS all_usage_within_after_60_day,\n"
						+ "COALESCE( t20.all_usage_within_after_90_day::NUMERIC, 0 ) AS all_usage_within_after_90_day,\n"
						+ "COALESCE( t20.all_usage_within_after_120_day::NUMERIC, 0 ) AS all_usage_within_after_120_day,\n"
						+ "COALESCE( t20.all_usage_within_after_150_day::NUMERIC, 0 ) AS all_usage_within_after_150_day,\n"
						+ "COALESCE( t20.all_usage_within_after_180_day::NUMERIC, 0 ) AS all_usage_within_after_180_day\n"
					+ "FROM s_params AS t10\n"
					+ "CROSS JOIN i_usage_history AS t20\n"
				+ ") AS t10\n"
				+ "GROUP BY 1, 2, 3, 4, 5, 6\n";

			val rowNums = JDBCUtils.execute(sql, new JDBCParameter() {
				{
					val request = Request.getCurrent();
					add(request.getDataType());
				}
			});
			JDBCUtils.commit();

			log.info(String.format("Insert t_usage_history[count=%s]", rowNums));
		}
	}
}
