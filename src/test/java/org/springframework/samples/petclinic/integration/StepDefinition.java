package org.springframework.samples.petclinic.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinition extends SpringIntegrationTest {

	private final WebDriver webDriver = Browser.webDriver;

	@Given("^I go to the main page$")
	public void mainPage() {
		webDriver.navigate().to("http://localhost:8080");
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

}
