package app.nakajo.a00100.job.a00100.export.job.request.report.member;

import java.util.ArrayList;
import java.util.Collection;

import app.nakajo.a00100.job.a00100.export.job.Job;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		public void execute() throws Exception {
			log.info("会員情報出力");
			delete();
			insert();
			evaluation();
		}

		void evaluation() throws Exception {
			Evaluation.getInstance().execute();
		}

		void delete() throws Exception {
			for (val x : new String[] {
					"tmp_repeat_report",
			}) {
				log.info(String.format("Delete %s", x));
				JDBCUtils.execute(String.format("TRUNCATE TABLE %s CASCADE", x));
				JDBCUtils.commit();
			}
		}

		void insert() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id\n"
				+ ")\n"
				+ "INSERT INTO tmp_repeat_report\n"
				+ "(\n"
					+ "evaluation,\n"
					+ "channel,\n"
					+ "usage_month,\n"
					+ "last_6_month,\n"
					+ "after_30_day,\n"
					+ "after_60_day,\n"
					+ "after_90_day,\n"
					+ "after_120_day,\n"
					+ "after_150_day,\n"
					+ "after_180_day,\n"
					+ "all_last_6_month,\n"
					+ "all_after_30_day,\n"
					+ "all_after_60_day,\n"
					+ "all_after_90_day,\n"
					+ "all_after_120_day,\n"
					+ "all_after_150_day,\n"
					+ "all_after_180_day\n"
				+ ")\n"
				+ "SELECT t10.evaluation,\n"
					+ "t10.channel,\n"
					+ "t10.usage_month,\n"
					+ "t10.last_6_month,\n"
					+ "t10.after_30_day,\n"
					+ "t10.after_60_day,\n"
					+ "t10.after_90_day,\n"
					+ "t10.after_120_day,\n"
					+ "t10.after_150_day,\n"
					+ "t10.after_180_day,\n"
					+ "t10.all_last_6_month,\n"
					+ "t10.all_after_30_day,\n"
					+ "t10.all_after_60_day,\n"
					+ "t10.all_after_90_day,\n"
					+ "t10.all_after_120_day,\n"
					+ "t10.all_after_150_day,\n"
					+ "t10.all_after_180_day\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT t10.evaluation,\n"
						+ "t10.channel,\n"
						+ "t10.usage_month,\n"
						+ "SUM( t10.last_6_month ) AS last_6_month,\n"
						+ "SUM( CASE WHEN t10.last_6_month > 0 THEN t10.after_30_day ELSE 0 END ) AS after_30_day,\n"
						+ "SUM( CASE WHEN t10.last_6_month > 0 THEN t10.after_60_day ELSE 0 END ) AS after_60_day,\n"
						+ "SUM( CASE WHEN t10.last_6_month > 0 THEN t10.after_90_day ELSE 0 END ) AS after_90_day,\n"
						+ "SUM( CASE WHEN t10.last_6_month > 0 THEN t10.after_120_day ELSE 0 END ) AS after_120_day,\n"
						+ "SUM( CASE WHEN t10.last_6_month > 0 THEN t10.after_150_day ELSE 0 END ) AS after_150_day,\n"
						+ "SUM( CASE WHEN t10.last_6_month > 0 THEN t10.after_180_day ELSE 0 END ) AS after_180_day,\n"
						+ "SUM( t10.all_last_6_month ) AS all_last_6_month,\n"
						+ "SUM( CASE WHEN t10.all_last_6_month > 0 THEN t10.all_after_30_day ELSE 0 END ) AS all_after_30_day,\n"
						+ "SUM( CASE WHEN t10.all_last_6_month > 0 THEN t10.all_after_60_day ELSE 0 END ) AS all_after_60_day,\n"
						+ "SUM( CASE WHEN t10.all_last_6_month > 0 THEN t10.all_after_90_day ELSE 0 END ) AS all_after_90_day,\n"
						+ "SUM( CASE WHEN t10.all_last_6_month > 0 THEN t10.all_after_120_day ELSE 0 END ) AS all_after_120_day,\n"
						+ "SUM( CASE WHEN t10.all_last_6_month > 0 THEN t10.all_after_150_day ELSE 0 END ) AS all_after_150_day,\n"
						+ "SUM( CASE WHEN t10.all_last_6_month > 0 THEN t10.all_after_180_day ELSE 0 END ) AS all_after_180_day\n"
					+ "FROM\n"
					+ "(\n"
						+ "SELECT\n"
							+ "CASE\n"
								+ "WHEN t20.evaluation IN ( 'とても満足', 'まあまあ満足' ) THEN '満足'\n"
								+ "WHEN t20.evaluation IN ( 'どちらでもない', 'やや不満足', '大変不満足' ) THEN '不満足'\n"
								+ "WHEN t20.evaluation IS NULL THEN '無回答'\n"
								+ "ELSE t20.evaluation\n"
							+ "END AS evaluation,\n"
							+ "t20.channel,\n"
							+ "FIRST_DAY( t20.usage_date ) AS usage_month,\n"
							+ "t20.member_id,\n"
							+ "SIGN( SUM( t20.usage_within_last_6_month ) ) AS last_6_month,\n"
							+ "SIGN( SUM( t20.usage_within_after_30_day ) ) AS after_30_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_60_day ) ) AS after_60_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_90_day ) ) AS after_90_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_120_day ) ) AS after_120_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_150_day ) ) AS after_150_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_180_day ) ) AS after_180_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_last_6_month ) ) AS all_last_6_month,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_30_day ) ) AS all_after_30_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_60_day ) ) AS all_after_60_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_90_day ) ) AS all_after_90_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_120_day ) ) AS all_after_120_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_150_day ) ) AS all_after_150_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_180_day ) ) AS all_after_180_day\n"
						+ "FROM s_params AS t10\n"
						+ "INNER JOIN j_export_job AS j10\n"
							+ "ON j10.id = t10.job_id\n"
						+ "INNER JOIN t_usage_history AS t20\n"
							+ "ON t20.data_type IN\n"
								+ "(\n"
									+ "'VOC'::VARCHAR,\n"
									+ "'MAIL'::VARCHAR\n"
								+ ")\n"
							+ "AND t20.service = 'EPARK会員情報'::VARCHAR\n"
							+ "AND t20.usage_date BETWEEN j10.usage_date[ 1 ] AND j10.usage_date[ 2 ]\n"
						+ "GROUP BY 1, 2, 3, 4\n"
					+ ") AS t10\n"
					+ "GROUP BY 1, 2, 3\n"
					+ "UNION ALL\n"
					+ "SELECT t10.channel,\n"
						+ "t10.channel,\n"
						+ "t10.usage_month,\n"
						+ "COUNT( t10.last_6_month ) AS last_6_month,\n"
						+ "SUM( t10.after_30_day ) AS after_30_day,\n"
						+ "SUM( t10.after_60_day ) AS after_60_day,\n"
						+ "SUM( t10.after_90_day ) AS after_90_day,\n"
						+ "SUM( t10.after_120_day ) AS after_120_day,\n"
						+ "SUM( t10.after_150_day ) AS after_150_day,\n"
						+ "SUM( t10.after_180_day ) AS after_180_day,\n"
						+ "COUNT( t10.all_last_6_month ) AS all_last_6_month,\n"
						+ "SUM( t10.all_after_30_day ) AS all_after_30_day,\n"
						+ "SUM( t10.all_after_60_day ) AS all_after_60_day,\n"
						+ "SUM( t10.all_after_90_day ) AS all_after_90_day,\n"
						+ "SUM( t10.all_after_120_day ) AS all_after_120_day,\n"
						+ "SUM( t10.all_after_150_day ) AS all_after_150_day,\n"
						+ "SUM( t10.all_after_180_day ) AS all_after_180_day\n"
					+ "FROM\n"
					+ "(\n"
						+ "SELECT t20.channel,\n"
							+ "FIRST_DAY( t20.usage_date ) AS usage_month,\n"
							+ "t20.member_id,\n"
							+ "SIGN( SUM( t20.usage_within_last_6_month ) ) AS last_6_month,\n"
							+ "SIGN( SUM( t20.usage_within_after_30_day ) ) AS after_30_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_60_day ) ) AS after_60_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_90_day ) ) AS after_90_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_120_day ) ) AS after_120_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_150_day ) ) AS after_150_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_180_day ) ) AS after_180_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_last_6_month ) ) AS all_last_6_month,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_30_day ) ) AS all_after_30_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_60_day ) ) AS all_after_60_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_90_day ) ) AS all_after_90_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_120_day ) ) AS all_after_120_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_150_day ) ) AS all_after_150_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_180_day ) ) AS all_after_180_day\n"
						+ "FROM s_params AS t10\n"
						+ "INNER JOIN j_export_job AS j10\n"
							+ "ON j10.id = t10.job_id\n"
						+ "INNER JOIN t_usage_history AS t20\n"
							+ "ON t20.data_type IN\n"
								+ "(\n"
									+ "'SILENT'::VARCHAR\n"
								+ ")\n"
							+ "AND t20.service = 'EPARK会員情報'::VARCHAR\n"
							+ "AND t20.usage_date BETWEEN j10.usage_date[ 1 ] AND j10.usage_date[ 2 ]\n"
						+ "GROUP BY 1, 2, 3\n"
					+ ") AS t10\n"
					+ "GROUP BY 1, 2, 3\n"
				+ ") AS t10\n";

			val rowNums = JDBCUtils.execute(sql,
					new JDBCParameter() {
						{
							val job = Job.getCurrent();
							add(job.getId());
						}
					});
			JDBCUtils.commit();
			log.info(String.format("Insert tmp_repeat_report[count=%s]", rowNums));
		}
	}
}
