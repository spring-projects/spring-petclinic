package org.springframework.samples.petclinic.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersonTest {

    private Person personObj;

    @Before
    public void setUp(){
        personObj = new Person();
    }

    // Testing the DAO of Person class by verifying the setter and getter of FirstName
    @Test
    public void testSetAndGetFirstName() {
        personObj.setFirstName("Johnny");
        assertEquals("Johnny", personObj.getFirstName());
    }

    // Testing the DAO of Person class by verifying the setter and getter of FirstName
    @Test
    public void testSetAndGetLastName() {
        personObj.setLastName("Oliver");
        assertEquals("Oliver", personObj.getLastName());
    }
}
