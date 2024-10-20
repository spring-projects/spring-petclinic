package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.TestBase;

import java.time.Duration;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class HomePage extends TestBase {

	public HomePage(WebDriver driver, Properties loc) {
		TestBase.driver = driver;
		TestBase.locators = loc;
	}

	By welcomePhoto = By.className(locators.getProperty("welcomePhoto"));

	By homePageLink = By.xpath(locators.getProperty("homePageLink"));

	By logoLink = By.xpath(locators.getProperty("logoLink"));

	public void clickOnHomePageLink() {
		driver.findElement(homePageLink).click();
	}

	public boolean isWelcomePhotoVisible(String src) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		wait.until(visibilityOfElementLocated(welcomePhoto));
		WebElement welcomePicture = driver.findElement(welcomePhoto);
		return welcomePicture.getAttribute("src").equals(src);
	}

	public void clickOnLogoLink() {
		driver.findElement(logoLink).click();
	}

}
