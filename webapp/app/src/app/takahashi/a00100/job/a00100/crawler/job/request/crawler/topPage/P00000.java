package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.topPage;

import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebClient;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class P00000 extends WebClient {
	String getURI() {
		return "https://haisha-yoyaku.jp/bun2sdental/detail/index/id/"
				+ Request.getCurrent().getCatalogId();
	}

	@Override
	public void navigate() throws Exception {
		val driver = getDriver();
		driver.get(getURI());
	}
}
