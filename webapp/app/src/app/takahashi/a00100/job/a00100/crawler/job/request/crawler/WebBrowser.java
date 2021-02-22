package app.takahashi.a00100.job.a00100.crawler.job.request.crawler;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import common.webDriver.ChromeWebDriver;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class WebBrowser implements AutoCloseable {
	static WebBrowser m_instance;
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
		val x = getInstance();
		return (x.m_current == null ? x.m_current = new _Current() : x.m_current);
	}

	@Override
	public void close() throws Exception {
		if (m_current != null) {
			m_current.close();
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
				m_driver.quit();
			}
		}
	}
}
