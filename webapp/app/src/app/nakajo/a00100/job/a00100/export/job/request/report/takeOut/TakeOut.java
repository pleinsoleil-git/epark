package app.nakajo.a00100.job.a00100.export.job.request.report.takeOut;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Sheet;

import app.nakajo.a00100.job.a00100.export.job.Job;
import app.nakajo.a00100.job.a00100.export.job.request.report.Report;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class TakeOut {
	static TakeOut m_instance;
	_Current m_current;

	TakeOut() {
	}

	public static TakeOut getInstance() {
		return (m_instance == null ? m_instance = new TakeOut() : m_instance);
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
				m_sheet = book.getSheet("テイクアウト");
			}

			return m_sheet;
		}

		public void execute() throws Exception {
			delete();
			insert();
			all();
		}

		void all() throws Exception {
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
					+ "usage_within_last_6_month,\n"
					+ "usage_within_after_30_day,\n"
					+ "usage_within_after_60_day,\n"
					+ "usage_within_after_90_day,\n"
					+ "usage_within_after_120_day,\n"
					+ "usage_within_after_150_day,\n"
					+ "usage_within_after_180_day,\n"
					+ "all_usage_within_last_6_month,\n"
					+ "all_usage_within_after_30_day,\n"
					+ "all_usage_within_after_60_day,\n"
					+ "all_usage_within_after_90_day,\n"
					+ "all_usage_within_after_120_day,\n"
					+ "all_usage_within_after_150_day,\n"
					+ "all_usage_within_after_180_day\n"
				+ ")\n"
				+ "SELECT t10.evaluation,\n"
					+ "t10.channel,\n"
					+ "t10.usage_month,\n"
					+ "t10.usage_within_last_6_month,\n"
					+ "t10.usage_within_after_30_day,\n"
					+ "t10.usage_within_after_60_day,\n"
					+ "t10.usage_within_after_90_day,\n"
					+ "t10.usage_within_after_120_day,\n"
					+ "t10.usage_within_after_150_day,\n"
					+ "t10.usage_within_after_180_day,\n"
					+ "t10.all_usage_within_last_6_month,\n"
					+ "t10.all_usage_within_after_30_day,\n"
					+ "t10.all_usage_within_after_60_day,\n"
					+ "t10.all_usage_within_after_90_day,\n"
					+ "t10.all_usage_within_after_120_day,\n"
					+ "t10.all_usage_within_after_150_day,\n"
					+ "t10.all_usage_within_after_180_day\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT t10.evaluation,\n"
						+ "t10.channel,\n"
						+ "t10.usage_month,\n"
						+ "SUM( t10.usage_within_last_6_month ) AS usage_within_last_6_month,\n"
						+ "SUM( t10.usage_within_after_30_day ) AS usage_within_after_30_day,\n"
						+ "SUM( t10.usage_within_after_60_day ) AS usage_within_after_60_day,\n"
						+ "SUM( t10.usage_within_after_90_day ) AS usage_within_after_90_day,\n"
						+ "SUM( t10.usage_within_after_120_day ) AS usage_within_after_120_day,\n"
						+ "SUM( t10.usage_within_after_150_day ) AS usage_within_after_150_day,\n"
						+ "SUM( t10.usage_within_after_180_day ) AS usage_within_after_180_day,\n"
						+ "SUM( t10.all_usage_within_last_6_month ) AS all_usage_within_last_6_month,\n"
						+ "SUM( t10.all_usage_within_after_30_day ) AS all_usage_within_after_30_day,\n"
						+ "SUM( t10.all_usage_within_after_60_day ) AS all_usage_within_after_60_day,\n"
						+ "SUM( t10.all_usage_within_after_90_day ) AS all_usage_within_after_90_day,\n"
						+ "SUM( t10.all_usage_within_after_120_day ) AS all_usage_within_after_120_day,\n"
						+ "SUM( t10.all_usage_within_after_150_day ) AS all_usage_within_after_150_day,\n"
						+ "SUM( t10.all_usage_within_after_180_day ) AS all_usage_within_after_180_day\n"
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
							+ "SIGN( SUM( t20.usage_within_last_6_month ) ) AS usage_within_last_6_month,\n"
							+ "SIGN( SUM( t20.usage_within_after_30_day ) ) AS usage_within_after_30_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_60_day ) ) AS usage_within_after_60_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_90_day ) ) AS usage_within_after_90_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_120_day ) ) AS usage_within_after_120_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_150_day ) ) AS usage_within_after_150_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_180_day ) ) AS usage_within_after_180_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_last_6_month ) ) AS all_usage_within_last_6_month,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_30_day ) ) AS all_usage_within_after_30_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_60_day ) ) AS all_usage_within_after_60_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_90_day ) ) AS all_usage_within_after_90_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_120_day ) ) AS all_usage_within_after_120_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_150_day ) ) AS all_usage_within_after_150_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_180_day ) ) AS all_usage_within_after_180_day\n"
						+ "FROM s_params AS t10\n"
						+ "INNER JOIN j_export_job AS j10\n"
							+ "ON j10.id = t10.job_id\n"
						+ "INNER JOIN t_usage_history AS t20\n"
							+ "ON t20.data_type IN\n"
								+ "(\n"
									+ "'VOC'::VARCHAR,\n"
									+ "'MAIL'::VARCHAR\n"
								+ ")\n"
							+ "AND t20.service = 'EPARKテイクアウト'::VARCHAR\n"
							+ "AND t20.usage_date BETWEEN j10.usage_date[ 1 ] AND j10.usage_date[ 2 ]\n"
						+ "GROUP BY 1, 2, 3, 4\n"
					+ ") AS t10\n"
					+ "WHERE t10.usage_within_last_6_month > 0\n"
					+ "GROUP BY 1, 2, 3\n"
					+ "UNION ALL\n"
					+ "SELECT t10.channel,\n"
						+ "t10.channel,\n"
						+ "t10.usage_month,\n"
						+ "COUNT( t10.usage_within_last_6_month ) AS usage_within_last_6_month,\n"
						+ "SUM( t10.usage_within_after_30_day ) AS usage_within_after_30_day,\n"
						+ "SUM( t10.usage_within_after_60_day ) AS usage_within_after_60_day,\n"
						+ "SUM( t10.usage_within_after_90_day ) AS usage_within_after_90_day,\n"
						+ "SUM( t10.usage_within_after_120_day ) AS usage_within_after_120_day,\n"
						+ "SUM( t10.usage_within_after_150_day ) AS usage_within_after_150_day,\n"
						+ "SUM( t10.usage_within_after_180_day ) AS usage_within_after_180_day,\n"
						+ "COUNT( t10.all_usage_within_last_6_month ) AS all_usage_within_last_6_month,\n"
						+ "SUM( t10.all_usage_within_after_30_day ) AS all_usage_within_after_30_day,\n"
						+ "SUM( t10.all_usage_within_after_60_day ) AS all_usage_within_after_60_day,\n"
						+ "SUM( t10.all_usage_within_after_90_day ) AS all_usage_within_after_90_day,\n"
						+ "SUM( t10.all_usage_within_after_120_day ) AS all_usage_within_after_120_day,\n"
						+ "SUM( t10.all_usage_within_after_150_day ) AS all_usage_within_after_150_day,\n"
						+ "SUM( t10.all_usage_within_after_180_day ) AS all_usage_within_after_180_day\n"
					+ "FROM\n"
					+ "(\n"
						+ "SELECT t20.channel,\n"
							+ "FIRST_DAY( t20.usage_date ) AS usage_month,\n"
							+ "t20.member_id,\n"
							+ "SIGN( SUM( t20.usage_within_last_6_month ) ) AS usage_within_last_6_month,\n"
							+ "SIGN( SUM( t20.usage_within_after_30_day ) ) AS usage_within_after_30_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_60_day ) ) AS usage_within_after_60_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_90_day ) ) AS usage_within_after_90_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_120_day ) ) AS usage_within_after_120_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_150_day ) ) AS usage_within_after_150_day,\n"
							+ "SIGN( SUM( t20.usage_within_after_180_day ) ) AS usage_within_after_180_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_last_6_month ) ) AS all_usage_within_last_6_month,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_30_day ) ) AS all_usage_within_after_30_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_60_day ) ) AS all_usage_within_after_60_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_90_day ) ) AS all_usage_within_after_90_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_120_day ) ) AS all_usage_within_after_120_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_150_day ) ) AS all_usage_within_after_150_day,\n"
							+ "SIGN( SUM( t20.all_usage_within_after_180_day ) ) AS all_usage_within_after_180_day\n"
						+ "FROM s_params AS t10\n"
						+ "INNER JOIN j_export_job AS j10\n"
							+ "ON j10.id = t10.job_id\n"
						+ "INNER JOIN t_usage_history AS t20\n"
							+ "ON t20.data_type IN\n"
								+ "(\n"
									+ "'SILENT'::VARCHAR\n"
								+ ")\n"
							+ "AND t20.service = 'テイクアウト'::VARCHAR\n"
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
