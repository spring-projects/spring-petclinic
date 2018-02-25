package org.springframework.samples.petclinic.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseEntityTest {

    private BaseEntity baseEntityInstance;

    @Before
    public void setUp(){
        baseEntityInstance = new BaseEntity();
    }

    // Expects the baseEntity to have Integer of 100
    @Test
    public void testGetAndSetId() {
        baseEntityInstance.setId(100);
        assertEquals((Integer)100, baseEntityInstance.getId());
    }

    // Expects the baseEntity ID to be null
    @Test
    public void TestIsNew() {
        baseEntityInstance.isNew();
        assertNull(baseEntityInstance.getId());
    }
}
