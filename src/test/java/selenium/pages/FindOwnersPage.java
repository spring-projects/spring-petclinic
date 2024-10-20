package selenium.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.TestBase;

import java.time.Duration;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class FindOwnersPage extends TestBase {

	public FindOwnersPage(WebDriver driver, Properties loc) {
		TestBase.driver = driver;
		TestBase.locators = loc;
	}

	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

	By findOwnersPageLink = By.xpath(locators.getProperty("findOwnersLink"));

	By lastNameField = By.id(locators.getProperty("lastNameField"));

	By findOwnerButton = By.cssSelector(locators.getProperty("findOwnerButton"));

	By addOwnerButton = By.xpath(locators.getProperty("addOwnerButton"));

	By ownerNotFound = By.cssSelector(locators.getProperty("ownerNotFound"));

	public void navigateToFindOwnersPage() {
		wait.until(visibilityOfElementLocated(findOwnersPageLink));
		driver.findElement(findOwnersPageLink).click();
	}

	public void setTextInLastNameField(String text) {
		wait.until(visibilityOfElementLocated(lastNameField));
		driver.findElement(lastNameField).sendKeys(text);
	}

	public void clickOnFindOwnerButton() {
		driver.findElement(findOwnerButton).sendKeys(Keys.RETURN);
	}

	public void clickOnAddOwnerButton() {
		driver.findElement(addOwnerButton).click();
		wait.until(visibilityOfElementLocated(By.tagName("h2")));
	}

	public String getOwnerNotFoundText() {
		try {
			WebElement ownerNotFoundElement = driver.findElement(ownerNotFound);
			return ownerNotFoundElement.getText();
		}
		catch (NoSuchElementException e) {
			return null;
		}
	}

}
