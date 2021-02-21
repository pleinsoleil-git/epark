package app.takahashi.a00100.job.a00100.export.job;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

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
			for (val x : query()) {
				(m_current = x).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	Collection<_Current> query() throws Exception {
		String sql;
		sql = "SELECT j10.id\n"
			+ "FROM j_job AS j10\n"
			+ "WHERE j10.deleted = FALSE\n"
			+ "ORDER BY j10.id\n";

		val rsh = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rsh);
	}

	@Data
	public static class _Current {
		Long m_id;

		public void execute() throws Exception {
			System.out.println(m_id);
		}
	}
}
