package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface VaccinationRepository extends Repository<Vaccination, Integer> {

	@Query("SELECT v FROM Vaccination v WHERE v.pet.id = :petId")
	List<Vaccination> findByPetId(@Param("petId") int petId);

	void save(Vaccination vaccination);

}
