package selenium.scenarios;

import jdk.jfr.Description;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import selenium.TestBase;
import selenium.pages.*;

import static org.testng.AssertJUnit.assertTrue;

public class TS04AddVisitTest extends TestBase {

	private OwnerPage ownerPage;

	private FindOwnersPage findOwnersPage;

	private AddVisitPage addVisitPage;

	@BeforeMethod
	public void setObjects() {
		ownerPage = new OwnerPage(driver, locators);
		findOwnersPage = new FindOwnersPage(driver, locators);
		addVisitPage = new AddVisitPage(driver, locators);
	}

	public void addVisit(String date, String description) {
		String dateText = input.getProperty(date);
		String descriptionText = input.getProperty(description);
		addVisitPage.fillTheFields(dateText, descriptionText);
		addVisitPage.clickOnAddVisitButton();
	}

	public void navigateToVisitPage() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();

		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable();
		ownerPage.clickOnAddVisitButton();
	}

	// Spelling mistake in success message - Report
	@Test
	@Description("Validate adding a visit for a pet")
	public void testAddVisitForPet() {
		navigateToVisitPage();

		addVisit("date", "description");
		assertTrue(ownerPage.isVisitAddedMessageDisplayed());
		assertTrue(ownerPage.isVisitAdded("description"));
	}

	@Test(priority = 1)
	@Description("Validate adding a visit without filling any of the fields")
	public void testVisitEmptyFields() {
		navigateToVisitPage();

		addVisitPage.clickOnAddVisitButton();
		String expectedErrorMessage = tap.getProperty("emptyFieldInAddVisit");
		assertTrue(addVisitPage.isErrorMessageDisplayedForEmptyField(expectedErrorMessage));
	}

	@Test(priority = 2)
	@Description("Validate adding a visit with an invalid date")
	public void testInvalidDate() {
		navigateToVisitPage();

		addVisit("invalidDate", "description");
		String expectedErrorMessage = tap.getProperty("invalidDateInAddVisit");
		assertTrue(addVisitPage.isErrorMessageDisplayedForInvalidDate(expectedErrorMessage));
	}

}
