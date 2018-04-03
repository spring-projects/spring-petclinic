/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.newDataStore;


import java.util.*;
import java.util.regex.Pattern;

import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.StaticOwner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

/**
 * @author Gibran
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class NewOwnerStoreTest {

    private static final int TEST_OWNER_ID = 1;

    @Autowired
    OwnerRepository ownerRepository;

    NewOwnerStore testOwnerStore;

    Map<Integer, StaticOwner> ownerStore;

    Owner owner;

    @Before
    public void setup() {
        testOwnerStore = NewOwnerStore.getInstance(ownerRepository);
        testOwnerStore.forklift();
        ownerStore = testOwnerStore.getStore();
        MockitoAnnotations.initMocks(this);
        doReturn(TEST_OWNER_ID).when(owner).getId();
        doReturn("John").when(owner).getFirstName();
        doReturn("Doe").when(owner).getLastName();
        doReturn("123 Cucumber Lane").when(owner).getAddress();
        doReturn("Placeville").when(owner).getCity();
        doReturn("1234567890").when(owner).getTelephone();
        given(this.ownerRepository.findById(TEST_OWNER_ID)).willReturn(owner);
    }

    @Test
    public void testForklift() {
        for (Integer id: ownerStore.keySet()){

            Integer key = id;
            String value = ownerStore.get(id).getAddress();
            System.out.println(key + " " + value);
        }
    }

    @Test
    public void testShadowRead() {
        testOwnerStore = NewOwnerStore.getInstance(ownerRepository);
        testOwnerStore.forklift();

        Collection<Owner> results = this.ownerRepository.findByLastName("");

        int inconsistencies = compareResults(results, "");

        assertEquals(inconsistencies, 0);
    }

    private int compareResults(Collection<Owner> results, String lastName) {

        String pattern = "/^" + lastName + "/";

        HashMap<Integer, StaticOwner> storeMap = testOwnerStore.getStore();

        ArrayList<StaticOwner> staticOwners = new ArrayList<>();

        for (StaticOwner owner : storeMap.values()) {
            if (!Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(owner.getLastName()).find())
                staticOwners.add(owner);
        }

        System.out.println("Size: " + staticOwners.size());
        int inconsistencies = 0;

        for (Owner owner : results)
        {
            boolean found = false;
            for (StaticOwner staticOwner : staticOwners)
            {
                if (staticOwner.equals(owner)) {
                    System.out.println("Found. Good");
                    found = true;
                    continue;
                }
            }

            if (!found)
            {
                inconsistencies++;
                System.out.println("Not Found. Bad");
                testOwnerStore.findAndReplace(owner);
            }

        }

        return inconsistencies;
    }

    @Test
    public void consistencyCheck () {
        System.out.println(testOwnerStore.checkConsistency());
    }

    @Test
    public void testShadowWrite() {
        testOwnerStore = NewOwnerStore.getInstance(ownerRepository);

        //make sure that inconsistencies are recorded and fixed
        testOwnerStore.testPutInOldDatastoreOnly(owner);
        assertEquals(1, testOwnerStore.checkConsistency());
        assertEquals(0, testOwnerStore.checkConsistency());

        //make sure that any changes written to old database are also written to new database
        testOwnerStore.save(owner);
        assertEquals(0, testOwnerStore.checkConsistency());
    }
}
