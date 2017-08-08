package springpetclinic_selenium.cucumber.stepdefs;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import springpetclinic_selenium.model.Owner;
import springpetclinic_selenium.utils.Configure;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

public class AddOwner {
 
	private Configure config = new Configure();
	private HtmlUnitDriver driver = new HtmlUnitDriver();
	private Owner owner = config.getOwner();


    @Given("^a User wishes to add a Pet Owner to the Petclinic project\\.$")
    public void a_User_wishes_to_add_a_Pet_Owner_to_the_Petclinic_project() throws Throwable {
    	 assertTrue(owner != null);
    }
    
    @When("^the User enters the pet Owners details$")
    public void the_User_enters_the_pet_Owners_details() throws Throwable {
    	 // Express the Regexp above with the code you wish you had
        if(config.getZapEnabled()){
            driver.setProxy(config.getZapIp(), config.getZapPort());
        }
        driver.get(config.getPetClinicUrl()+"/owners/find.html");
        driver.findElement(By.xpath("/html/body/div/a")).click();
        driver.findElement(By.xpath("//input[@id='firstName']")).sendKeys(config.getOwner().getFirstName());
        driver.findElement(By.xpath("//input[@id='lastName']")).sendKeys(config.getOwner().getLastName());
        driver.findElement(By.xpath("//input[@id='address']")).sendKeys(config.getOwner().getAddress());
        driver.findElement(By.xpath("//input[@id='city']")).sendKeys(config.getOwner().getAddress());
        driver.findElement(By.xpath("//input[@id='telephone']")).sendKeys(config.getOwner().getTelephone());
        driver.findElement(By.xpath("//*[@id='add-owner-form']/div[6]/button")).submit();
    }

    @Then("^the Pet Owner \"(.*?)\" should be added to the Petclinic Project\\.$")
    public void the_Pet_Owner_should_be_added_to_the_Petclinic_Project(String arg1) throws Throwable {
    	assertTrue(driver.findElement(By.xpath("/html/body/div/table[1]/tbody/tr[1]/td/b")).getText().equals(arg1));
    }
}