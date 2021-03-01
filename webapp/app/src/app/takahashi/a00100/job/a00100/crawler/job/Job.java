package app.takahashi.a00100.job.a00100.crawler.job;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Job {
	static Job m_instance;
	_Current m_current;

	Job() {
	}

	public static Job getInstance() {
		return (m_instance == null ? m_instance = new Job() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
		try {
			// delete();

			for (val x : query()) {
				(m_current = x).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	void delete() throws Exception {
		for (val x : new String[] {
				"j_request_status",
				"t_clinic",
		}) {
			log.info(String.format("Delete %s", x));
			JDBCUtils.execute(String.format("TRUNCATE TABLE %s CASCADE", x));
			JDBCUtils.commit();
		}
	}

	Collection<_Current> query() throws Exception {
		String sql;
		sql = "SELECT j10.id,\n"
				+ "j10.thread_nums AS threadNums\n"
			+ "FROM j_job AS j10\n"
			+ "WHERE j10.deleted = FALSE\n"
			+ "ORDER BY j10.id\n";

		val rsh = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rsh);
	}

	@Data
	public static class _Current {
		Long m_id;
		Long m_threadNums;

		public void execute() throws Exception {
			request();
			log.info("Done!!");
		}

		void request() throws Exception {
			Request.getInstance().execute();
		}
	}
}
