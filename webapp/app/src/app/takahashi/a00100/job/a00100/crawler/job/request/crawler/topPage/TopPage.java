package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.topPage;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class TopPage {
	static TopPage m_instance;
	_Current m_current;

	TopPage() {
	}

	public static TopPage getInstance() {
		return (m_instance == null ? m_instance = new TopPage() : m_instance);
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

	@Data
	public static class _Current {
		public void execute() throws Exception {
		}
	}
}