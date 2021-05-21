package org.springframework.samples.petclinic.integration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.test.context.event.annotation.AfterTestClass;

import java.util.concurrent.TimeUnit;

public class Browser {

	public final static WebDriver webDriver = createDriver();

	private static WebDriver createDriver() {
		setupDriver();
		ChromeDriver chromeDriver = new ChromeDriver(new ChromeDriverService.Builder().withSilent(true).build(),
			chromeOptions());
		chromeDriver.manage().window().setSize(new Dimension(1024, 768));
		chromeDriver.manage().timeouts().implicitlyWait(0L, TimeUnit.SECONDS);
		return chromeDriver;
	}

	private static void setupDriver() {
		WebDriverManager.chromedriver().timeout(60).setup();
	}

	private static ChromeOptions chromeOptions() {
		final ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("enable-automation");
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--disable-infobars");
		chromeOptions.addArguments("--disable-dev-shm-usage");
		chromeOptions.addArguments("--disable-browser-side-navigation");
		return chromeOptions;
	}





}
