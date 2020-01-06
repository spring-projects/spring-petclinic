package integration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumConfig {
	private WebDriver driver;

	public SeleniumConfig() throws MalformedURLException {
		Capabilities capabilities = new FirefoxOptions();
		driver = new RemoteWebDriver(
				new URL("http://selenium-hub-cicd.apps.cluster-ottawa-630b.ottawa-630b.example.opentlc.com/wd/hub"),
				capabilities);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public WebDriver getDriver() {
		return driver;
	}
}
