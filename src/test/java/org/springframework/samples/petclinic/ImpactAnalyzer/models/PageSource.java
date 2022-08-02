package org.springframework.samples.petclinic.ImpactAnalyzer.models;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.io.UnsupportedEncodingException;
import org.openqa.selenium.WebDriver;


public class PageSource {


	private String pageName;

	private String pageSource;

	private String pageUrl;

	public PageSource(String pageName, String pageSource,String pageUrl){

		this.pageName = pageName;

		this.pageSource = pageSource;

		this.pageUrl = pageUrl;


	}

	public String getPageUrl() {
		return pageUrl;
	}

	public static void addExtraJSONPage(String pageUrl, WebDriver driver) throws UnsupportedEncodingException, InterruptedException {
			driver.get(pageUrl);
		Thread.sleep(8000);
		new PageSource(pageUrl,driver.getPageSource(),pageUrl).addJSON();
	}

	public void addJSON() throws InterruptedException, UnsupportedEncodingException {


		String base64encodedString = Base64.getEncoder().encodeToString(
               pageUrl.getBytes("utf-8"));
//        String pageSource = driver.getPageSource();
        System.out.println(pageSource);
        Thread.sleep(8000);

        File out = new File("json/"+base64encodedString+".json");
        File in = new File(base64encodedString+".html");

        try {
            FileWriter myWriter = new FileWriter(in);
            myWriter.write(pageSource);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HtmlParser.parseToJsonFile(in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

}
