package selenium.scenarios;

import org.junit.Before;
import org.junit.Test;
import selenium.TestBase;
import selenium.pages.FindOwnersPage;
import selenium.pages.HomePage;

import static org.junit.Assert.assertTrue;

public class HomePageTest extends TestBase {

	private HomePage homePage;

	private FindOwnersPage findOwnersPage;

	@Before
	public void setObjects() {
		homePage = new HomePage(driver, locators);
		findOwnersPage = new FindOwnersPage(driver, locators);
	}

	public void checkForCorrectWelcomePhoto() {
		String expectedPhotoSrc = tap.getProperty("welcomePhoto");
		assertTrue(homePage.isWelcomePhotoVisible(expectedPhotoSrc));
	}

	@Test
	public void testNavigateHomeFromHeader() {
		findOwnersPage.navigateToFindOwnersPage();

		homePage.clickOnHomePageLink();
		checkForCorrectWelcomePhoto();
	}

	@Test
	public void navigateHomeFromLogo() {
		findOwnersPage.navigateToFindOwnersPage();

		homePage.clickOnLogoLink();
		checkForCorrectWelcomePhoto();
	}

}
