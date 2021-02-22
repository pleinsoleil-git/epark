package app.takahashi.a00100.job.a00100.crawler.job.request.crawler;

import org.openqa.selenium.WebDriver;

import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class WebClient {
	public WebDriver getDriver() {
		return WebBrowser.getCurrent().getDriver();
	}

	public WebClient execute() throws Exception {
		navigate();
		return submit();
	}

	public void navigate() throws Exception {
	}

	public WebClient submit() throws Exception {
		return null;
	}
}
