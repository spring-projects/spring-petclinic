package integration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumConfig {
	public static final String SELENIUM_HUB_URL = "http://selenium-hub-cicd.apps.cluster-ottawa-7ca3.ottawa-7ca3.example.opentlc.com/wd/hub";

	private WebDriver firefoxDriver;
	private WebDriver chromeDriver;

	public SeleniumConfig() throws MalformedURLException {
		Capabilities firefoxCapabilities = new FirefoxOptions();
		firefoxDriver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), firefoxCapabilities);
		firefoxDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		Capabilities chromeCapabilities = new ChromeOptions();
		chromeDriver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), chromeCapabilities);
		chromeDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public WebDriver getFirefoxDriver() {
		return firefoxDriver;
	}

	public WebDriver getChromeDriver() {
		return chromeDriver;
	}

}
