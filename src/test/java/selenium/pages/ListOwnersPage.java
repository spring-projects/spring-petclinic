package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.TestBase;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class ListOwnersPage extends TestBase {

	public ListOwnersPage(WebDriver driver, Properties loc) {
		TestBase.driver = driver;
		TestBase.locators = loc;
	}

	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

	By table = By.id(locators.getProperty("tableId"));

	WebElement tableElement = driver.findElement(table);

	List<WebElement> rows = tableElement.findElements(By.tagName("tr"));

	public void tableAppearance() {
		wait.until(visibilityOf(tableElement));

		assertTrue(tableElement.isDisplayed(), "Table is displayed");
		assertEquals(rows.size(), 6, "Expected 6 rows in the table");

		String owner1 = driver.findElement(By.xpath("(//tr)[2]")).getText();
		String owner2 = driver.findElement(By.xpath("(//tr)[3]")).getText();

		assertEquals(rows.get(1).getText(), owner1, "Incorrect data in row 1");
		assertEquals(rows.get(2).getText(), owner2, "Incorrect data in row 2");
	}

	public void clickOnNameFromTable(int row) {
		WebElement firstRow = rows.get(row);
		List<WebElement> name = firstRow.findElements(By.tagName("td"));
		WebElement firstName = name.get(0);
		String firstNameText = firstName.getText();
		firstName.click();

		WebElement ownerName = driver.findElement(By.xpath("(//td)[1]"));
		String ownerNameText = ownerName.getText();
		assertEquals(ownerNameText, firstNameText);
	}

	public void clickOnDifferentPage(String page) {
		driver.findElement(By.xpath(page)).click();
	}

}
