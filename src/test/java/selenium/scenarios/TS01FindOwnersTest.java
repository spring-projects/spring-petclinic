package selenium.scenarios;

import jdk.jfr.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import selenium.TestBase;
import selenium.pages.AddOwnerPage;
import selenium.pages.FindOwnersPage;
import selenium.pages.ListOwnersPage;
import selenium.pages.OwnerPage;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TS01FindOwnersTest extends TestBase {

	private OwnerPage ownerPage;

	private FindOwnersPage findOwnersPage;

	@BeforeMethod
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
	@Description("Validate if the correct owner is displayed after searching by an existing last name")
	public void testFindOwnerByExistingLastName() {
		setupFindOwnersPage(input.getProperty("existingLastName"));

		assertTrue(ownerPage.isLastNameDisplayed(input.getProperty("existingLastName")),
				"The last name should be displayed on the Owner page");
	}

	@Test(priority = 1)
	@Description("Validate if an owner is displayed after searching by an existing first name")
	public void testFindOwnerByExistingFirstName() {
		setupFindOwnersPage(input.getProperty("existingFirstName"));

		String ownerNotFoundText = findOwnersPage.getOwnerNotFoundText();
		String expectedText = tap.getProperty("ownerNotFoundText");
		assertEquals(ownerNotFoundText, expectedText, "Expected text has been displayed");
	}

	@Test(priority = 2)
	@Description("Validate if an owner is displayed after searching for a non-existing last name")
	public void testFindOwnerByNonExistingLastName() {
		setupFindOwnersPage(input.getProperty("nonExistingLastName"));

		String ownerNotFoundText = findOwnersPage.getOwnerNotFoundText();
		String expectedText = tap.getProperty("ownerNotFoundText");
		assertEquals(ownerNotFoundText, expectedText, "Expected text has been displayed");
	}

	@Test(priority = 3)
	@Description("Validate case sensitivity after searching by an existing last name")
	public void testCaseSensitiveLastName() {
		setupFindOwnersPage(input.getProperty("caseSensitiveLastName"));

		assertTrue(ownerPage.isLastNameDisplayed(input.getProperty("existingLastName")),
				"The last name should be displayed on the Owner page");
	}

	@Test(priority = 4)
	@Description("Validate if all owners are displayed after clicking the 'Find Owner' button "
			+ "without filling the 'Last name' field")
	public void testEmptyLastNameField() {
		setupFindOwnersPage("");

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.tableAppearance();
	}

	@Test(priority = 5)
	@Description("Validate if you can navigate to the Owner page after clicking on a name from the table")
	public void testNavigateToOwnerPageFromTable() {
		setupFindOwnersPage("");

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable();
	}

	@Test(priority = 6)
	@Description("Validate finding a newly added owner")
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
