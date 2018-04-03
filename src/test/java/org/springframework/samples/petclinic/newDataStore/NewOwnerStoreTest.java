/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.newDataStore;

import java.util.Iterator;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.StaticOwner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Gibran
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class NewOwnerStoreTest {

    @Autowired
    OwnerRepository owner;

    NewOwnerStore testOwnerStore;

    Map<Integer, StaticOwner> ownerStore;

    @Before
    public void setup() {
        testOwnerStore = new NewOwnerStore(owner);
        testOwnerStore.forklift();
        ownerStore = testOwnerStore.getNewOwnerStore();
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
    public void consistencyCheck () {
        System.out.println(testOwnerStore.checkConsistency());
    }
}
