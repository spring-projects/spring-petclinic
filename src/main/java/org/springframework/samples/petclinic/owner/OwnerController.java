/*
 * Copyright 2012-2018 the original author or authors.
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
package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class OwnerController{
	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;
	
	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private final OwnerRepository owners;
    
    
    public OwnerController(OwnerRepository clinicService) {
        this.owners = clinicService;
    }

   
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/owners/new")
    public String initCreationForm(Map<String, Object> model) {
        Owner owner = new Owner();
        model.put("owner", owner);
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/owners/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
  
    	if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
        	String s=toEncryptedHashValue("SHA-512",owner.getPassword());
            owner.setPassword(s);
        	this.owners.save(owner);
            return "redirect:/owners/" + owner.getId();
        }
    }


	@GetMapping("/owners/find")
    public String initFindForm(Map<String, Object> model,Model model2) {
        model.put("owner", new Owner());
        boolean s=(boolean)session.getAttribute("flag");
        if (s) {
        	model2.addAttribute("flag",true);
        }
        return "owners/findOwners";
    }

    @GetMapping("/owners")
    public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /owners to return all records
        if (owner.getLastName() == null) {
            owner.setLastName(""); // empty string signifies broadest possible search
        }

        // find owners by last name
        Collection<Owner> results = this.owners.findByLastName(owner.getLastName());
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        } else if (results.size() == 1) {
            // 1 owner found
            owner = results.iterator().next();
            return "redirect:/owners/" + owner.getId();
        } else {
            // multiple owners found
            model.put("selections", results);
            return "owners/ownersList";
        }
    }

    @GetMapping("/owners/{ownerId}/edit")
    public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
        Owner owner = this.owners.findById(ownerId);
        model.addAttribute(owner);
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/owners/{ownerId}/edit")
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId) {
    	if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            owner.setId(ownerId);
            this.owners.save(owner);
            return "redirect:/owners/{ownerId}";
        }
    }

    /**
     * Custom handler for displaying an owner.
     *
     * @param ownerId the ID of the owner to display
     * @return a ModelMap with the model attributes for the view
     */
    @GetMapping("/owners/{ownerId}")
    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(this.owners.findById(ownerId));
        return mav;
    }
    
    @GetMapping("/login")
    public String login(Owner owner,Map<String, Object> model) {
        return "login";
    }

    @PostMapping("/loginh")
    public String loginh(@Valid Owner owner, BindingResult result, Map<String, Object> model,Model model2) {
    	 if (owner.getPassword()!=null) {
    		 String s=toEncryptedHashValue("SHA-512",owner.getPassword());
             owner.setPassword(s);
        	 Owner results1 = this.owners.findByEmailAndPass(owner.getEmail(),owner.getPassword());
        	 if (results1!=null) {
        		 model2.addAttribute("flag",true);
        		 model2.addAttribute("loginName",results1.getLastName()+" Welcome");
        		 session.setAttribute("flag", true);
         		return "welcome";
        	 }
    	 }
    	 
    	 
    	 Owner results2 = this.owners.findByOrEmail(owner.getEmail());
    	 Collection<Owner> results3 = this.owners.findByOrPass(owner.getPassword());
    		if (results2==null && results3.isEmpty()) {
    			result.rejectValue("email", "notFound", "not found");
    			result.rejectValue("password", "notFound", "not found");
    			return "login";
        	}else if (results2==null){
        		result.rejectValue("email", "notFound", "not found");
        		return "login";
        	}else if (results3.isEmpty()){
        		result.rejectValue("password", "notFound", "not found");
        		return "login";
        	}
    		
    		return "login";
 
    }

    private String toEncryptedHashValue(String algorithmName, String value) {
        MessageDigest md = null;
        StringBuilder sb = null;
        try {
            md = MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(value.getBytes());
        sb = new StringBuilder();
        for (byte b : md.digest()) {
            String hex = String.format("%02x", b);
            sb.append(hex);
        }
        return sb.toString();
    }

}
