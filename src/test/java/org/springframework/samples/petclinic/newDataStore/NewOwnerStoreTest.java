/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.newDataStore;

import java.util.Iterator;
import java.util.Map;
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

    @Test
    public void testPopulation() {
        testOwnerStore = new NewOwnerStore(owner);
        testOwnerStore.populateStore();
        Map<Integer, StaticOwner> ownerStore = testOwnerStore.getNewOwnerStore();

        for (Integer id: ownerStore.keySet()){

            Integer key = id;
            String value = ownerStore.get(id).getAddress();
            System.out.println(key + " " + value);
        }


    }

}
