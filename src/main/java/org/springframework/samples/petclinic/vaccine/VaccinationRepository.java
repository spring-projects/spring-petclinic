package org.springframework.samples.petclinic.vaccine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccinations, Integer> {

	@Query("SELECT v FROM Vaccinations v WHERE v.pet.id = :petId")
	Set<Vaccinations> findAllByPetId(@Param("petId") Integer petId);

}
