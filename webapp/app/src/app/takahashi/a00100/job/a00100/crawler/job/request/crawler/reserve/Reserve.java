package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.reserve;

import java.util.ArrayList;
import java.util.Collection;

import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebClient;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Reserve {
	static ThreadLocal<Reserve> m_instances = new ThreadLocal<Reserve>() {
		@Override
		protected Reserve initialValue() {
			return new Reserve();
		}
	};
	_Current m_current;

	Reserve() {
	}

	public static Reserve getInstance() {
		return m_instances.get();
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
			m_instances.remove();
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
			try (val data = PageData.getInstance()) {
				for (WebClient client = new P00000(); client != null;) {
					client = client.execute();
				}
			}
		}
	}
}
