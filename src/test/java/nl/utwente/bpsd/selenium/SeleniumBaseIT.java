package nl.utwente.bpsd.selenium;

import org.junit.After;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 *
 * @author Martijn
 * @since 21-6-2017.
 */
public class SeleniumBaseIT {
    protected final WebDriver driver;
    private boolean testFinished = false;
    public static final String BASE_URL = "http://pet-clinic:8080/";
//    public static final String BASE_URL = "http://localhost:8080/";

    public SeleniumBaseIT() throws MalformedURLException {
//		System.setProperty("webdriver.chrome.driver","C:\\Users\\marti\\Downloads\\chromedriver_win32\\chromedriver.exe");
//        this.driver = new ChromeDriver();
        this.driver = new Augmenter().augment(new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), DesiredCapabilities.firefox()));
        driver.get(BASE_URL);
    }

    public void fillTextField(By by, String text){
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(text);
    }

    protected void setTestFinished() {
        testFinished = true;
    }

    @After
    public void afterTest() {
        if (!testFinished) {
            if (driver instanceof TakesScreenshot) {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Logger.getLogger(this.getClass().getName()).warning(screenshot.getAbsolutePath());
            }
        }
        driver.close();
    }


    protected boolean pageContainsText(String text) {
        return driver.findElements(By.xpath("//*[text()='"+text+"']")).size() == 1;
    }

    public String getHTML() {
        return driver.findElement(By.xpath("//html")).getAttribute("innerHTML");
    }

    protected void waitForPageToLoad() {
        waitFor(new FixedPeriod(333));
        waitFor(new PageLoadedExpectedCondition());
    }

    protected void waitFor(ExpectedCondition<?> condition) {
        new WebDriverWait(driver, 3).until(condition);
    }

    private class PageLoadedExpectedCondition implements ExpectedCondition<Boolean> {

        @Override
        public Boolean apply(WebDriver webDriver) {
            if (webDriver instanceof JavascriptExecutor) {
                return ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete");
            }
            throw new ClassCastException("This webdriver is not able to execute javascript.");
        }
    }

    protected class FixedPeriod implements ExpectedCondition<Boolean> {
        private final int time;

        public FixedPeriod(int timeInMilliseconds) {
            this.time = timeInMilliseconds;
        }

        @Override
        public Boolean apply(WebDriver webDriver) {
            try {
                Thread.sleep(time);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
