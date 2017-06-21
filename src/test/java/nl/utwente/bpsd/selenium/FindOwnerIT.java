package nl.utwente.bpsd.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

import java.net.MalformedURLException;

/**
 *
 * @author Martijn
 * @since 21-6-2017.
 */
public class FindOwnerIT extends SeleniumBaseIT {
    public FindOwnerIT() throws MalformedURLException {
        super();
    }

    @Test
    @Category(SeleniumBaseIT.class)
    public void findOwnerIT() {
        driver.get(BASE_URL+"/owners/find");
        fillTextField(By.name("lastName"),"Coleman");
        driver.findElement(By.name("lastName")).submit();
        Assert.assertTrue(driver.findElementsByXPath("//*[text()='Jean Coleman']").size() == 1);
    }
}
