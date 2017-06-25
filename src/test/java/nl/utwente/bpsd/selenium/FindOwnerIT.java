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
        waitFor(new FixedPeriod(1000));
        waitForPageToLoad();
        Assert.assertTrue("Could not find \"Jean Coleman\" on the current page. This is the html of the current page: "+getHTML(),driver.findElements(By.xpath("//*[text()='Jean Coleman']")).size() == 1);
        setTestFinished();
    }

}
