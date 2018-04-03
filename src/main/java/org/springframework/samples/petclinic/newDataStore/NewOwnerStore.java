/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.newDataStore;

import java.util.Collection;
import java.util.HashMap;
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

    public void populateStore() {
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

        // Report whether inexistent or inconsistent based on exists

    }
}
