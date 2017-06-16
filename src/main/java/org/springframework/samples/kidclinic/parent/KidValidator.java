/*
 * Copyright 2002-2013 the original author or authors.
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
package org.springframework.samples.kidclinic.parent;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Kid</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class KidValidator implements Validator {

    private static final String REQUIRED = "required";

    @Override
    public void validate(Object obj, Errors errors) {
        Kid kid = (Kid) obj;
        String name = kid.getName();
        String medications = kid.getMedications();
        String allergies = kid.getAllergies();
        
        // name validation
        if (!StringUtils.hasLength(name)) {
            errors.rejectValue("name", REQUIRED, REQUIRED);
        }
        
        // allergies validation
        if (!StringUtils.hasLength(allergies)) {
            errors.rejectValue("allergies", REQUIRED, REQUIRED);
        }
        
        // medications validation
        if (!StringUtils.hasLength(medications)) {
            errors.rejectValue("medications", REQUIRED, REQUIRED);
        }

        if (kid.isNew() && kid.getGender() == null) {
            errors.rejectValue("gender", REQUIRED, REQUIRED);
        }

        // birth date validation
        if (kid.getBirthDate() == null) {
            errors.rejectValue("birthDate", REQUIRED, REQUIRED);
        }
    }

    /**
     * This Validator validates *just* Kid instances
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Kid.class.isAssignableFrom(clazz);
    }


}
