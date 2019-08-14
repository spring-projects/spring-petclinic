package org.springframework.samples.petclinic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class AlwaysFailTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void failTest() throws Exception{
        assertEquals(1, 2);
    }

}
