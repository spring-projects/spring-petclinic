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
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class OwnerPage extends TestBase {

	public OwnerPage(WebDriver driver, Properties loc) {
		TestBase.driver = driver;
		TestBase.locators = loc;
	}

	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

	By editOwnerButton = By.xpath(locators.getProperty("editOwnerButton"));

	By addNewPetButton = By.xpath(locators.getProperty("addNewPetButton"));

	By editPetButton = By.xpath(locators.getProperty("editPetButton"));

	By addVisitButton = By.xpath(locators.getProperty("addVisitButton"));

	By nameLocator = By.xpath(locators.getProperty("nameLocator"));

	By successMessage = By.id(locators.getProperty("successMessageAlert"));

	By updateMessage = By.id(locators.getProperty("updateMessageAlert"));

	By newPetAddedMessage = By.id(locators.getProperty("newPetAddedMessageAlert"));

	By petDetailsClass = By.className(locators.getProperty("petDetails"));

	By petUpdateMessage = By.id(locators.getProperty("updateMessageAlert"));

	By visitAddedMessage = By.id(locators.getProperty("visitAddedMessage"));

	By visitDetailsClass = By.className(locators.getProperty("visitDetails"));

	public boolean isLastNameDisplayed(String lastName) {
		WebElement element = driver.findElement(nameLocator);
		return element.getText().contains(lastName);
	}

	public boolean isSuccessMessageDisplayed() {
		wait.until(visibilityOfElementLocated(successMessage));
		WebElement message = driver.findElement(successMessage);
		String expectedSuccessMessage = tap.getProperty("successMessage");
		return message.getText().equals(expectedSuccessMessage);
	}

	public void clickOnEditOwnerButton() {
		driver.findElement(editOwnerButton).click();
		wait.until(visibilityOf(driver.findElement(By.tagName("h2"))));
	}

	public boolean isUpdateMessageDisplayed() {
		wait.until(visibilityOfElementLocated(updateMessage));
		WebElement message = driver.findElement(updateMessage);
		String expectedUpdateMessage = tap.getProperty("updateMessage");
		return message.getText().equals(expectedUpdateMessage);
	}

	public void clickOnAddNewPetButton() {
		driver.findElement(addNewPetButton).click();
		wait.until(visibilityOf(driver.findElement(By.tagName("h2"))));
	}

	public boolean isPetAddedSuccessMessageDisplayed() {
		wait.until(visibilityOfElementLocated(newPetAddedMessage));
		WebElement message = driver.findElement(newPetAddedMessage);
		String expectedMessage = tap.getProperty("newPetAddedMessage");
		return message.getText().equals(expectedMessage);
	}

	public boolean isPetNameDisplayed(String petName) {
		List<WebElement> petDetails = driver.findElements(petDetailsClass);
		List<String> petDetailsText = petDetails.stream().map(WebElement::getText).toList();
		String expectedPetName = input.getProperty(petName);
		return petDetailsText.stream().anyMatch(petDetailsItem -> petDetailsItem.contains(expectedPetName));
	}

	public void clickOnEditPetButton() {
		List<WebElement> editButtons = driver.findElements(editPetButton);
		if (!editButtons.isEmpty()) {
			editButtons.get(0).click();
		}
	}

	public boolean isUpdatePetMessageDisplayed() {
		wait.until(visibilityOfElementLocated(petUpdateMessage));
		WebElement message = driver.findElement(petUpdateMessage);
		String expectedUpdateMessage = tap.getProperty("petUpdateMessage");
		return message.getText().equals(expectedUpdateMessage);
	}

	public void clickOnAddVisitButton() {
		driver.findElement(addVisitButton).click();
	}

	public boolean isVisitAddedMessageDisplayed() {
		wait.until(visibilityOfElementLocated(visitAddedMessage));
		WebElement message = driver.findElement(visitAddedMessage);
		String expectedUpdateMessage = tap.getProperty("visitAddedMessage");
		return message.getText().equals(expectedUpdateMessage);
	}

	public boolean isVisitAdded(String description) {
		WebElement visitDetails = driver.findElement(visitDetailsClass);
		String expectedDescription = input.getProperty(description);
		return visitDetails.getText().contains(expectedDescription);
	}

}
