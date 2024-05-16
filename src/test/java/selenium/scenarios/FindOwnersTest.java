package selenium.scenarios;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import selenium.TestBase;
import selenium.pages.AddOwnerPage;
import selenium.pages.FindOwnersPage;
import selenium.pages.ListOwnersPage;
import selenium.pages.OwnerPage;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FindOwnersTest extends TestBase {

	private OwnerPage ownerPage;

	private FindOwnersPage findOwnersPage;

	@Before
	public void setObjects() {
		ownerPage = new OwnerPage(driver, locators);
		findOwnersPage = new FindOwnersPage(driver, locators);
	}

	private void setupFindOwnersPage(String lastName) {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.setTextInLastNameField(lastName);
		findOwnersPage.clickOnFindOwnerButton();
	}

	@Test
	public void testFindOwnerByExistingLastName() {
		setupFindOwnersPage(input.getProperty("existingLastName"));

		assertTrue(ownerPage.isLastNameDisplayed(input.getProperty("existingLastName")));
	}

	@Test
	public void testFindOwnerByExistingFirstName() {
		setupFindOwnersPage(input.getProperty("existingFirstName"));

		String ownerNotFoundText = findOwnersPage.getOwnerNotFoundText();
		String expectedText = tap.getProperty("ownerNotFoundText");
		assertEquals(expectedText, ownerNotFoundText);
	}

	@Test
	public void testFindOwnerByNonExistingLastName() {
		setupFindOwnersPage(input.getProperty("nonExistingLastName"));

		String ownerNotFoundText = findOwnersPage.getOwnerNotFoundText();
		String expectedText = tap.getProperty("ownerNotFoundText");
		assertEquals(expectedText, ownerNotFoundText);
	}

	@Test
	public void testCaseSensitiveLastName() {
		setupFindOwnersPage(input.getProperty("caseSensitiveLastName"));

		assertTrue(ownerPage.isLastNameDisplayed(input.getProperty("existingLastName")));
	}

	@Test
	public void testEmptyLastNameField() {
		setupFindOwnersPage("");

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.tableAppearance();
	}

	@Test
	public void testNavigateToOwnerPageFromTable() {
		setupFindOwnersPage("");

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable(1);
	}

	@Test
	public void testFindNewlyAddedOwner() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnAddOwnerButton();

		String firstName = input.getProperty("firstName");
		String lastName = input.getProperty("lastName");
		String address = input.getProperty("address");
		String city = input.getProperty("city");
		String telephone = input.getProperty("telephone");

		AddOwnerPage addOwnerPage = new AddOwnerPage(driver, locators);
		addOwnerPage.setTextInFields(firstName, lastName, address, city, telephone);
		addOwnerPage.clickingOnAddOwnerButton();

		assertTrue(ownerPage.isSuccessMessageDisplayed());
		assertTrue(ownerPage.isLastNameDisplayed(lastName));

		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();
		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnDifferentPage(locators.getProperty("thirdPage"));

		List<WebElement> cells = driver.findElements(By.xpath("(//td)"));

		cells.stream()
			.filter(cell -> cell.getText().equals(firstName + " " + lastName))
			.findFirst()
			.ifPresent(WebElement::click);

		assertTrue(ownerPage.isLastNameDisplayed(lastName));
	}

}
