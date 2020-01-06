package integration;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPageIT {

	private SeleniumConfig config;
	private String url = "http://app-petclinic-dev.apps.cluster-ottawa-630b.ottawa-630b.example.opentlc.com/";

	public MainPageIT() throws MalformedURLException {
		config = new SeleniumConfig();
	}

	public String getTitle() {
		return this.config.getFirefoxDriver().getTitle();
	}

	@After
	public void closeConnection() {
		config.getFirefoxDriver().quit();
	}

	@Test
	public void petclinicTitleFireFoxIT() {
		WebDriver driver = config.getFirefoxDriver();
		driver.get(url);
		assertEquals("Title not as expected: ", "PetClinic :: a Spring Framework demonstration", getTitle());
		String welcomeText = driver.findElement(By.id("welcome")).getText();
		assertEquals("Wrong welcome text.", "Welcome to the Red Hat Pet Clinic", welcomeText);
		System.err.println("Text extracted: " + welcomeText);
	}

	@Test
	public void petclinicTitleChromeIT() {
		WebDriver driver = config.getChromeDriver();
		driver.get(url);
		assertEquals("Title not as expected: ", "PetClinic :: a Spring Framework demonstration", getTitle());
		String welcomeText = driver.findElement(By.id("welcome")).getText();
		assertEquals("Wrong welcome text.", "Welcome to the Red Hat Pet Clinic", welcomeText);
		System.err.println("Text extracted: " + welcomeText);
	}

}
