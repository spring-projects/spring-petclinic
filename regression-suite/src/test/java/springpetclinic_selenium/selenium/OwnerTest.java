package springpetclinic_selenium.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import springpetclinic_selenium.utils.Configure;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for Spring Petclinic.
 */
public class OwnerTest 
    extends TestCase
{
    private HtmlUnitDriver driver = new HtmlUnitDriver();
    private Configure config = new Configure();
    
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OwnerTest( String testName )
    {
        super(testName);
        if(config.getZapEnabled()){
        	driver.setProxy(config.getZapIp(), config.getZapPort());
        }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( OwnerTest.class );
    }

    /**
     * Test searching for an owner that does not exist.
     */
    public void testNotFindOwner()
    {
        driver.get(config.getPetClinicUrl()+"/owners/find.html");
        System.out.println(config.getPetClinicUrl()+"/owners/find.html");
        driver.findElement(By.xpath("//input[@id='lastName']")).sendKeys(config.getOwnerLastNameNotExist());
        driver.findElement(By.xpath("//*[@id='search-owner-form']/fieldset/div[2]/button")).submit();
        assertTrue(driver.findElement(By.xpath("//*[@id='owner.errors']")).getText().equals("has not been found"));
    }
    
    /**
     * Test adding owner.
     */
    public void testAddOwner()
    {
        driver.get(config.getPetClinicUrl()+"/owners/find.html");
        driver.findElement(By.xpath("/html/body/div/a")).click();
        driver.findElement(By.xpath("//input[@id='firstName']")).sendKeys(config.getOwner().getFirstName());
        driver.findElement(By.xpath("//*[@id='lastName']")).sendKeys(config.getOwner().getLastName());
        driver.findElement(By.xpath("//input[@id='address']")).sendKeys(config.getOwner().getAddress());
        driver.findElement(By.xpath("//input[@id='city']")).sendKeys(config.getOwner().getAddress());
        driver.findElement(By.xpath("//input[@id='telephone']")).sendKeys(config.getOwner().getTelephone());
        driver.findElement(By.xpath("//*[@id='add-owner-form']/div[6]/button")).submit();
        assertTrue(driver.findElement(By.xpath("/html/body/div/table[1]/tbody/tr[1]/td/b")).getText().equals("John Smith"));
    }
}
