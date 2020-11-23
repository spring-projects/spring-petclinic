package guru.springframework.springpetclinic.services;

import guru.springframework.springpetclinic.model.Pet;
import guru.springframework.springpetclinic.model.Vet;

import java.util.Set;

public interface VetService {

	Vet findById(Long id);

	Vet save(Vet vet);

	Set<Vet> findAll();
}
