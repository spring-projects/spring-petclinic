package com.griddynamics.webservices.suits;


import com.griddynamics.qa.framework.BaseStoriesRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzakharova
 */

public class WebServicesExampleSuite extends BaseStoriesRunner {
    protected static String contextPath = "/com/griddynamics/context/webServicesApplicationContext.xml";

    public WebServicesExampleSuite() {
        super(contextPath);
    }

    @Override
    protected List<String> excludeStories() {
        return new ArrayList<String>() {
            {
                add("**/web/**/*Given*.story");
            }
        };
    }

    @Override
    protected List<String> includeStories() {
        return new ArrayList<String>() {
            {
                add("**/stories/webservice/**/**.story");
            }
        };
    }

    @Override
    public void beforeRun() {
    }
}
