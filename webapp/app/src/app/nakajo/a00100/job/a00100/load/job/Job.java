package app.nakajo.a00100.job.a00100.load.job;

import java.util.ArrayList;
import java.util.Collection;

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
			delete();

			for (val x : query()) {
				(m_current = x).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	void delete() throws Exception {
		for (val x : new String[] {
				"i_usage_history",
		}) {
			log.info(String.format("Delete %s", x));
			JDBCUtils.execute(String.format("TRUNCATE TABLE %s CASCADE", x));
			JDBCUtils.commit();
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
		Long m_id;

		public void execute() throws Exception {
			log.info("Done!!");
		}
	}
}
