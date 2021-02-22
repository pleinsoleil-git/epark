package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.top;

import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebClient;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class P00000 extends WebClient {
	String getURI() {
		return "https://haisha-yoyaku.jp/bun2sdental/detail/index/id/"
				+ Request.getCurrent().getCatalogId();
	}
}
