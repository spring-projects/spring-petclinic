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
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.SpecialtyRepository;

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
	public CommandLineRunner pruebasSpring(VetRepository vetRepository, SpecialtyRepository specialtyRepository) {

		return (args) -> {

			System.out.println("\nCrear un objeto Vet sin Speciality");
			Vet vet = new Vet();
			vet.setFirstName("Roberto");
			vet.setLastName("Gutiérrez");

			System.out.println("\nPersistir el objeto Vet en BBDD");
			vet = vetRepository.save(vet);

			System.out.println("\nConsultar por ID y comprobar que se ha creado correctamente");
			Vet vetTemp = vetRepository.findOne(vet.getId());
			System.out.println(vetTemp.toString());

			System.out.println("\nEditar el elemento recién creado para añadir una Speciality concreta");
			Specialty s = specialtyRepository.findSpecialtiesById(1);
			vet.addSpecialty(s);
			vet = vetRepository.save(vet);
			System.out.println(vet.toString());

			System.out.println("\nListar todos los veterinarios existentes");
			for (Vet v : vetRepository.findAll()) {
				System.out.println("Vet: " + v);
			}

		};
	}
}
