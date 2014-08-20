package com.griddynamics.web.pages;

import com.griddynamics.qa.ui.AbstractPage;
import com.griddynamics.qa.ui.Pages;
import org.jbehave.web.selenium.WebDriverProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author lzakharova
 */

public class CustomPages extends Pages {

    @Autowired
    private WebDriverProvider driverProvider;

    public CustomPages() {
    }

    public CustomPages(WebDriverProvider driverProvider, Map<String, AbstractPage> pageMappings) {
        this.driverProvider = driverProvider;
        pagesMap.putAll(pageMappings);
    }
}
