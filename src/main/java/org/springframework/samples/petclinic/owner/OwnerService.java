package org.springframework.samples.petclinic.owner;

import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public long getOwnerCount() {
        return ownerRepository.countOwners();
    }
}