/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;







import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.samples.petclinic.vet.SpecialityRepository;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication(proxyBeanMethods = false)
public class PetClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoVetRepository(VetRepository vetRepository, SpecialityRepository specialityRepository){
		return (args) ->{

			Vet vet = new Vet();
			vet.setFirstName("Javier");
			vet.setLastName("Polo");

			vet = vetRepository.save(vet);

			System.out.println(vetRepository.findVetById(vet.getId()));

			Specialty speciality = specialityRepository.findSpecialtiesById(1);

			vet.addSpecialty(speciality);

			vetRepository.save(vet);

			System.out.println("Veterinary: " + vet + "Speciality: " + vet.getSpecialties());

			for (Vet vetLoop: vetRepository.findAll()){
				System.out.println("Veterinary: " + vetLoop);
			}
			System.out.println("Veterinaries working on radiology \n");
//			for (Vet vetLoop: vetRepository.findVetBySpecialtiesName("Radiology")){
//				System.out.println("Veterinary: " + vetLoop);
//			}

			System.out.println(vetRepository.findVetBySpecialtiesName("radiology"));
		};
	}

}
