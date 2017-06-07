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

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.kidclinic.visit.Visit;
import org.springframework.samples.kidclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 */
@Controller
class VisitController {

    private final VisitRepository visits;
    private final KidRepository kids;


    @Autowired
    public VisitController(VisitRepository visits, KidRepository kids) {
        this.visits = visits;
        this.kids = kids;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    /**
     * Called before each and every @RequestMapping annotated method.
     * 2 goals:
     * - Make sure we always have fresh data
     * - Since we do not use the session scope, make sure that Kid object always has an id
     * (Even though id is not part of the form fields)
     *
     * @param kidId
     * @return Kid
     */
    @ModelAttribute("visit")
    public Visit loadKidWithVisit(@PathVariable("kidId") int kidId, Map<String, Object> model) {
        Kid kid = this.kids.findById(kidId);
        model.put("kid", kid);
        Visit visit = new Visit();
        kid.addVisit(visit);
        return visit;
    }

    // Spring MVC calls method loadKidWithVisit(...) before initNewVisitForm is called
    @RequestMapping(value = "/parents/*/kids/{kidId}/visits/new", method = RequestMethod.GET)
    public String initNewVisitForm(@PathVariable("kidId") int kidId, Map<String, Object> model) {
        return "kids/createOrUpdateVisitForm";
    }

    // Spring MVC calls method loadKidWithVisit(...) before processNewVisitForm is called
    @RequestMapping(value = "/parents/{parentId}/kids/{kidId}/visits/new", method = RequestMethod.POST)
    public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return "kids/createOrUpdateVisitForm";
        } else {
            this.visits.save(visit);
            return "redirect:/parents/{parentId}";
        }
    }

}
