package org.springframework.samples.petclinic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AlwaysFailTests {

    @Test
    public void failTest() throws Exception {
        assertEquals(2, 2);
    }

}
