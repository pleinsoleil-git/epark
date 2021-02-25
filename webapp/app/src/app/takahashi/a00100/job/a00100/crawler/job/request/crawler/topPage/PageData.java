package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.topPage;

import app.takahashi.a00100.job.a00100.crawler.job.request.Result;

class PageData extends Result {
	static ThreadLocal<PageData> m_currents = new ThreadLocal<PageData>() {
		@Override
		protected PageData initialValue() {
			return new PageData();
		}
	};

	PageData() {
	}

	PageData getCurrent() {
		return m_currents.get();
	}

	PageData remove() {
		try {
			return m_currents.get();
		} finally {
			m_currents.remove();
		}
	}
}
