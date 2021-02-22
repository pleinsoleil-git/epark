package app.takahashi.a00100.job.a00100.crawler;

import app.takahashi.a00100.job.a00100.export.job.Job;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Model extends app.takahashi.a00100.job.common.app.Model {
	static boolean m_executing = false;

	@Override
	public void execute() throws Exception {
		if (isExecutable() == true) {
			try {
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
