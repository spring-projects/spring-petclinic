/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.junit.Test;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.util.SerializationUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Dave Syer
 *
 */
public class VetTests {

    @Test
    public void testSerialization() {
        Vet vet = new Vet();
        vet.setFirstName("Zaphod");
        vet.setLastName("Beeblebrox");
        vet.setId(123);
        Vet other = (Vet) SerializationUtils
                .deserialize(SerializationUtils.serialize(vet));
        assertThat(other.getFirstName()).isEqualTo(vet.getFirstName());
        assertThat(other.getLastName()).isEqualTo(vet.getLastName());
        assertThat(other.getId()).isEqualTo(vet.getId());
    }

    @Test
    public void testGetSpecialties(){
        Vet vet = new Vet();
        vet.setFirstName("Zaphod");
        vet.setLastName("Beeblebrox");
        vet.setId(123);
        Specialty radiology = new Specialty();
        Specialty urinalysis = new Specialty();
        Specialty kinesiology = new Specialty();
        vet.addSpecialty(radiology);
        vet.addSpecialty(urinalysis);
        //vet.addSpecialty(kinesiology);

        List<Specialty> testList = vet.getSpecialties();
        assertThat(testList).isEqualTo(vet.getSpecialties());
        testList.getClass().getSimpleName().equals("UnmodifiableCollection");
    }

    @Test
    public void testAddGetNrSpecialty(){
        Vet vet = new Vet();
        vet.setFirstName("Zaphod");
        vet.setLastName("Beeblebrox");
        vet.setId(123);

        int preAddedNumber = vet.getNrOfSpecialties();
        Specialty radiology = new Specialty();
        //radiology.setName("radiology");
        vet.addSpecialty(radiology);
        assertThat(vet.getNrOfSpecialties()).isEqualTo(++preAddedNumber);
        assertThat((vet.getSpecialtiesInternal()).contains(radiology));
    }

}
