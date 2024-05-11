package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.TestBase;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

public class AddPetPage extends TestBase {

	public AddPetPage(WebDriver driver, Properties loc) {
		TestBase.driver = driver;
		TestBase.locators = loc;
	}

	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

	By namePetField = By.id(locators.getProperty("namePetField"));

	By birthDateField = By.id(locators.getProperty("birthDateField"));

	By petTypeField = By.id(locators.getProperty("petTypeField"));

	By addPetButton = By.xpath(locators.getProperty("addPetButton"));

	By updatePetButton = By.xpath(locators.getProperty("updatePetButton"));

	public void fillTheFields(String namePet, String birthDate, String petType) {
		driver.findElement(namePetField).sendKeys(namePet);
		driver.findElement(birthDateField).sendKeys(birthDate);
		WebElement dropdownElement = driver.findElement(petTypeField);
		Select dropdown = new Select(dropdownElement);
		dropdown.selectByValue(petType);
	}

	public void clickOnAddPetButton() {
		driver.findElement(addPetButton).click();
	}

	public boolean isErrorMessageDisplayedForSamePetName() {
		WebElement error = driver.findElement(By.className(locators.getProperty("addPetPageErrorMessage")));
		String expectedErrorText = tap.getProperty("samePetNameErrorMessage");
		return error.getText().equals(expectedErrorText);
	}

	public boolean isErrorMessageDisplayedForEmptyFields(String message) {
		List<WebElement> messages = driver.findElements(By.xpath("//*[contains(text(),'" + message + "')]"));
		wait.until(visibilityOfAllElements(messages));
		return messages.size() == 2;
	}

	public boolean isInvalidDateErrorMessageDisplayed() {
		WebElement error = driver.findElement(By.className(locators.getProperty("addPetPageErrorMessage")));
		String expectedErrorText = tap.getProperty("invalidDateErrorMessage");
		return error.getText().equals(expectedErrorText);
	}

	public void clearFields() {
		driver.findElement(namePetField).clear();
		driver.findElement(birthDateField).clear();
	}

	public void clickOnUpdatePetButton() {
		driver.findElement(updatePetButton).click();
	}

}
