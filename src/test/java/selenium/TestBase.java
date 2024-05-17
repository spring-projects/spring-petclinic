package selenium;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TestBase {

	public static WebDriver driver;

	public static Properties prop = new Properties();

	public static Properties locators = new Properties();

	public static Properties tap = new Properties();

	public static Properties input = new Properties();

	public static FileReader frProp;

	public static FileReader frLoc;

	public static FileReader frTap;

	public static FileReader frInput;

	@Before
	public void setUp() throws IOException {
		String userDirectory = System.getProperty("user.dir");
		String basePath = "\\src\\test\\java\\selenium\\config\\";
		String propPath = basePath + "config.properties";
		String locPath = basePath + "locators.properties";
		String tapPath = basePath + "textsAndPhotos.properties";
		String inputPath = basePath + "input.properties";

		if (driver == null) {
			frProp = new FileReader(userDirectory + propPath);
			frLoc = new FileReader(userDirectory + locPath);
			frTap = new FileReader(userDirectory + tapPath);
			frInput = new FileReader(userDirectory + inputPath);

			prop.load(frProp);
			locators.load(frLoc);
			tap.load(frTap);
			input.load(frInput);
		}

		String browser = prop.getProperty("browser").toLowerCase();
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless=new", "-disable-gpu", "-window-size=1920,1080", "--lang=en");

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.addArguments("--headless", "-disable-gpu", "-window-size=1920,1080", "--lang=en");

		EdgeOptions edgeOptions = new EdgeOptions();
		edgeOptions.addArguments("--headless=new", "-disable-gpu", "-window-size=1920,1080", "--lang=en");

		switch (browser) {
			case "chrome":
				driver = new ChromeDriver(chromeOptions);
				break;
			case "firefox":
				driver = new FirefoxDriver(firefoxOptions);
				break;
			case "edge":
				driver = new EdgeDriver(edgeOptions);
				break;
			default:
				throw new IllegalArgumentException("Unsupported browser: " + browser);
		}

		driver.manage().window().maximize();
		driver.get(prop.getProperty("testUrl"));

		WebElement welcomePhoto = driver.findElement(By.className(locators.getProperty("welcomePhoto")));
		String ActualWelcomePhotoSrc = welcomePhoto.getAttribute("src");
		String expectedWelcomePhoto = tap.getProperty("welcomePhoto");
		org.junit.Assert.assertEquals(ActualWelcomePhotoSrc, expectedWelcomePhoto);
	}

	@After
	public void tearDown() {
		driver.quit();
	}

}
