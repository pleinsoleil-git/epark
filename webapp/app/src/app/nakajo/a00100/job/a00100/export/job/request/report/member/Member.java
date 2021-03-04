package app.nakajo.a00100.job.a00100.export.job.request.report.member;

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
			delete();
			insert();
			all();
		}

		void all() throws Exception {
			All.getInstance().execute();
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
			sql = "WITH RECURSIVE s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS service\n"
				+ ")\n"
				+ "INSERT INTO tmp_repeat_report\n"
				+ "(\n"
					+ "data_type,\n"
					+ "usage_month,\n"
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
					+ "SELECT t20.data_type,\n"
						+ "t20.evaluation,\n"
						+ "t20.usage_type,\n"
						+ "FIRST_DAY( t20.usage_date ) AS usage_month,\n"
						+ "t20.member_id,\n"
						+ "SIGN( SUM( t20.usage_within_last_2_year ) ) AS usage_within_last_2_year,\n"
						+ "SIGN( SUM( t20.usage_within_last_1_year ) ) AS usage_within_last_1_year,\n"
						+ "SIGN( SUM( t20.usage_within_last_6_month ) ) AS usage_within_last_6_month,\n"
						+ "SIGN( SUM( CASE WHEN t20.usage_within_last_6_month > 0 THEN t20.usage_within_after_30_day ELSE 0 END ) ) AS usage_within_after_30_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.usage_within_last_6_month > 0 THEN t20.usage_within_after_60_day ELSE 0 END ) ) AS usage_within_after_60_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.usage_within_last_6_month > 0 THEN t20.usage_within_after_90_day ELSE 0 END ) ) AS usage_within_after_90_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.usage_within_last_6_month > 0 THEN t20.usage_within_after_120_day ELSE 0 END ) ) AS usage_within_after_120_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.usage_within_last_6_month > 0 THEN t20.usage_within_after_150_day ELSE 0 END ) ) AS usage_within_after_150_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.usage_within_last_6_month > 0 THEN t20.usage_within_after_180_day ELSE 0 END ) ) AS usage_within_after_180_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.usage_within_last_6_month > 0 THEN t20.usage_within_after_1_year ELSE 0 END ) ) AS usage_within_after_1_year,\n"
						+ "SIGN( SUM( CASE WHEN t20.usage_within_last_6_month > 0 THEN t20.usage_within_after_2_year ELSE 0 END ) ) AS usage_within_after_2_year,\n"
						+ "SIGN( SUM( t20.all_usage_within_last_6_month ) ) AS all_usage_within_last_6_month,\n"
						+ "SIGN( SUM( CASE WHEN t20.all_usage_within_last_6_month > 0 THEN t20.all_usage_within_after_30_day ELSE 0 END ) ) AS all_usage_within_after_30_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.all_usage_within_last_6_month > 0 THEN t20.all_usage_within_after_60_day ELSE 0 END ) ) AS all_usage_within_after_60_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.all_usage_within_last_6_month > 0 THEN t20.all_usage_within_after_90_day ELSE 0 END ) ) AS all_usage_within_after_90_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.all_usage_within_last_6_month > 0 THEN t20.all_usage_within_after_120_day ELSE 0 END ) ) AS all_usage_within_after_120_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.all_usage_within_last_6_month > 0 THEN t20.all_usage_within_after_150_day ELSE 0 END ) ) AS all_usage_within_after_150_day,\n"
						+ "SIGN( SUM( CASE WHEN t20.all_usage_within_last_6_month > 0 THEN t20.all_usage_within_after_180_day ELSE 0 END ) ) AS all_usage_within_after_180_day\n"
					+ "FROM s_params AS t10\n"
					+ "INNER JOIN j_export_job AS j10\n"
						+ "ON j10.id = t10.job_id\n"
					+ "INNER JOIN t_usage_history AS t20\n"
						+ "ON t20.service = t10.service\n"
						+ "AND t20.usage_date BETWEEN j10.usage_date[ 1 ] AND j10.usage_date[ 2 ]\n"
					+ "GROUP BY 1, 2, 3, 4, 5\n"
				+ ") AS t10\n"
				+ "GROUP BY 1, 2, 3, 4\n";

			val rowNums = JDBCUtils.execute(sql,
					new JDBCParameter() {
						{
							val job = Job.getCurrent();
							add(job.getId());
							add("EPARK会員情報");
						}
					});
			JDBCUtils.commit();
			log.info(String.format("Insert tmp_repeat_report[count=%s]", rowNums));
		}
	}
}
