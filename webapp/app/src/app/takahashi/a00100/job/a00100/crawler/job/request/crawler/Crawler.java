package app.takahashi.a00100.job.a00100.crawler.job.request.crawler;

import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Crawler {
	public void execute() throws Exception {
		topPage();
		reservePage();
	}

	public void save() throws Exception {
	}

	void topPage() throws Exception {
	}

	void reservePage() throws Exception {
	}
}
