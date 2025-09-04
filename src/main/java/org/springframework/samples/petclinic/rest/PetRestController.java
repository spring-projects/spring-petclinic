package org.springframework.samples.petclinic.rest;

import java.util.List;

import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets")
public class PetRestController {

    private final OwnerRepository ownerRepository;
    private final PetTypeRepository petTypeRepository;

    public PetRestController(OwnerRepository ownerRepository, PetTypeRepository petTypeRepository) {
        this.ownerRepository = ownerRepository;
        this.petTypeRepository = petTypeRepository;
    }

    @GetMapping("/{ownerId}")
    public List<Pet> getPetsByOwner(@PathVariable int ownerId) {
        return ownerRepository.findById(ownerId)
                .map(owner -> owner.getPets())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found with id " + ownerId));
    }
}
