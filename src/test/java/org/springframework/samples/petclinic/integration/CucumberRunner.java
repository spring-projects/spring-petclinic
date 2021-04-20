package org.springframework.samples.petclinic.integration;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:target/cucumber-html-report" },
		glue = { "org.springframework.samples.petclinic.integration" }, features = "classpath:scenarios")
public class CucumberRunner {

}
