package org.springframework.samples.petclinic.owner;

public class GreetingController
{
}
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/greeting")
public class GreetingController {

    @GetMapping
    public String greeting() {
        return "greeting";
    }
}
