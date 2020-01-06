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
	public void petclinicTitleIT() {
		WebDriver driver = config.getDriver();
		driver.get(url);
		assertEquals("Title not as expected: ", "PetClinic :: a Spring Framework demonstration", getTitle());
		String welcomeText = driver.findElement(By.id("welcome")).getText();
		assertEquals("Wrong welcome text.", "Welcome to the Pet Clinic!", welcomeText);
		System.out.println("Text extracted: " + welcomeText);
	}

}
