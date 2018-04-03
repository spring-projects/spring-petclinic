/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.newDataStore;

import java.util.*;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.StaticOwner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Gibran
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class NewOwnerStoreTest {

    @Autowired
    OwnerRepository owners;

    NewOwnerStore testOwnerStore;

    @Test
    public void testPopulation() {
        testOwnerStore = NewOwnerStore.getInstance(owners);
        testOwnerStore.populateStore();
        Map<Integer, StaticOwner> ownerStore = testOwnerStore.getStore();

        for (Integer id: ownerStore.keySet()){

            Integer key = id;
            String value = ownerStore.get(id).getAddress();
            System.out.println(key + " " + value);
        }
    }

    @Test
    public void testShadowRead() {
        testOwnerStore = NewOwnerStore.getInstance(owners);
        testOwnerStore.populateStore();

        Collection<Owner> results = this.owners.findByLastName("");

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


}
