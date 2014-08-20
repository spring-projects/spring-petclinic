package com.griddynamics.web.pages;

import org.jbehave.web.selenium.WebDriverProvider;

import static org.junit.Assert.assertTrue;

/**
 * @author lzakharova
 */
public class PetClinicHomePage extends PetClinicCommonPage {

    public PetClinicHomePage(WebDriverProvider driverProvider, String currentPageUri, String currentPageTitle) {
        super(driverProvider, currentPageUri, currentPageTitle);
    }

    @Override
    public void assertCurrentPage() {
        super.assertCurrentPage();
        assertTrue(PAGE_TITLE + " is not displayed", isElementDisplayedOnPage(PAGE_TITLE));
    }
}

