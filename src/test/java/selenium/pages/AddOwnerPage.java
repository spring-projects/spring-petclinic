package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.TestBase;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

public class AddOwnerPage extends TestBase {

	public AddOwnerPage(WebDriver driver, Properties loc) {
		TestBase.driver = driver;
		TestBase.locators = loc;
	}

	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

	By firstNameField = By.id(locators.getProperty("firstNameField"));

	By lastNameField = By.id(locators.getProperty("lastNameField"));

	By addressField = By.id(locators.getProperty("address"));

	By cityField = By.id(locators.getProperty("city"));

	By telephoneField = By.id(locators.getProperty("telephone"));

	By addOwnerButton = By.xpath(locators.getProperty("addOwnerButton2"));

	By updateOwnerButton = By.xpath(locators.getProperty("updateOwnerButton"));

	public void setTextInFields(String firstName, String lastName, String address, String city, String telephone) {
		driver.findElement(firstNameField).sendKeys(firstName);
		driver.findElement(lastNameField).sendKeys(lastName);
		driver.findElement(addressField).sendKeys(address);
		driver.findElement(cityField).sendKeys(city);
		driver.findElement(telephoneField).sendKeys(telephone);
	}

	public void clickingOnAddOwnerButton() {
		driver.findElement(addOwnerButton).click();
	}

	public boolean isErrorMessageDisplayedForEmptyFields(String message) {
		List<WebElement> messages = driver.findElements(By.xpath("//*[contains(text(),'" + message + "')]"));
		wait.until(visibilityOfAllElements(messages));
		return messages.size() >= 4;
	}

	public boolean isErrorMessageDisplayedForTextInTelephoneField(String message) {
		WebElement ErrorMessage = driver.findElement(By.xpath("//*[contains(text(),'" + message + "')]"));
		wait.until(visibilityOf(ErrorMessage));
		return ErrorMessage.getText().equals(message);
	}

	public void clearFields() {
		driver.findElement(firstNameField).clear();
		driver.findElement(lastNameField).clear();
		driver.findElement(addressField).clear();
		driver.findElement(cityField).clear();
		driver.findElement(telephoneField).clear();
	}

	public void clickOnUpdateOwnerButton() {
		driver.findElement(updateOwnerButton).click();
	}

}
