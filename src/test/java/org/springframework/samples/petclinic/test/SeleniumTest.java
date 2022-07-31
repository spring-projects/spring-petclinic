package org.springframework.samples.petclinic.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

// This is a comment
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.samples.petclinic.ImpactAnalyzer.connect.Connect;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.PageSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



public class SeleniumTest {
	WebDriver driver;


	@org.junit.Test
	public void test() throws InterruptedException, UnsupportedEncodingException {

		String currentUrl;

		ArrayList<String> urls = new ArrayList<String>();
		WebDriverManager.chromedriver().setup();

		ChromeOptions opt = new ChromeOptions();
		opt.setHeadless(true);
		driver = new ChromeDriver(opt);
		WebDriver driver = new ChromeDriver();

		//Veterinarians listing
//        driver.get("http://localhost:8080");
//
//        driver.findElement(By.xpath("/html/body/nav/div/div/ul/li[4]/a/span[2]")).click();
//        urls.add(driver.getCurrentUrl());
//        driver.findElement(By.xpath("/html/body/nav/div/div/ul/li[3]/a/span[2]")).click();
//        urls.add(driver.getCurrentUrl());
//
//        String table = "";
//        table = driver.findElement(By.xpath("//html/body/div/div/table/tbody")).getText();
//        urls.add(driver.getCurrentUrl());
//
//        driver.findElement(By.xpath("/html/body/div/div/div[1]/span[4]/a")).click();
//        urls.add(driver.getCurrentUrl());
//
//        table += "\n"+ driver.findElement(By.xpath("//html/body/div/div/table/tbody")).getText();
//        urls.add(driver.getCurrentUrl());
//
//        driver.findElement(By.xpath("/html/body/div/div/div[1]/span[6]/a")).click();
//        urls.add(driver.getCurrentUrl());
//
//        System.out.println(table);
//
//        driver.findElement(By.xpath("/html/body/nav/div/div/ul/li[2]/a")).click();
//        urls.add(driver.getCurrentUrl());
//
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div/div/form/a")).click();
//        urls.add(driver.getCurrentUrl());
//
//        Thread.sleep(1000);

         /*
        @Page new_user
        @URL http://localhost:8080/owners/new
         */
		driver.get("http://localhost:8080/owners/new");

		currentUrl = driver.getCurrentUrl();
		PageSource.pageSource(driver,currentUrl);

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[1]/div/div/input")).sendKeys("Başak");
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[2]/div/div/input")).sendKeys("Balcı");
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[3]/div/div/input")).sendKeys("B");
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[4]/div/div/input")).sendKeys("İST");
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[5]/div/div/input")).sendKeys("0531031343");
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[2]/div/button")).click();
		urls.add(driver.getCurrentUrl());

		Thread.sleep(1000);
		//Edit
          /*
        @Page edit
        @URL http://localhost:8080/owners/13/edit
         */

		driver.get("http://localhost:8080/owners/11/edit");
		currentUrl = driver.getCurrentUrl();
		PageSource.pageSource(driver,currentUrl);

//		driver.findElement(By.xpath("/html/body/div/div/a[1]")).click();
//		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[1]/div/div/input")).clear();
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[2]/div/div/input")).clear();
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[1]/div/div/input")).sendKeys("Ekin");
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[2]/div/div/input")).sendKeys("Şahinkaya");
		urls.add(driver.getCurrentUrl());

		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/div/div/form/div[2]/div/button")).click();
		urls.add(driver.getCurrentUrl());

//		Thread.sleep(1000);
//		driver.findElement(By.xpath("/html/body/div/div/a[2]")).click();
//		urls.add(driver.getCurrentUrl());

        /*
        @Page add_new_pet
        @URL http://localhost:8080/owners/1/pets/new
         */

		driver.get("http://localhost:8080/owners/11/pets/new");
		currentUrl = driver.getCurrentUrl();
		PageSource.pageSource(driver,currentUrl);


		currentUrl = driver.getCurrentUrl();
		PageSource.pageSource(driver,currentUrl);



		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[2]/div/div/input")).sendKeys("çiko");
		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[3]/div/div/input")).sendKeys("04.03.2021");

		urls.add(driver.getCurrentUrl());

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[3]/div/div/input")).clear();

		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[3]/div/div/input")).sendKeys("06.05.2022");
		Thread.sleep(4000);


		driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[4]/div/select/option[2]")).click();
		urls.add(driver.getCurrentUrl());

		Thread.sleep(4000);
		driver.findElement(By.xpath("/html/body/div/div/form/div[2]/div/button")).click();
		urls.add(driver.getCurrentUrl());

//
//        //add visit
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div/div/table[2]/tbody/tr/td[2]/table/tbody/tr/td[2]/a")).click();
//        urls.add(driver.getCurrentUrl());
//
//        driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[1]/div/div/input")).sendKeys("05.03.2021");
//        urls.add(driver.getCurrentUrl());
//
//        driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div[2]/div/div/input")).sendKeys("helloo");
//        urls.add(driver.getCurrentUrl());
//
//        driver.findElement(By.xpath("/html/body/div/div/form/div[2]/div/button")).click();
//        urls.add(driver.getCurrentUrl());
//
//        Thread.sleep(1000);
//
//        //find owner button
//        Thread.sleep(1000);
//
//        driver.findElement(By.xpath("/html/body/nav/div/div/ul/li[2]/a")).click();
//        urls.add(driver.getCurrentUrl());
//
//        driver.findElement(By.xpath("/html/body/div/div/form/div[1]/div/div/input")).sendKeys("Şahinkaya");
//        urls.add(driver.getCurrentUrl());
//
//        driver.findElement(By.xpath("/html/body/div/div/form/div[2]/div/button")).click();
//        urls.add(driver.getCurrentUrl());
//        Thread.sleep(6000);
//        driver.quit();
//
//        String outputFile ="";
//        for (int i = 0; i <urls.size() ; i++) {
//            outputFile= outputFile + "\n" + urls.get(i);
//        }



	}

}

