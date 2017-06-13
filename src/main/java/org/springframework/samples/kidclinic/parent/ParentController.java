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

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class ParentController {

    private static final String VIEWS_PARENT_CREATE_OR_UPDATE_FORM = "parents/createOrUpdateParentForm";
    private final ParentRepository parents;


    @Autowired
    public ParentController(ParentRepository clinicService) {
        this.parents = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping(value = "/parents/new", method = RequestMethod.GET)
    public String initCreationForm(Map<String, Object> model) {
        Parent parent = new Parent();
        model.put("parent", parent);
        return VIEWS_PARENT_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/parents/new", method = RequestMethod.POST)
    public String processCreationForm(@Valid Parent parent, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_PARENT_CREATE_OR_UPDATE_FORM;
        } else {
            this.parents.save(parent);
            return "redirect:/parents/" + parent.getId();
        }
    }

    @RequestMapping(value = "/parents/find", method = RequestMethod.GET)
    public String initFindForm(Map<String, Object> model) {
        model.put("parent", new Parent());
        return "parents/findParents";
    }

    @RequestMapping(value = "/parents", method = RequestMethod.GET)
    public String processFindForm(Parent parent, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /parents to return all records
        if (parent.getLastName() == null) {
            parent.setLastName(""); // empty string signifies broadest possible search
        }

        // find parents by last name
        Collection<Parent> results = this.parents.findByLastName(parent.getLastName());
        if (results.isEmpty()) {
            // no parents found
            result.rejectValue("lastName", "notFound", "not found");
            return "parents/findParents";
        } else if (results.size() == 1) {
            // 1 parent found
            parent = results.iterator().next();
            return "redirect:/parents/" + parent.getId();
        } else {
            // multiple parents found
            model.put("selections", results);
            return "parents/parentsList";
        }
    }

    @RequestMapping(value = "/parents/{parentId}/edit", method = RequestMethod.GET)
    public String initUpdateParentForm(@PathVariable("parentId") int parentId, Model model) {
        Parent parent = this.parents.findById(parentId);
        model.addAttribute(parent);
        return VIEWS_PARENT_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/parents/{parentId}/edit", method = RequestMethod.POST)
    public String processUpdateParentForm(@Valid Parent parent, BindingResult result, @PathVariable("parentId") int parentId) {
        if (result.hasErrors()) {
            return VIEWS_PARENT_CREATE_OR_UPDATE_FORM;
        } else {
            parent.setId(parentId);
            this.parents.save(parent);
            return "redirect:/parents/{parentId}";
        }
    }

    /**
     * Custom handler for displaying an parent.
     *
     * @param parentId the ID of the parent to display
     * @return a ModelMap with the model attributes for the view
     */
    @RequestMapping("/parents/{parentId}")
    public ModelAndView showParent(@PathVariable("parentId") int parentId) {
        ModelAndView mav = new ModelAndView("parents/parentDetails");
        mav.addObject(this.parents.findById(parentId));
        return mav;
    }

}
