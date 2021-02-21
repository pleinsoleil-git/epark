package app.takahashi.a00100.job.a00100.export.job.request.report.reserve;

import java.util.ArrayList;
import java.util.Collection;

import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Reserve {
	static Reserve m_instance;
	_Current m_current;

	Reserve() {
	}

	public static Reserve getInstance() {
		return (m_instance == null ? m_instance = new Reserve() : m_instance);
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

	public static class _Current {
		public void execute() throws Exception {
			medical();
		}

		void medical() throws Exception {
			Medical.getInstance().execute();
		}
	}
}
