package app.takahashi.a00100.job.a00100.crawler.job.request.crawler;

public class CrawlerData {
	static CrawlerData m_instance;

	CrawlerData() {
	}

	public static CrawlerData getInstance() {
		return (m_instance == null ? m_instance = new CrawlerData() : m_instance);
	}

	public void save() throws Exception {
	}

	public static class _Current {
	}
}
