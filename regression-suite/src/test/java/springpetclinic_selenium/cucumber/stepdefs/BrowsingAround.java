package springpetclinic_selenium.cucumber.stepdefs;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import springpetclinic_selenium.utils.Configure;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.*;

public class BrowsingAround {
	
	private HtmlUnitDriver driver = new HtmlUnitDriver();
	Configure config = new Configure();
	
	@Given("^I am on the home page$")
	public void i_am_on_the_home_page() throws Throwable {
        if(config.getZapEnabled()){
            driver.setProxy(config.getZapIp(), config.getZapPort());
        }
		driver.get(config.getPetClinicUrl());
	}

	@Then("^I should see \"(.*?)\"$")
	public void i_should_see(String arg1) throws Throwable {
		assertTrue(driver.getPageSource().contains(arg1));
	}

	@Given("^I follow \"(.*?)\"$")
	public void i_follow(String arg1) throws Throwable {
		driver.get(config.getPetClinicUrl()+arg1);
		assertTrue(driver.getCurrentUrl().equals(config.getPetClinicUrl()+arg1));
	}

	@Then("^I should be on the vets, \"(.*?)\", page$")
	public void i_should_be_on_the_vets_page(String arg1) throws Throwable {
		assertTrue(driver.findElementByXPath("/html/body/div/h2").getText().equals(arg1));
	}

	@Then("^I should see \"(.*?)\" within h2$")
	public void i_should_see_within(String arg1) throws Throwable {
		assertTrue(driver.findElementByXPath("/html/body/div/h2").getText().equals(arg1));
	}

	@When("^I fill in \"(.*?)\" with \"(.*?)\"$")
	public void i_fill_in_with(String arg1, String arg2) throws Throwable {
		driver.findElementByXPath("//input[@id='"+arg1+"']").sendKeys(arg2);
		assertTrue(driver.findElementByXPath("//input[@id='"+arg1+"']").getAttribute("id").equals(arg1));
	}

	@When("^I press \"(.*?)\"$")
	public void i_press(String arg1) throws Throwable {
		List<WebElement> buttons = driver.findElements(By.tagName("button"));
		for (WebElement webElement : buttons) {
			webElement.submit();
			assertTrue(true);
		}
	}
}
