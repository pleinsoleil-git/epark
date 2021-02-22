package app.takahashi.a00100.job.a00100.crawler.job.request.crawler;

import java.util.ArrayList;
import java.util.Collection;

import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Crawler {
	static Crawler m_instance;
	_Current m_current;

	Crawler() {
	}

	public static Crawler getInstance() {
		return (m_instance == null ? m_instance = new Crawler() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
		try (val browser = WebBrowser.getInstance()) {
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
			topPage();
			reservePage();
		}

		void topPage() throws Exception {
		}

		void reservePage() throws Exception {
		}
	}
}
