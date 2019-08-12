package org.springframework.samples.petclinic.employer;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class EmployerController{

    @GetMapping("/employers")
    public String showEmployerList(Model model){
        List<Employer> emplo = new ArrayList<Employer>();
        model.addAttribute("employers",emplo);
        emplo.add(new Employer("Geio",2900));
        emplo.add(new Employer("Poua",5000));
        emplo.add(new Employer("Heijo",3500));
        emplo.add(new Employer("Reina",7502));
        emplo.add(new Employer("Ghest",4500));
        emplo.add(new Employer("Undermetal",2900));
        emplo.add(new Employer("Rolando",2900));
        return "employers/employersList";
    }
}
