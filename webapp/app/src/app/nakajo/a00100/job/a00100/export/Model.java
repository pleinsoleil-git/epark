package app.nakajo.a00100.job.a00100.export;

import app.nakajo.a00100.job.a00100.load.job.Job;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Model extends app.nakajo.a00100.job.common.app.Model {
	static boolean m_executing = false;

	@Override
	public void execute() throws Exception {
		if (isExecutable() == true) {
			try {
				job();
			} finally {
				m_executing = false;
			}
		}
	}

	synchronized boolean isExecutable() {
		return (m_executing == false ? m_executing = true : m_executing);
	}

	void job() throws Exception {
		Job.getInstance().execute();
	}
}
