package org.springframework.samples.petclinic.integration;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StepDefinition extends SpringIntegrationTest {

	public static final String BASE_URL = "http://localhost:8080";

	private final WebDriver webDriver = Browser.webDriver;

	@Given("^I go to the main page$")
	public void mainPage() {
		webDriver.navigate().to(BASE_URL);
	}

	@Given("^I go to the find-owners page$")
	public void findOwnersPage() {
		webDriver.navigate().to(BASE_URL + "/owners/find");
	}

	@When("^I click on the link with title \"([^\"]*)\"$")
	public void clickOnLinkWithTitle(String linkTitle) {
		By elementSelector = By.xpath(String.format("//*[@title='%s']", linkTitle));
		new WebDriverWait(webDriver, 60L).until(driver -> driver.findElement(elementSelector).isDisplayed());
		WebElement webElement = webDriver.findElement(elementSelector);
		webElement.click();
	}

	@Then("^I should see the \"([^\"]*)\" page$")
	public void shouldSeeThePage(String pageTitle) {
		By elementSelector = By.tagName("h2");
		new WebDriverWait(webDriver, 60L).until(driver -> driver.findElement(elementSelector).isDisplayed());
		WebElement webElement = webDriver.findElement(elementSelector);
		assertThat(webElement.getText()).isEqualTo(pageTitle);
	}

	@When("^I fill the field named \"([^\"]*)\" with value \"([^\"]*)\"$")
	public void fillInputBoxWithValue(String name, String value) {
		By elementSelector = By.name(name);
		WebElement webElement = webDriver.findElement(elementSelector);
		webElement.sendKeys(value);
	}

	@When("^I submit the form \"([^\"]*)\"$")
	public void submitForm(String id) {
		By elementSelector = By.id(id);
		WebElement webElement = webDriver.findElement(elementSelector);
		webElement.submit();
	}


	@Then("I should see {string} error")
	public void shouldSeeErrorOnFindOwnerPage(String error) {
		By elementSelector = By.tagName("p");
		new WebDriverWait(webDriver, 60L).until(driver -> driver.findElement(elementSelector).isDisplayed());
		WebElement webElement = webDriver.findElement(elementSelector);
		assertThat(webElement.getText()).isEqualTo(error);
	}


	@Given("I go to add owner page")
	public void goToAddOwnerPage() {
		webDriver.navigate().to(BASE_URL + "/owners/new");
	}

	@Then("I should see {string} error message")
	public void shouldSeeErrorMessageOnAddOwnerPage(String error) {
		By elementSelector = By.xpath("//span[@class='help-inline']");
		new WebDriverWait(webDriver, 60L).until(driver -> driver.findElement(elementSelector).isDisplayed());
		WebElement webElement = webDriver.findElement(elementSelector);
		assertThat(webElement.getText()).contains(error);

	}

	@When("I click on the link with heading {string}")
	public void clickOnTheLinkWithHeading(String heading) {
		By elementSelector = By.xpath(String.format("//*[text()='%s']", heading));
		new WebDriverWait(webDriver, 60L).until(driver -> driver.findElement(elementSelector).isDisplayed());
		WebElement webElement = webDriver.findElement(elementSelector);
		webElement.click();
	}

	@Then("I should see {string} exception")
	public void shouldSeeException(String exception) {

		By elementSelector = By.tagName("h2");
		new WebDriverWait(webDriver, 60L).until(driver -> driver.findElement(elementSelector).isDisplayed());
		WebElement webElement = webDriver.findElement(elementSelector);
		assertThat(webElement.getText()).contains(exception);
	}


	@Then("I should see owner named {string}")
	public void shouldSeeOwnerInformationOnOwnerPage(String lastName) {
		By elementSelector = By.xpath("//table[@class='table table-striped']//b");
		new WebDriverWait(webDriver, 60L).until(driver -> driver.findElement(elementSelector).isDisplayed());
		WebElement webElement = webDriver.findElement(elementSelector);
		assertThat(webElement.getText()).contains(lastName);
	}

	@Then("I should see owners named {string}")
	public void shouldSeeMultipleOwnersInformationOnOwnersPage(String lastName) {
		By elementSelector = By.xpath("//table[@id='owners']//a");
		new WebDriverWait(webDriver, 60L).until(driver -> driver.findElement(elementSelector).isDisplayed());
		WebElement webElement = webDriver.findElement(elementSelector);
		assertThat(webElement.getText()).contains(lastName);
	}


}
