package selenium.scenarios;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import selenium.TestBase;
import selenium.pages.*;

import static org.junit.Assert.*;

public class AddPetTest extends TestBase {

	private AddOwnerPage addOwnerPage;

	private OwnerPage ownerPage;

	private FindOwnersPage findOwnersPage;

	private AddPetPage addPetPage;

	@Before
	public void setObjects() {
		addOwnerPage = new AddOwnerPage(driver, locators);
		ownerPage = new OwnerPage(driver, locators);
		findOwnersPage = new FindOwnersPage(driver, locators);
		addPetPage = new AddPetPage(driver, locators);
	}

	public void addOrEditPet(String action, String petName, String birthDate, String petType) {
		String petNameText = input.getProperty(petName);
		String petBirthDateText = input.getProperty(birthDate);
		String petTypeOption = input.getProperty(petType);

		addPetPage.fillTheFields(petNameText, petBirthDateText, petTypeOption);
		if (action.equalsIgnoreCase("add")) {
			addPetPage.clickOnAddPetButton();
		}
		else if (action.equalsIgnoreCase("update")) {
			addPetPage.clickOnUpdatePetButton();
		}
	}

	public void navigateToAddPetForExistingOwner(int row) {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable(row);
		ownerPage.clickOnAddNewPetButton();
	}

	@Test
	public void testAddPetToNewlyAddedOwner() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnAddOwnerButton();

		String firstName = input.getProperty("firstName");
		String lastName = input.getProperty("lastName");
		String address = input.getProperty("address");
		String city = input.getProperty("city");
		String telephone = input.getProperty("telephone");
		addOwnerPage.setTextInFields(firstName, lastName, address, city, telephone);
		addOwnerPage.clickingOnAddOwnerButton();

		ownerPage.clickOnAddNewPetButton();

		addOrEditPet("add", "petName", "birthDate", "petType");
		assertTrue(ownerPage.isPetAddedSuccessMessageDisplayed());
		assertTrue(ownerPage.isPetNameDisplayed("petName"));
	}

	@Test
	public void testAddPetToExistingOwner() {
		navigateToAddPetForExistingOwner(4);

		addOrEditPet("add", "petName2", "birthDate2", "petType2");
		assertTrue(ownerPage.isPetAddedSuccessMessageDisplayed());
		assertTrue(ownerPage.isPetNameDisplayed("petName2"));
	}

	@Test
	public void testAddingSamePetTwice() {
		navigateToAddPetForExistingOwner(1);

		addOrEditPet("add", "petName3", "birthDate3", "petType3");
		driver.navigate().back();
		addPetPage.clickOnAddPetButton();

		assertTrue(addPetPage.isErrorMessageDisplayedForSamePetName());
	}

	@Test
	public void testAddPetWithEmptyFields() {
		navigateToAddPetForExistingOwner(1);

		addPetPage.clickOnAddPetButton();
		String expectedErrorMessage = tap.getProperty("emptyPetFieldsErrorMessage");
		assertTrue(addPetPage.isErrorMessageDisplayedForEmptyFields(expectedErrorMessage));
	}

	@Test
	public void testAddPetWithFutureBirthDate() {
		navigateToAddPetForExistingOwner(1);

		addOrEditPet("add", "petName4", "futureBirthDate", "petType4");
		assertTrue(addPetPage.isInvalidDateErrorMessageDisplayed());
	}

	// Pet is still added after putting numbers in 'Name' field - REPORT DEFECT!!!
	@Test
	@Ignore("Disabled due to defect")
	public void testAddNumbersInNameField() {
		navigateToAddPetForExistingOwner(1);

		addOrEditPet("add", "numberPetName", "birthDate5", "petType5");
		assertFalse(ownerPage.isPetAddedSuccessMessageDisplayed());
	}

	@Test
	public void testUpdatePet() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable(1);
		ownerPage.clickOnEditPetButton();
		addPetPage.clearFields();

		addOrEditPet("update", "petName", "birthDate", "petType");
		assertTrue(ownerPage.isUpdatePetMessageDisplayed());
		assertTrue(ownerPage.isPetNameDisplayed("petName"));
	}

}
