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

package org.springframework.samples.petclinic.system;

import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
class InitData {
    private final EntityManagerFactory emf;

    @Autowired
    public InitData(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @PostConstruct
    public void registerListeners() {
        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
        EntityManager em = sessionFactory.createEntityManager();

        createPetTypes(em);
        createOwners(em);
        createVets(em);
        createSpecialities(em);
    }

    private void createPetTypes(EntityManager em) {
        em.getTransaction().begin();
        Stream.of("cat", "dog", "lizard", "snake", "bird", "hamster").forEach(v -> {
            PetType petType = new PetType();
            em.persist(petType);
        });
        em.getTransaction().commit();
    }

    private void createSpecialities(EntityManager em) {
        em.getTransaction().begin();
        Stream.of("radiology", "surgery", "dentistry").forEach(name -> em.persist(new Specialty(name)));
        em.getTransaction().commit();
    }

    private void createVets(EntityManager em) {
        em.getTransaction().begin();
        em.persist(new Vet("James", "Carter"));
        em.persist(new Vet("Helen", "Leary"));
        em.persist(new Vet("Linda", "Douglas"));
        em.persist(new Vet("Rafael", "Ortega"));
        em.persist(new Vet("Henry", "Stevens"));
        em.persist(new Vet("Sharon", "Jenkins"));
        em.getTransaction().commit();
    }

    private void createOwners(EntityManager em) {
        em.getTransaction().begin();
        em.persist(new Owner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023"));
        em.persist(new Owner("Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749"));
        em.persist(new Owner("Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", "6085558763"));
        em.persist(new Owner("Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198"));
        em.persist(new Owner("Peter", "McTavish", "2387 S. Fair Way", "Madison", "6085552765"));
        em.persist(new Owner("Jean", "Coleman", "105 N. Lake St.", "Monona", "6085552654"));
        em.persist(new Owner("Jeff", "Black", "1450 Oak Blvd.", "Monona", "6085555387"));
        em.persist(new Owner("Maria", "Escobito", "345 Maple St.", "Madison", "6085557683"));
        em.persist(new Owner("David", "Schroeder", "2749 Blackhawk Trail", "Madison", "6085559435"));
        em.persist(new Owner("Carlos", "Estaban", "2335 Independence La.", "Waunakee", "6085555487"));
        em.getTransaction().commit();
    }

}
