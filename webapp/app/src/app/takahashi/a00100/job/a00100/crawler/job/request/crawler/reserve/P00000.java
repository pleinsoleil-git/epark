package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.reserve;

import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebClient;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
class P00000 extends WebClient {
	String getURI() {
		return "https://ssl.haisha-yoyaku.jp/"
				+ Request.getCurrent().getShopownerId()
				+ "/login/serviceAppoint/index?SITE_CODE=sgs&utm_source=epark.jp&utm_medium=referral&utm_content=shop_reserve";
	}

	@Override
	public void navigate() throws Exception {
		val driver = getDriver();
		driver.get(getURI());
	}

	public WebClient submit() throws Exception {
		return null;
	}
}
