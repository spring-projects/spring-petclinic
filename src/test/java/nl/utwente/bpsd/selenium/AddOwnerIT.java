package nl.utwente.bpsd.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.net.MalformedURLException;

/**
 * @author Martijn
 * @since 21-6-2017.
 */
public class AddOwnerIT extends SeleniumBaseIT {
    public AddOwnerIT() throws MalformedURLException {
        super();
    }

    @Test
    @Category(SeleniumBaseIT.class)
    public void addOwnerIT() {
        driver.get(BASE_URL+"/owners/new");

        //Add an owner
        fillTextField(By.name("firstName"), "Sophie");
        fillTextField(By.name("lastName"), "Lathouwers");
        fillTextField(By.name("address"), "Homeroad 12");
        fillTextField(By.name("city"), "Enschede");
        fillTextField(By.name("telephone"), "0534890000");
        driver.findElement(By.name("telephone")).submit();
        waitForPageToLoad();
        Assert.assertTrue("Could not locate \"Sophie Lathouwers\" on the page. This is the html of the current page: "+getHTML(), pageContainsText("Sophie Lathouwers"));

        //Add a pet
        waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText("Add New Pet")));
        driver.findElement(By.linkText("Add New Pet")).click();
        fillTextField(By.name("name"), "Thumper");
        fillTextField(By.name("birthDate"), "1942/08/09");
        new Select(driver.findElement(By.name("type"))).selectByValue("hamster");
        driver.findElement(By.name("name")).submit();

        waitForPageToLoad();
        Assert.assertTrue("Could not locate \"Thumper\" on the page. This is the html of the current page: "+getHTML(),pageContainsText("Thumper"));
        setTestFinished();
    }

}
