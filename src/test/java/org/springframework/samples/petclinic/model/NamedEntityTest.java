package org.springframework.samples.petclinic.model;

import org.junit.Before;
import org.junit.Test;

import javax.naming.Name;

import static org.junit.Assert.*;

public class NamedEntityTest {

    private NamedEntity namedObj;

    @Before
    public void setUp(){
        namedObj = new NamedEntity();
    }

    // Testing the DAO of NamedEntity class by verifying the setter, getter and toString of Name
    @Test
    public void testSetAndGetName() {
        namedObj.setName("Junior The Senior");
        assertEquals("Junior The Senior", namedObj.getName());
        assertEquals("Junior The Senior", namedObj.toString());
    }
}
