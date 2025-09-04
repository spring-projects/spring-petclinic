package org.springframework.samples.petclinic.rest;

import java.util.List;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
public class OwnerRestController {

    private final OwnerRepository ownerRepository;

    public OwnerRestController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @GetMapping
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    @GetMapping("/{ownerId}")
    public Owner getOwnerById(@PathVariable int ownerId) {
        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found with id " + ownerId));
    }

    @PostMapping
    public Owner createOwner(@RequestBody Owner owner) {
        return ownerRepository.save(owner);
    }

    @PutMapping("/{ownerId}")
    public Owner updateOwner(@PathVariable int ownerId, @RequestBody Owner ownerRequest) {
        return ownerRepository.findById(ownerId).map(owner -> {
            owner.setFirstName(ownerRequest.getFirstName());
            owner.setLastName(ownerRequest.getLastName());
            owner.setAddress(ownerRequest.getAddress());
            owner.setCity(ownerRequest.getCity());
            owner.setTelephone(ownerRequest.getTelephone());
            return ownerRepository.save(owner);
        }).orElseThrow(() -> new IllegalArgumentException("Owner not found with id " + ownerId));
    }

    @DeleteMapping("/{ownerId}")
    public void deleteOwner(@PathVariable int ownerId) {
        ownerRepository.deleteById(ownerId);
    }
}
