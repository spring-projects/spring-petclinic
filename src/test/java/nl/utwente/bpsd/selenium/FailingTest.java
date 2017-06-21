package nl.utwente.bpsd.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;

/**
 * @author Martijn
 * @since 21-6-2017.
 */
public class FailingTest extends SeleniumBaseTest {
    public FailingTest() throws MalformedURLException {
        super();
    }

    @Test
    @Category(SeleniumBaseTest.class)
    public void failTest() {
	Assert.fail();
    }

}
