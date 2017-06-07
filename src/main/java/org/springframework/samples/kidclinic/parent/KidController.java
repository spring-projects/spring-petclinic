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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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
 */
@Controller
@RequestMapping("/parents/{parentId}")
class KidController {

    private static final String VIEWS_KIDS_CREATE_OR_UPDATE_FORM = "kids/createOrUpdateKidForm";
    private final KidRepository kids;
    private final ParentRepository parents;

    @Autowired
    public KidController(KidRepository kids, ParentRepository parents) {
        this.kids = kids;
        this.parents = parents;
    }

    @ModelAttribute("gender")
    public Collection<KidGender> populateKidGenders() {
        return this.kids.findKidGenders();
    }

    @ModelAttribute("parent")
    public Parent findParent(@PathVariable("parentId") int parentId) {
        return this.parents.findById(parentId);
    }

    @InitBinder("parent")
    public void initParentBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder("kid")
    public void initKidBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new KidValidator());
    }

    @RequestMapping(value = "/kids/new", method = RequestMethod.GET)
    public String initCreationForm(Parent parent, ModelMap model) {
        Kid kid = new Kid();
        parent.addKid(kid);
        model.put("kid", kid);
        return VIEWS_KIDS_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/kids/new", method = RequestMethod.POST)
    public String processCreationForm(Parent parent, @Valid Kid kid, BindingResult result, ModelMap model) {
        if (StringUtils.hasLength(kid.getName()) && kid.isNew() && parent.getKid(kid.getName(), true) != null){
            result.rejectValue("name", "duplicate", "already exists");
        }
        if (result.hasErrors()) {
            model.put("kid", kid);
            return VIEWS_KIDS_CREATE_OR_UPDATE_FORM;
        } else {
            parent.addKid(kid);
            this.kids.save(kid);
            return "redirect:/parents/{parentId}";
        }
    }

    @RequestMapping(value = "/kids/{kidId}/edit", method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable("kidId") int kidId, ModelMap model) {
        Kid kid = this.kids.findById(kidId);
        model.put("kid", kid);
        return VIEWS_KIDS_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/kids/{kidsId}/edit", method = RequestMethod.POST)
    public String processUpdateForm(@Valid Kid kid, BindingResult result, Parent parent, ModelMap model) {
        if (result.hasErrors()) {
            kid.setParent(parent);
            model.put("kid", kid);
            return VIEWS_KIDS_CREATE_OR_UPDATE_FORM;
        } else {
            parent.addKid(kid);
            this.kids.save(kid);
            return "redirect:/parents/{parentId}";
        }
    }

}
