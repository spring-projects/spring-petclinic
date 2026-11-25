/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Acceptance test (Selenium) for owner registration and search, run against a deployed
 * application through a remote browser. Executed only by the {@code acceptance} Maven
 * profile.
 */
class OwnerRegistrationAT {

	private static final String SELENIUM_URL = System.getProperty("acceptance.seleniumUrl", "http://localhost:4444");

	private static final String BASE_URL = System.getProperty("acceptance.baseUrl", "http://petclinic:8080");

	private RemoteWebDriver driver;

	private WebDriverWait wait;

	@RegisterExtension
	final AfterTestExecutionCallback screenshotOnFailure = new AfterTestExecutionCallback() {
		@Override
		public void afterTestExecution(ExtensionContext context) throws IOException {
			if (context.getExecutionException().isPresent() && driver != null) {
				Path dir = Path.of("target", "acceptance-screenshots");
				Files.createDirectories(dir);
				byte[] png = driver.getScreenshotAs(OutputType.BYTES);
				Files.write(dir.resolve(context.getRequiredTestMethod().getName() + ".png"), png);
			}
		}
	};

	@BeforeEach
	void setUp() throws IOException {
		driver = new RemoteWebDriver(URI.create(SELENIUM_URL).toURL(), new ChromeOptions());
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@AfterEach
	void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	void shouldListBothOwners_whenTwoOwnersShareTheSearchedLastName() {
		registerOwner("Ana", "Acceptance", "3199990001");
		wait.until(ExpectedConditions.urlMatches("/owners/\\d+(;jsessionid=[^/?#]*)?$"));
		registerOwner("Bruno", "Acceptance", "3199990002");
		wait.until(ExpectedConditions.urlMatches("/owners/\\d+(;jsessionid=[^/?#]*)?$"));

		searchOwners("Acceptance");

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("owners")));
		List<WebElement> rows = driver.findElements(By.cssSelector("#owners tbody tr"));
		assertThat(rows).hasSize(2);
		List<String> texts = rows.stream().map(WebElement::getText).toList();
		assertThat(texts).anyMatch(t -> t.contains("Ana Acceptance") && t.contains("3199990001"));
		assertThat(texts).anyMatch(t -> t.contains("Bruno Acceptance") && t.contains("3199990002"));
	}

	@Test
	void shouldRejectOwner_whenTelephoneHasFiveDigits() {
		registerOwner("Carla", "Rejected", "12345");

		assertThat(driver.getCurrentUrl()).endsWith("/owners/new");
		WebElement group = driver.findElement(By.id("telephone"))
			.findElement(By.xpath("./ancestor::*[contains(@class,'form-group')][1]"));
		assertThat(group.getAttribute("class")).contains("has-error");

		searchOwners("Rejected");

		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#lastNameGroup p")));
		assertThat(driver.getCurrentUrl()).contains("/owners");
		assertThat(driver.findElements(By.id("owners"))).isEmpty();
		assertThat(driver.findElements(By.cssSelector("#search-owner-form #lastNameGroup p"))).isNotEmpty();
	}

	private void registerOwner(String firstName, String lastName, String telephone) {
		driver.get(BASE_URL + "/owners/new");
		fill("firstName", firstName);
		fill("lastName", lastName);
		fill("address", "Rua Teste 1");
		fill("city", "Belo Horizonte");
		fill("telephone", telephone);
		driver.findElement(By.id("add-owner-form")).submit();
	}

	private void searchOwners(String lastName) {
		driver.get(BASE_URL + "/owners/find");
		fill("lastName", lastName);
		driver.findElement(By.id("search-owner-form")).submit();
	}

	private void fill(String fieldId, String value) {
		WebElement field = driver.findElement(By.id(fieldId));
		field.clear();
		field.sendKeys(value);
	}

}
