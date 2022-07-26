package org.springframework.samples.petclinic.ImpactAnalyzer.models;


import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.io.UnsupportedEncodingException;


public class PageSource {

    public static void pageSource(WebDriver driver, String url) throws InterruptedException, UnsupportedEncodingException {


        String base64encodedString = Base64.getEncoder().encodeToString(
                url.getBytes("utf-8"));
        String pageSource = driver.getPageSource();
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
