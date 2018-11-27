package org.springframework.samples.petclinic.system;


import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WelcomeController {
	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;
	
    @GetMapping("/")
    public String welcome(Model model) {
        return "welcome";
    }
    
    
}
