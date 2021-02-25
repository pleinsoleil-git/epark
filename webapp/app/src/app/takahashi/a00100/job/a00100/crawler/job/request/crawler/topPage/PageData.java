package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.topPage;

import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import app.takahashi.a00100.job.a00100.crawler.job.request.Result;
import lombok.val;

class PageData extends Result {
	static ThreadLocal<PageData> m_currents = new ThreadLocal<PageData>() {
		@Override
		protected PageData initialValue() {
			return new PageData();
		}
	};

	PageData() {
	}

	static PageData getCurrent() {
		return m_currents.get();
	}

	@Override
	public void close() throws Exception {
		try {
			val request = Request.getCurrent();
			request.getResults().add(this);
		} finally {
			m_currents.remove();
		}
	}
}
