package app.takahashi.a00100.job.a00100.crawler.job.request;

import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m_", chain = false)
class Status implements AutoCloseable {
	Long m_status;
	String m_message;

	@Override
	public void close() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS request_id,\n"
					+ "?::NUMERIC AS status,\n"
					+ "?::VARCHAR AS message\n"
			+ ")\n"
			+ "INSERT INTO j_request_status\n"
			+ "(\n"
				+ "foreign_id,\n"
				+ "status,\n"
				+ "message\n"
			+ ")\n"
			+ "SELECT t10.request_id,\n"
				+ "t10.status,\n"
				+ "t10.message\n"
			+ "FROM s_params AS t10\n";

		val params = new JDBCParameter() {
			{
				add(Request.getCurrent().getId());
				add(getStatus());
				add(getMessage());
			}
		};

		JDBCUtils.execute(sql, params);
		JDBCUtils.commit();
	}
}
