package org.springframework.samples.petclinic.system;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WelcomeController {
	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;
	
    @GetMapping("/")
    public String welcome(Model model) {

    	session.setAttribute("flag", false);

    	model.addAttribute("flag",false);
        return "welcome";
    }
    
    
}
