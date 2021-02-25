package app.takahashi.a00100.job.a00100.crawler.job.request;

import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Result implements AutoCloseable {
	@Override
	public void close() throws Exception {
	}
}
