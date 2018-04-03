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

    public Map<Integer, StaticOwner> ownerStore;
    private final OwnerRepository owners;
    private Collection<Owner> ownerRepositoryData;

    public NewOwnerStore(OwnerRepository owners) {
        this.owners = owners;
        this.ownerStore = new HashMap<>();
    }

    public StaticOwner convertToStaticOwner(Owner ownerEntity) {
        return new StaticOwner(ownerEntity.getAddress(), ownerEntity.getCity(), ownerEntity.getTelephone());
    }

    public Map<Integer, StaticOwner> getNewOwnerStore() {
        return this.ownerStore;
    }

    public void forklift() {
        Collection<Owner> ownerRepositoryData = this.owners.findAll();
        for(Owner owner : ownerRepositoryData) {
            ownerStore.put(owner.getId(), convertToStaticOwner(owner));
        }
    }

    public int checkConsistency() {
        int inconsistencies = 0;

        ownerRepositoryData = this.owners.findAll();

        Iterator<Owner> iterator = ownerRepositoryData.iterator();

        for (Integer id: ownerStore.keySet()){

            if(iterator.hasNext()) {
                Owner oldOwner = iterator.next();
                if(id != oldOwner.getId() || !ownerStore.get(id).equals(oldOwner)) {
                    inconsistencies++;
                    violation(id, convertToStaticOwner(oldOwner), ownerStore.get(id));
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
}
