package app.nakajo.a00100.job.a00100.load.job.request.load.request;

import java.util.ArrayList;
import java.util.Collection;

import app.nakajo.a00100.job.a00100.load.job.request.load.usage.entry.Entry;
import app.nakajo.a00100.job.a00100.load.job.request.load.usage.reader.Reader;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Request {
	static Request m_instance;
	_Current m_current;

	Request() {
	}

	public static Request getInstance() {
		return (m_instance == null ? m_instance = new Request() : m_instance);
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
		}

		void entry() throws Exception {
			Entry.getInstance().execute();
		}

		void delete() throws Exception {
			for (val x : new String[] {
					"i_request",
			}) {
				log.info(String.format("Delete %s", x));
				JDBCUtils.execute(String.format("TRUNCATE TABLE %s CASCADE", x));
				JDBCUtils.commit();
			}
		}

		void insert() throws Exception {
			try (val stmt = JDBCUtils.createStatement()) {
				String sql;
				sql = "WITH s_params AS\n"
					+ "(\n"
						+ "SELECT ?::VARCHAR AS usage_history_id,\n"
							+ "?::VARCHAR AS media_id,\n"
							+ "?::VARCHAR AS service,\n"
							+ "?::VARCHAR AS usage_date,\n"
							+ "?::VARCHAR AS member_id,\n"
							+ "?::VARCHAR AS evaluation,\n"
							+ "?::VARCHAR AS channel,\n"
							+ "?::VARCHAR AS reserve_1,\n"
							+ "?::VARCHAR AS reserve_2,\n"
							+ "?::VARCHAR AS usage_within_last_2_year,\n"
							+ "?::VARCHAR AS usage_within_last_1_year,\n"
							+ "?::VARCHAR AS usage_within_last_6_month,\n"
							+ "?::VARCHAR AS usage_within_after_30_day,\n"
							+ "?::VARCHAR AS usage_within_after_60_day,\n"
							+ "?::VARCHAR AS usage_within_after_90_day,\n"
							+ "?::VARCHAR AS usage_within_after_120_day,\n"
							+ "?::VARCHAR AS usage_within_after_150_day,\n"
							+ "?::VARCHAR AS usage_within_after_180_day,\n"
							+ "?::VARCHAR AS usage_within_after_1_year,\n"
							+ "?::VARCHAR AS usage_within_after_2_year,\n"
							+ "?::VARCHAR AS all_usage_within_last_6_month,\n"
							+ "?::VARCHAR AS all_usage_within_after_30_day,\n"
							+ "?::VARCHAR AS all_usage_within_after_60_day,\n"
							+ "?::VARCHAR AS all_usage_within_after_90_day,\n"
							+ "?::VARCHAR AS all_usage_within_after_120_day,\n"
							+ "?::VARCHAR AS all_usage_within_after_150_day,\n"
							+ "?::VARCHAR AS all_usage_within_after_180_day\n"
					+ ")\n"
					+ "INSERT INTO i_usage_history\n"
					+ "(\n"
						+ "usage_history_id,\n"
						+ "media_id,\n"
						+ "service,\n"
						+ "usage_date,\n"
						+ "member_id,\n"
						+ "evaluation,\n"
						+ "channel,\n"
						+ "reserve_1,\n"
						+ "reserve_2,\n"
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
					+ "SELECT t10.usage_history_id,\n"
						+ "t10.media_id,\n"
						+ "t10.service,\n"
						+ "t10.usage_date,\n"
						+ "t10.member_id,\n"
						+ "t10.evaluation,\n"
						+ "t10.channel,\n"
						+ "t10.reserve_1,\n"
						+ "t10.reserve_2,\n"
						+ "t10.usage_within_last_2_year,\n"
						+ "t10.usage_within_last_1_year,\n"
						+ "t10.usage_within_last_6_month,\n"
						+ "t10.usage_within_after_30_day,\n"
						+ "t10.usage_within_after_60_day,\n"
						+ "t10.usage_within_after_90_day,\n"
						+ "t10.usage_within_after_120_day,\n"
						+ "t10.usage_within_after_150_day,\n"
						+ "t10.usage_within_after_180_day,\n"
						+ "t10.usage_within_after_1_year,\n"
						+ "t10.usage_within_after_2_year,\n"
						+ "t10.all_usage_within_last_6_month,\n"
						+ "t10.all_usage_within_after_30_day,\n"
						+ "t10.all_usage_within_after_60_day,\n"
						+ "t10.all_usage_within_after_90_day,\n"
						+ "t10.all_usage_within_after_120_day,\n"
						+ "t10.all_usage_within_after_150_day,\n"
						+ "t10.all_usage_within_after_180_day\n"
					+ "FROM s_params AS t10\n";

				stmt.parse(sql);

				int rowNums = 0;

				try (val reader = new Reader()) {
					for (boolean done = false; done == false;) {
						for (int rowNum = 0; rowNum < 10000; rowNum++) {
							val rec = reader.read();
							if (rec == null) {
								done = true;
								break;
							}

							int colNum = 1;
							stmt.setString(colNum++, rec.getUsageHistoryId());
							stmt.setString(colNum++, rec.getMediaId());
							stmt.setString(colNum++, rec.getService());
							stmt.setString(colNum++, rec.getUsageDate());
							stmt.setString(colNum++, rec.getMemberId());
							stmt.setString(colNum++, rec.getEvaluation());
							stmt.setString(colNum++, rec.getUsageType());
							stmt.setString(colNum++, rec.getReserve1());
							stmt.setString(colNum++, rec.getReserve2());
							stmt.setString(colNum++, rec.getUsageWithinLast2Year());
							stmt.setString(colNum++, rec.getUsageWithinLast1Year());
							stmt.setString(colNum++, rec.getUsageWithinLast6Month());
							stmt.setString(colNum++, rec.getUsageWithinAfter30Day());
							stmt.setString(colNum++, rec.getUsageWithinAfter60Day());
							stmt.setString(colNum++, rec.getUsageWithinAfter90Day());
							stmt.setString(colNum++, rec.getUsageWithinAfter120Day());
							stmt.setString(colNum++, rec.getUsageWithinAfter150Day());
							stmt.setString(colNum++, rec.getUsageWithinAfter180Day());
							stmt.setString(colNum++, rec.getUsageWithinAfter1Year());
							stmt.setString(colNum++, rec.getUsageWithinAfter2Year());
							stmt.setString(colNum++, rec.getAllUsageWithinLast6Month());
							stmt.setString(colNum++, rec.getAllUsageWithinAfter30Day());
							stmt.setString(colNum++, rec.getAllUsageWithinAfter60Day());
							stmt.setString(colNum++, rec.getAllUsageWithinAfter90Day());
							stmt.setString(colNum++, rec.getAllUsageWithinAfter120Day());
							stmt.setString(colNum++, rec.getAllUsageWithinAfter150Day());
							stmt.setString(colNum++, rec.getAllUsageWithinAfter180Day());
							stmt.addBatch();
						}

						rowNums += stmt.executeBatchAndClear().length;
						JDBCUtils.commit();
					}
				}

				log.info(String.format("Insert i_usage_history[count=%s]", rowNums));
			}
		}
	}
}
