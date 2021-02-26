package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.top;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

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
		return "https://haisha-yoyaku.jp/bun2sdental/detail/index/id/"
				+ Request.getCurrent().getCatalogId();
	}

	@Override
	public void navigate() throws Exception {
		val driver = getDriver();
		driver.get(getURI());
	}

	@Override
	public WebClient submit() throws Exception {
		getDentalName();
		getTotalStar();
		getReviewCount();
		getMeniList();
		getNetReserveType();
		return null;
	}

	void getDentalName() throws Exception {
		val data = PageData.getCurrent();
		val driver = getDriver();
		val by = By.xpath("//h1[@class='name_main']");

		for (val x : driver.findElements(by)) {
			data.setDentalName(StringUtils.trim(x.getText()));
		}
	}

	void getTotalStar() throws Exception {
		val data = PageData.getCurrent();
		val driver = getDriver();
		val by = By.xpath("//div[@class='hyouka']/div/div/p[@class='totalStar']");

		for (val x : driver.findElements(by)) {
			val c = StringUtils.trim(x.getText());
			val p = Pattern.compile("[^\\d\\.]");
			val m = p.matcher(c);

			data.setTotalStar(m.find() == true ? m.replaceAll("") : c);
		}
	}

	void getReviewCount() throws Exception {
		val data = PageData.getCurrent();
		val driver = getDriver();
		val by = By.xpath("//p[@class='column2_kuchikomi']/a[@class='count']");

		for (val x : driver.findElements(by)) {
			data.setReviewCount(StringUtils.trim(x.getText()));
		}
	}

	void getMeniList() throws Exception {
		val data = PageData.getCurrent();
		val driver = getDriver();

		for (int i = 0; i < 10; i++) {
			if (i > 0) {
				Thread.sleep(500);
			}

			try {
				val menuList = new LinkedHashMap<String, ArrayList<String>>() {
					{
						val by = By.xpath("//ul[@class='tab_detail-clinic ' or @class='tab_detail-clinic']");

						for (val x : driver.findElements(by)) {
							for (val menu : x.findElements(By.xpath("./child::li/a"))) {
								val title = StringUtils.split(menu.getText())[0];
								if (containsKey(title) == true) {
									continue;
								}

								put(title, new ArrayList<String>() {
									{
										// --------------------------------------------------
										// ホバーして一度表示しないとテキストが取れない
										// --------------------------------------------------
										val items = menu.findElements(By.xpath("./following-sibling::ul/li/a"));
										if (items.isEmpty() == false) {
											val action = new Actions(driver);
											action.moveToElement(menu).build().perform();

											for (val item : items) {
												action.clickAndHold(item).build().perform();

												val text = StringUtils.trim(item.getText());
												if (StringUtils.isEmpty(text) == true) {
													throw new EmptyException();
												}

												add(text);
											}
										}
									}
								});
							}
						}
					}
				};

				data.setMenuList(menuList);
				return;
			} catch (EmptyException e) {
				log.error(String.format("Empty menu[id=%s]",
						Request.getCurrent().getCatalogId()));
			}
		}
	}

	void getNetReserveType() throws Exception {
		val data = PageData.getCurrent();
		val driver = getDriver();
		val by = By.xpath("//a[@class='net_reserve_btn']");

		for (val x : driver.findElements(by)) {
			data.setNetReserveType(StringUtils.split(StringUtils.trim(x.getText()))[0]);
		}
	}
}
