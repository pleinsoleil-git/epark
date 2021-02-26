package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.reserve;

import java.util.ArrayList;

import org.openqa.selenium.By;

import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebClient;
import common.lang.StringUtils;
import common.lang.exception.EmptyException;
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

	@Override
	public WebClient submit() throws Exception {
		getServiceList();
		return null;
	}

	void getServiceList() throws Exception {
		val data = PageData.getCurrent();
		val driver = getDriver();

		for (int i = 0; i < 10; i++) {
			if (i > 0) {
				Thread.sleep(500);
			}

			try {
				val serviceList = new ArrayList<String>() {
					{
						val by = By.xpath("//ul[@class='service_list']/li");

						for (val x : driver.findElements(by)) {
							val text = StringUtils.trim(x.getText());
							if (StringUtils.isEmpty(text) == true) {
								throw new EmptyException();
							}

							add(text);
						}
					}
				};

				data.setServiceList(serviceList);
				return;
			} catch (EmptyException e) {
				log.error(String.format("Empty menu[id=%s]",
						Request.getCurrent().getCatalogId()));
			}
		}
	}
}
