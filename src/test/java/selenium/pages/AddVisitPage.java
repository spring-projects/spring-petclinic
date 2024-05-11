package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.TestBase;

import java.time.Duration;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class AddVisitPage extends TestBase {

	public AddVisitPage(WebDriver driver, Properties loc) {
		TestBase.driver = driver;
		TestBase.locators = loc;
	}

	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));

	By visitDateField = By.id(locators.getProperty("visitDate"));

	By descriptionField = By.id(locators.getProperty("descriptionField"));

	By addVisitButton = By.xpath(locators.getProperty("addVisit"));

	public void fillTheFields(String visitDate, String description) {
		driver.findElement(visitDateField).sendKeys(visitDate);
		driver.findElement(descriptionField).sendKeys(description);
	}

	public void clickOnAddVisitButton() {
		driver.findElement(addVisitButton).click();
	}

	public boolean isErrorMessageDisplayedForEmptyField(String message) {
		WebElement ErrorMessage = driver.findElement(By.xpath("//*[contains(text(),'" + message + "')]"));
		wait.until(visibilityOf(ErrorMessage));
		return ErrorMessage.getText().equals(message);
	}

	public boolean isErrorMessageDisplayedForInvalidDate(String message) {
		WebElement ErrorMessage = driver.findElement(By.xpath("//*[contains(text(),'" + message + "')]"));
		wait.until(visibilityOf(ErrorMessage));
		return ErrorMessage.getText().equals(message);
	}

}
