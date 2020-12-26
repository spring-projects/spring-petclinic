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

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication(proxyBeanMethods = false)
public class PetClinicApplication {

	public static void main2(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }

    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<Integer>();

        ints.add(2);
        ints.add(1);

        Collections.sort(ints, (n1, n2) -> n1.compareTo(n2));
        for (Integer i : ints) {
            System.out.println(i);
        }
    }

}
