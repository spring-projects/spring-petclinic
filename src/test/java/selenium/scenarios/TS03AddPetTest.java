package selenium.scenarios;

import jdk.jfr.Description;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import selenium.TestBase;
import selenium.pages.*;

import static org.testng.AssertJUnit.*;

public class TS03AddPetTest extends TestBase {

	private AddOwnerPage addOwnerPage;

	private OwnerPage ownerPage;

	private FindOwnersPage findOwnersPage;

	private AddPetPage addPetPage;

	@BeforeMethod
	public void setObjects() {
		addOwnerPage = new AddOwnerPage(driver, locators);
		ownerPage = new OwnerPage(driver, locators);
		findOwnersPage = new FindOwnersPage(driver, locators);
		addPetPage = new AddPetPage(driver, locators);
	}

	public void addOrEditPet(@NotNull String action, String petName, String birthDate, String petType) {
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

	public void navigateToAddPetForExistingOwner() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable();
		ownerPage.clickOnAddNewPetButton();
	}

	@Test
	@Description("Validate adding a pet to a newly added owner")
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

	@Test(priority = 1)
	@Description("Validate adding a new pet to an already existing owner")
	public void testAddPetToExistingOwner() {
		navigateToAddPetForExistingOwner();

		addOrEditPet("add", "petName2", "birthDate2", "petType2");
		assertTrue(ownerPage.isPetAddedSuccessMessageDisplayed());
		assertTrue(ownerPage.isPetNameDisplayed("petName2"));
	}

	@Test(priority = 2)
	@Description("Validate adding the same pet twice")
	public void testAddingSamePetTwice() {
		navigateToAddPetForExistingOwner();

		addOrEditPet("add", "petName3", "birthDate3", "petType3");
		driver.navigate().back();
		addPetPage.clickOnAddPetButton();

		assertTrue(addPetPage.isErrorMessageDisplayedForSamePetName());
	}

	@Test(priority = 3)
	@Description("Validate adding a pet without filling any of the fields")
	public void testAddPetWithEmptyFields() {
		navigateToAddPetForExistingOwner();

		addPetPage.clickOnAddPetButton();
		String expectedErrorMessage = tap.getProperty("emptyPetFieldsErrorMessage");
		assertTrue(addPetPage.isErrorMessageDisplayedForEmptyFields(expectedErrorMessage));
	}

	@Test(priority = 4)
	@Description("Validate adding a pet with a future birth date")
	public void testAddPetWithFutureBirthDate() {
		navigateToAddPetForExistingOwner();

		addOrEditPet("add", "petName4", "futureBirthDate", "petType4");
		assertTrue(addPetPage.isInvalidDateErrorMessageDisplayed());
	}

	// Pet is still added after putting numbers in 'Name' field - REPORT DEFECT!!!
	@Test(priority = 5)
	@Description("Validate adding numbers in the 'Name' field")
	public void testAddNumbersInNameField() {
		navigateToAddPetForExistingOwner();

		addOrEditPet("add", "numberPetName", "birthDate5", "petType5");
		assertFalse(ownerPage.isPetAddedSuccessMessageDisplayed());
	}

	@Test(priority = 6)
	@Description("Validate updating a pet")
	public void testUpdatePet() {
		findOwnersPage.navigateToFindOwnersPage();
		findOwnersPage.clickOnFindOwnerButton();

		ListOwnersPage listOwnersPage = new ListOwnersPage(driver, locators);
		listOwnersPage.clickOnNameFromTable();
		ownerPage.clickOnEditPetButton();
		addPetPage.clearFields();

		addOrEditPet("update", "petName", "birthDate", "petType");
		assertTrue(ownerPage.isUpdatePetMessageDisplayed());
		assertTrue(ownerPage.isPetNameDisplayed("petName"));
	}

}
