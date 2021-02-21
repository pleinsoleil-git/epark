package app.takahashi.a00100.job.a00100.export.job;

import lombok.Data;
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
		} finally {
			m_instance = null;
		}
	}

	@Data
	public static class _Current {
		Long m_id;

		public void execute() throws Exception {
		}
	}
}
