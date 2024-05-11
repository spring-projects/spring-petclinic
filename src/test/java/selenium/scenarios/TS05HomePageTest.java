package selenium.scenarios;

import org.springframework.context.annotation.Description;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import selenium.TestBase;
import selenium.pages.FindOwnersPage;
import selenium.pages.HomePage;

import static org.testng.AssertJUnit.assertTrue;

public class TS05HomePageTest extends TestBase {

	private HomePage homePage;

	private FindOwnersPage findOwnersPage;

	@BeforeMethod
	public void setObjects() {
		homePage = new HomePage(driver, locators);
		findOwnersPage = new FindOwnersPage(driver, locators);
	}

	public void checkForCorrectWelcomePhoto() {
		String expectedPhotoSrc = tap.getProperty("welcomePhoto");
		assertTrue(homePage.isWelcomePhotoVisible(expectedPhotoSrc));
	}

	@Test
	@Description("Validate if you can navigate to home page by clicking on the 'Home' header")
	public void testNavigateHomeFromHeader() {
		findOwnersPage.navigateToFindOwnersPage();

		homePage.clickOnHomePageLink();
		checkForCorrectWelcomePhoto();
	}

	@Test(priority = 1)
	@Description("Validate if you can navigate to home by page by clicking on the logo of the application")
	public void navigateHomeFromLogo() {
		findOwnersPage.navigateToFindOwnersPage();

		homePage.clickOnLogoLink();
		checkForCorrectWelcomePhoto();
	}

}
