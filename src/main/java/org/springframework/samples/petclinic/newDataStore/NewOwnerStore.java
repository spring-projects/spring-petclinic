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

    public Map<Integer, StaticOwner> ownerStore;
    private final OwnerRepository owners;

    public NewOwnerStore(OwnerRepository owners) {
        this.owners = owners;
        this.ownerStore = new HashMap<>();
    }

    public void populateStore() {
        Collection<Owner> ownerRepositoryData = this.owners.findAll();
        for(Owner owner : ownerRepositoryData) {
            ownerStore.put(owner.getId(), convertToStaticOwner(owner));
        }
    }

    public StaticOwner convertToStaticOwner(Owner ownerEntity) {
        return new StaticOwner(ownerEntity.getAddress(), ownerEntity.getCity(), ownerEntity.getTelephone());
    }
}
