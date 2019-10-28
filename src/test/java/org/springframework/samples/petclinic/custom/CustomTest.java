package org.springframework.samples.petclinic.custom;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class CustomTest {

    @Test
    public void alwaysTrueTest() {
        Assert.isTrue(true);
    }

}
