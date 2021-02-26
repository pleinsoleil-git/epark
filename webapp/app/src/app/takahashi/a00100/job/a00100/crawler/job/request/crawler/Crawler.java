package app.takahashi.a00100.job.a00100.crawler.job.request.crawler;

import java.util.ArrayList;
import java.util.Collection;

import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.reserve.Reserve;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.top.Top;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Crawler {
	static ThreadLocal<Crawler> m_instances = new ThreadLocal<Crawler>() {
		@Override
		protected Crawler initialValue() {
			return new Crawler();
		}
	};
	_Current m_current;

	Crawler() {
	}

	public static Crawler getInstance() {
		return m_instances.get();
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
			//top();
			reserve();
		}

		void top() throws Exception {
			Top.getInstance().execute();
		}

		void reserve() throws Exception {
			Reserve.getInstance().execute();
		}
	}
}
