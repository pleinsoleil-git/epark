package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.reserve;

import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class PageData implements AutoCloseable {
	static ThreadLocal<PageData> m_instances = new ThreadLocal<PageData>() {
		@Override
		protected PageData initialValue() {
			return new PageData();
		}
	};
	_Current m_current;

	PageData() {
	}

	static PageData getInstance() {
		return m_instances.get();
	}

	static _Current getCurrent() {
		val x = getInstance();
		return (x.m_current == null ? x.m_current = new _Current() : x.m_current);
	}

	@Override
	public void close() throws Exception {
		try {
			if (m_current != null) {
				val request = Request.getCurrent();
				request.getResults().add(m_current);
			}
		} finally {
			m_instances.remove();
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	static class _Current extends WebData {
		@Override
		public void close() throws Exception {
		}
	}
}
