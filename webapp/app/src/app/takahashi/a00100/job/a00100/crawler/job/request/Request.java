package app.takahashi.a00100.job.a00100.crawler.job.request;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import app.takahashi.a00100.job.a00100.crawler.job.Job;
import common.jdbc.JDBCParameter;
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
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS job_id\n"
			+ ")\n"
			+ "SELECT j10.id,\n"
				+ "j10.catalog_id AS catalogId,\n"
				+ "j10.shopowner_id AS shopownerId\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_request AS j10\n"
				+ "ON j10.foreign_id = t10.job_id\n"
			+ "WHERE j10.deleted = FALSE\n"
			+ "AND NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_request_status AS j900\n"
				+ "WHERE j900.foreign_id = j10.id\n"
			+ ")\n"
			+ "ORDER BY 1\n";

		val params = new JDBCParameter() {
			{
				val job = Job.getCurrent();
				add(job.getId());
			}
		};
		val rsh = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rsh, params);
	}

	@Data
	public static class _Current {
		Long m_id;

		public void execute() throws Exception {
			request();
			log.info("Done!!");
		}

		void request() throws Exception {
		}
	}
}
