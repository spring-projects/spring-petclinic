package selenium.scenarios;

import org.junit.Before;
import org.junit.Test;
import selenium.TestBase;
import selenium.pages.*;

import static org.junit.Assert.assertTrue;

public class AddVisitTest extends TestBase {

	private OwnerPage ownerPage;

	private FindOwnersPage findOwnersPage;

	private AddVisitPage addVisitPage;

	@Before
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
		listOwnersPage.clickOnNameFromTable(1);
		ownerPage.clickOnAddVisitButton();
	}

	@Test
	public void testAddVisitForPet() {
		navigateToVisitPage();

		addVisit("date", "description");
		assertTrue(ownerPage.isVisitAddedMessageDisplayed());
		assertTrue(ownerPage.isVisitAdded("description"));
	}

	@Test
	public void testVisitEmptyFields() {
		navigateToVisitPage();

		addVisitPage.clickOnAddVisitButton();
		String expectedErrorMessage = tap.getProperty("emptyFieldInAddVisit");
		assertTrue(addVisitPage.isErrorMessageDisplayedForEmptyField(expectedErrorMessage));
	}

	@Test
	public void testInvalidDate() {
		navigateToVisitPage();

		addVisit("invalidDate", "description");
		String expectedErrorMessage = tap.getProperty("invalidDateInAddVisit");
		assertTrue(addVisitPage.isErrorMessageDisplayedForInvalidDate(expectedErrorMessage));
	}

}
