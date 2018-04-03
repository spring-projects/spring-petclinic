/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.newDataStore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.StaticOwner;

/**
 * @author Gibran
 */
public class NewOwnerStore {

    private HashMap<Integer, StaticOwner> ownerStore;
    private final OwnerRepository owners;

    private static NewOwnerStore storeSingleton;

    private Collection<Owner> ownerRepositoryData;


    private NewOwnerStore(OwnerRepository owners) {
        this.owners = owners;
        this.ownerStore = new HashMap<>();
    }

    public static NewOwnerStore getInstance(OwnerRepository owners)
    {
        if (storeSingleton == null)
            storeSingleton = new NewOwnerStore(owners);

        return storeSingleton;
    }



    public void forklift() {
        Collection<Owner> ownerRepositoryData = this.owners.findAll();
        for(Owner owner : ownerRepositoryData) {
            ownerStore.put(owner.getId(), StaticOwner.convertToStaticOwner(owner));
        }
    }

    public HashMap<Integer, StaticOwner> getStore()
    {
        return ownerStore;
    }

    public void findAndReplace(Owner owner) {
        boolean exists = false;

        for (StaticOwner o : getStore().values()) {
            if (o.getId() == owner.getId()) {
                exists = true;
            }
        }

        // Replace
        getStore().put(owner.getId(), StaticOwner.convertToStaticOwner(owner));
    }

        // Report whether inexistent or inconsistent based on exists

    public int checkConsistency() {
        int inconsistencies = 0;

        ownerRepositoryData = this.owners.findAll();

        Iterator<Owner> iterator = ownerRepositoryData.iterator();

        for (Integer id: ownerStore.keySet()){

            if(iterator.hasNext()) {
                Owner oldOwner = iterator.next();
                if(id != oldOwner.getId() || !ownerStore.get(id).equals(oldOwner)) {
                    ownerStore.put(id, StaticOwner.convertToStaticOwner(oldOwner));
                    inconsistencies++;
                    violation(id, StaticOwner.convertToStaticOwner(oldOwner), ownerStore.get(id));
                }
            }
        }
        return inconsistencies;
    }

    private void violation(int i, StaticOwner expected, StaticOwner actual)
    {
        System.out.println("Consistency Violation! \n” + “i = ” + i + “\n\t expected = " +
        expected.toString() + "\n\t actual = " + actual.toString());
    }

    public void save(Owner owner){
        //actual write to datastore
        owners.save(owner);
        //shadow write to new datastore
        ownerStore.put(owner.getId(), StaticOwner.convertToStaticOwner(owner));
    }

    //this is for testing to introduce inconsistencies
    public void testPutInOldDatastoreOnly(Owner owner){
        owners.save(owner);
    }
}
