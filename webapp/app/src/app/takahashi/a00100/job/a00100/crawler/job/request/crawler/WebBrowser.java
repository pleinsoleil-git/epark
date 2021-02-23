package app.takahashi.a00100.job.a00100.crawler.job.request.crawler;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import common.webDriver.ChromeWebDriver;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class WebBrowser implements AutoCloseable {
	static WebBrowser m_instance;
	static Map<Long, _Current> m_currents = new ConcurrentHashMap<Long, _Current>();

	_Current m_current;

	WebBrowser() {
	}

	public static WebBrowser getInstance() {
		return (m_instance == null ? m_instance = new WebBrowser() {
			{
				val file = new File(System.getProperty("catalina.home"), ChromeWebDriver.V88);
				System.setProperty("webdriver.chrome.driver", file.getPath());
			}
		} : m_instance);
	}

	public static _Current getCurrent() {
		val id = Thread.currentThread().getId();
		_Current current = m_currents.get(id);

		if (current == null) {
			m_currents.put(id, current = new _Current());
		}

		return current;
	}

	@Override
	public void close() throws Exception {
		for (val x : m_currents.values()) {
			x.close();
		}
	}

	public static class _Current implements AutoCloseable {
		WebDriver m_driver;

		public WebDriver getDriver() {
			return (m_driver == null ? m_driver = new ChromeDriver() : m_driver);
		}

		@Override
		public void close() throws Exception {
			if (m_driver != null) {
				//m_driver.quit();
			}
		}
	}
}
