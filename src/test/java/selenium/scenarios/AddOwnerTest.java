package selenium.scenarios;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import selenium.TestBase;
import selenium.pages.AddOwnerPage;
import selenium.pages.FindOwnersPage;
import selenium.pages.ListOwnersPage;
import selenium.pages.OwnerPage;

import static org.junit.Assert.*;

public class AddOwnerTest extends TestBase {

	private AddOwnerPage addOwnerPage;

	private OwnerPage ownerPage;

	private FindOwnersPage findOwnersPage;

	@Before
	public void setObjects() {
		addOwnerPage = new AddOwnerPage(driver, locators);
		ownerPage = new OwnerPage(driver, locators);
		findOwnersPage = new FindOwnersPage(driver, locators);
	}

	public void navigateToAddOwner() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnAddOwnerButton();
	}

	public void addOrEditAnOwner(String action, String firstName, String lastName, String address, String city,
			String telephone) {
		String firstNameText = input.getProperty(firstName);
		String lastNameText = input.getProperty(lastName);
		String addressText = input.getProperty(address);
		String cityText = input.getProperty(city);
		String telephoneText = input.getProperty(telephone);

		addOwnerPage.setTextInFields(firstNameText, lastNameText, addressText, cityText, telephoneText);
		if (action.equalsIgnoreCase("add")) {
			addOwnerPage.clickingOnAddOwnerButton();
		}
		else if (action.equalsIgnoreCase("update")) {
			addOwnerPage.clickOnUpdateOwnerButton();
		}
	}

	@Test
	public void testAddingAnOwner() {
		navigateToAddOwner();
		addOrEditAnOwner("add", "firstName", "lastName", "address", "city", "telephone");

		assertTrue(ownerPage.isSuccessMessageDisplayed());
		assertTrue(ownerPage.isLastNameDisplayed(input.getProperty("lastName")));
	}

	@Test
	public void testEmptyFields() {
		navigateToAddOwner();
		addOwnerPage.clickingOnAddOwnerButton();

		String expectedErrorMessage = tap.getProperty("errorMessage");
		assertTrue(addOwnerPage.isErrorMessageDisplayedForEmptyFields(expectedErrorMessage));
	}

	// User is still created after putting numbers in the name fields - REPORT DEFECT!!!
	@Test
	@Ignore("Disabled due to defect")
	public void testNumbersInNameFields() {
		navigateToAddOwner();

		addOrEditAnOwner("add", "numbersInFirstName", "numbersInLastName", "address", "city", "telephone");

		assertFalse("Message should not be visible", ownerPage.isSuccessMessageDisplayed());
	}

	// You can add the same owner twice - REPORT DEFECT!!!
	@Test
	@Ignore("Disabled due to defect")
	public void testCreateSameOwnerTwice() {
		navigateToAddOwner();

		addOrEditAnOwner("add", "firstName2", "lastName2", "address2", "city2", "telephone2");

		driver.navigate().back();
		addOwnerPage.clickingOnAddOwnerButton();
		assertFalse("Message should not be visible", ownerPage.isSuccessMessageDisplayed());
	}

	@Test
	public void testTextInTelephoneField() {
		navigateToAddOwner();

		addOrEditAnOwner("add", "firstName", "lastName", "address", "city", "textInTelephoneField");

		String expectedErrorMessage = tap.getProperty("errorMessageTelephoneField");
		assertTrue("Error message should be displayed for invalid telephone number",
				addOwnerPage.isErrorMessageDisplayedForTextInTelephoneField(expectedErrorMessage));
	}

	@Test
	public void testUpdateOwner() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable(2);

		ownerPage.clickOnEditOwnerButton();
		addOwnerPage.clearFields();

		addOrEditAnOwner("update", "updateFirstName", "updateLastName", "updateAddress", "updateCity",
				"updateTelephone");

		assertTrue(ownerPage.isUpdateMessageDisplayed());
		assertTrue(ownerPage.isLastNameDisplayed(input.getProperty("updateLastName")));
	}

	// User can still be updated - REPORT DEFECT!!!
	@Test
	@Ignore("Disabled due to defect")
	public void testUpdateOwnerWithSameDetailsFromOtherOwner() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable(3);

		ownerPage.clickOnEditOwnerButton();
		addOwnerPage.clearFields();

		addOrEditAnOwner("update", "firstName", "lastName", "address", "city", "telephone");

		assertFalse("Error message is not displayed", ownerPage.isUpdateMessageDisplayed());
	}

	@Test
	public void testUpdateNewlyAddedOwner() {
		navigateToAddOwner();
		addOrEditAnOwner("add", "firstName3", "lastName3", "address3", "city3", "telephone3");

		ownerPage.clickOnEditOwnerButton();
		addOwnerPage.clearFields();

		addOrEditAnOwner("update", "updateFirstName2", "updateLastName2", "updateAddress2", "updateCity2",
				"updateTelephone2");

		assertTrue(ownerPage.isUpdateMessageDisplayed());
		assertTrue(ownerPage.isLastNameDisplayed(input.getProperty("updateLastName2")));
	}

}
