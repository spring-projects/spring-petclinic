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
		return this.config.getDriver().getTitle();
	}

	@After
	public void closeConnection() {
		config.getDriver().quit();
	}

	@Test
	public void googleTitleIT() {
		WebDriver driver = config.getDriver();
		driver.get(url);
		assertEquals("Title not as expected: ", "PetClinic :: a Spring Framework demonstration", getTitle());
		// Save the random value from the page.
		String welcomeText = driver.findElement(By.id("welcome")).getText();
		// Reload the page and get a new random value.
		driver.get(url);
		String value2 = driver.findElement(By.id("random-value")).getText();
		// Values should not be the same.
		assertEquals("Wrong welcome text.", "Welcome to the Pet Clinic!", welcomeText);
		System.out.println("Text extracted: " + welcomeText);
	}

}
