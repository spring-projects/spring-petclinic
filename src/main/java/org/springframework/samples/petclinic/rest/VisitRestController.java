package org.springframework.samples.petclinic.rest;

import java.util.List;

import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
public class VisitRestController {

    private final PetRepository petRepository;

    public VisitRestController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping("/{petId}")
    public List<Visit> getVisitsByPet(@PathVariable int petId) {
        return petRepository.findById(petId)
                .map(pet -> pet.getVisits())
                .orElseThrow(() -> new IllegalArgumentException("Pet not found with id " + petId));
    }
}
