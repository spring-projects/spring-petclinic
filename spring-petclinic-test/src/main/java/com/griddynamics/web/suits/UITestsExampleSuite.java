package com.griddynamics.web.suits;


import com.griddynamics.qa.framework.WebStoriesRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzakharova
 */

public class UITestsExampleSuite extends WebStoriesRunner {
    protected static String contextPath = "/com/griddynamics/context/webApplicationContext.xml";

    public UITestsExampleSuite() {
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
                add("**/stories/uitests/**/**.story");
            }
        };
    }

    @Override
    public void beforeRun() {
    }
}
