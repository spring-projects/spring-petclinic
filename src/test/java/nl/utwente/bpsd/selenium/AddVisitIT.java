package nl.utwente.bpsd.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Martijn
 * @since 21-6-2017.
 */
public class AddVisitIT extends SeleniumBaseIT {
    public AddVisitIT() throws MalformedURLException {
        super();
    }

    @Test
    @Category(SeleniumBaseIT.class)
    public void addOwnerTest() {
	driver.findElement(By.name("lastName")).submit();
	
	//Go to first owner
	WebElement table = driver.findElement(By.tagName("table"));
	List<WebElement> cells = table.findElements(By.xpath("tr/td"));
	cells.get(0).findElement(By.xpath("a")).click();

	//Go to edit page of first pet
	driver.findElement(By.xpath("//table//tr/td/table/tbody//a[text()='Edit Pet']")).click();

	//Edit Name of pet
	fillTextField(By.name("name"), "foobar");
	driver.findElement(By.name("name")).submit();

	Assert.assertNotNull(driver.findElement(By.xpath("//table//tr/td/dl/dd/[contains(text(), 'foobar')]")));
    }

}
