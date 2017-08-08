package springpetclinic_selenium.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import org.junit.runner.RunWith;
// Associates Cucumber-JVM with the JUnit runner
@RunWith(Cucumber.class)
@CucumberOptions( 
		format = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber-json-report.json"},
		features = "src/test/java/springpetclinic_selenium/cucumber/features")
public class RunCukesTest {
}
