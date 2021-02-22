package app.takahashi.a00100.job.a00100.export.job.request.report.match;

import java.util.ArrayList;
import java.util.Collection;

import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Match {
	static Match m_instance;
	_Current m_current;

	Match() {
	}

	public static Match getInstance() {
		return (m_instance == null ? m_instance = new Match() : m_instance);
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
			menu();
		}

		void menu() throws Exception {
			Menu.getInstance().execute();
		}
	}
}
