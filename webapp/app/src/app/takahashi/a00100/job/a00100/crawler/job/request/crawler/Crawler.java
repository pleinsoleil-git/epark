package app.takahashi.a00100.job.a00100.crawler.job.request.crawler;

import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Crawler {
	static ThreadLocal<Crawler> m_instances = new ThreadLocal<Crawler>() {
		@Override
		protected Crawler initialValue() {
			return new Crawler();
		}
	};

	Crawler() {
	}

	public static Crawler getInstance() {
		return m_instances.get();
	}

	public void execute() throws Exception {
		try {

		} finally {
			m_instances.remove();
		}

		topPage();
		reservePage();
	}

	void topPage() throws Exception {
	}

	void reservePage() throws Exception {
	}

	public void save() throws Exception {
	}
}
