package org.springframework.samples.petclinic.test.clinicTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

// This is a comment

import org.springframework.samples.petclinic.ImpactAnalyzer.connect.Connect;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.PageSource;

import static org.junit.Assert.fail;


public class FindOwner {

	String className;

	String methodName;


	WebDriver driver;
	ArrayList<PageSource> pageSourceList;
	ArrayList<String> pageNames;

	int getMaxVid;

	ArrayList<String> allPageNames ;


	@Before
	public final void before() {

		pageSourceList = new ArrayList<PageSource>();

	}

	boolean testFailed = false;
	@Test
	public void test(){
		try {
			findOwnerTest();
		}
		catch (Exception e){
			testFailed = true;

		}
	}
	@After
	public final void after() throws UnsupportedEncodingException, InterruptedException {
		Connect.connect();

		getMaxVid = Connect.getMaxVid();
//		allPageNames = Connect.getPageNames(getMaxVid);
		allPageNames = Connect.getPageNamesOfTests(getMaxVid,className,methodName);
		for(PageSource pageSource: pageSourceList){
			pageSource.addJSON();
			while (allPageNames.contains(pageSource.getPageUrl())){
				allPageNames.remove(pageSource.getPageUrl());
			}
		}
		for(String pageUrl : allPageNames){
			PageSource.addExtraJSONPage(pageUrl,driver);
		}

		if(testFailed){
			fail("Test failed");

		}

		Connect.close();
	}



	public void findOwnerTest() throws InterruptedException, UnsupportedEncodingException {

		String currentUrl;

		ArrayList<String> urls = new ArrayList<String>();
		WebDriverManager.chromedriver().setup();

		ChromeOptions opt = new ChromeOptions();
		opt.setHeadless(true);
		driver = new ChromeDriver(opt);
		WebDriver driver = new ChromeDriver();



		// find owner
		driver.get("http://localhost:8080/owners/find");

		currentUrl = driver.getCurrentUrl();

		pageSourceList.add(new PageSource("newUser", driver.getPageSource(), driver.getCurrentUrl()));




		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div/div/input")).sendKeys("Franklin");

		Thread.sleep(500);

		driver.findElement(By.xpath("/html/body/div/div/form/div[2]/div/button")).click();

//http://localhost:8080/owners/1

//		currentUrl = driver.getCurrentUrl();
//
//		pageSourceList.add(new PageSource("newUser", driver.getPageSource(), driver.getCurrentUrl()));
//
//
//		Thread.sleep(250);
//
//
//		driver.findElement(By.xpath("/html/body/div/div/table[2]/tbody/tr/td[2]/table/tbody/tr[4]/td[2]/a")).click();
//
////http://localhost:8080/owners/1/pets/1/visits/new
//
//		currentUrl = driver.getCurrentUrl();
//
//		pageSourceList.add(new PageSource("newUser", driver.getPageSource(), driver.getCurrentUrl()));
//
//
//		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[2]/div/div/input")).sendKeys("leo depresyonda");
//
//		Thread.sleep(250);
//
//
//		driver.findElement(By.xpath("/html/body/div/div/form/div[2]/div/button")).click();
//








	}




}

