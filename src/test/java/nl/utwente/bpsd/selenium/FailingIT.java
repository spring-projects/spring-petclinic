package nl.utwente.bpsd.selenium;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.MalformedURLException;

/**
 * @author Martijn
 * @since 21-6-2017.
 */
public class FailingIT extends SeleniumBaseIT {
    public FailingIT() throws MalformedURLException {
        super();
    }

    @Test
    @Category(SeleniumBaseIT.class)
    @Ignore
    public void failIT() {
	Assert.fail();
    }

}
