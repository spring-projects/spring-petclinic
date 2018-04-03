/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.newDataStore;

import java.util.Iterator;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.StaticOwner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

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

    @Mock
    Owner mockOwner;

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

    @Test
    public void checkShadowWrite() {
        testOwnerStore = new NewOwnerStore(owner);

       //make sure that inconsisties are recorded and fixed
       testOwnerStore.testPutInOldDatastoreOnly(mockOwner);
       assertEquals(1, testOwnerStore.checkConsistency());
       assertEquals(0, testOwnerStore.checkConsistency());

       //make sure that any changes written to old database are also written to new database
       testOwnerStore.save(mockOwner);
       assertEquals(0, testOwnerStore.checkConsistency());
    }


}
