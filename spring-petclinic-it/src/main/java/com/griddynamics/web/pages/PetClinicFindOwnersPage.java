package com.griddynamics.web.pages;

import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

/**
 * @author lzakharova
 */
public class PetClinicFindOwnersPage extends PetClinicCommonPage {

    public static final String FIND_OWNER_FIELD = "Find Owner field";
    public static final By findOwnerFieldLoc = By.xpath("//input[@id='lastName']");

    public static final String FIND_OWNER_BUTTON = "Find Owner button";
    public static final By findOwnerButtonLoc = By.xpath("//button[@type='submit']");

    public static final String OWNERS_TABLE = "Owners table";
    public static final By ownersTableLoc = By.xpath("//table[@id='owners']");


    public PetClinicFindOwnersPage(WebDriverProvider driverProvider, String currentPageUri, String currentPageTitle) {
        super(driverProvider, currentPageUri, currentPageTitle);

        addElement(FIND_OWNER_FIELD, findOwnerFieldLoc);
        addElement(FIND_OWNER_BUTTON, findOwnerButtonLoc);
        addElement(OWNERS_TABLE, ownersTableLoc);
    }

    @Override
    public void assertCurrentPage() {
        super.assertCurrentPage();
        assertTrue(PAGE_TITLE + " is not displayed", isElementDisplayedOnPage(PAGE_TITLE));
    }
}

