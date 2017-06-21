package nl.utwente.bpsd.selenium;

import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Martijn
 * @since 21-6-2017.
 */
public class SeleniumBaseTest {
    protected final RemoteWebDriver driver;
    public static final String BASE_URL = "http://pet-clinic:8080/";
//    public static final String BASE_URL = "http://localhost:8080/";

    public SeleniumBaseTest() throws MalformedURLException {
//		System.setProperty("webdriver.chrome.driver","C:\\Users\\marti\\Downloads\\chromedriver_win32\\chromedriver.exe");
//        this.driver = new ChromeDriver();
        this.driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), DesiredCapabilities.firefox());
        driver.get(BASE_URL);
    }

    public void fillTextField(By by, String text){
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(text);
    }

    @After
    public void afterTest() {
        driver.close();
    }


    protected boolean pageContainsText(String text) {
        return driver.findElementsByXPath("//*[text()='"+text+"']").size() == 1;
    }
}
