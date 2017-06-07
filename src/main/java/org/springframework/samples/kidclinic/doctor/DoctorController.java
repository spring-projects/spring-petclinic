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
package org.springframework.samples.kidclinic.doctor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class DoctorController {

    private final DoctorRepository doctors;

    @Autowired
    public DoctorController(DoctorRepository clinicService) {
        this.doctors = clinicService;
    }

    @RequestMapping(value = { "/doctors.html" })
    public String showDoctorList(Map<String, Object> model) {
        // Here we are returning an object of type 'Doctors' rather than a collection of Doctor
        // objects so it is simpler for Object-Xml mapping
        Doctors doctors = new Doctors();
        doctors.getDoctorList().addAll(this.doctors.findAll());
        model.put("doctors", doctors);
        return "doctors/doctorList";
    }

    @RequestMapping(value = { "/doctors.json", "/doctors.xml" })
    public @ResponseBody Doctors showResourcesVetList() {
        // Here we are returning an object of type 'Doctors' rather than a collection of Doctor
        // objects so it is simpler for JSon/Object mapping
        Doctors doctors = new Doctors();
        doctors.getDoctorList().addAll(this.doctors.findAll());
        return doctors;
    }

}
