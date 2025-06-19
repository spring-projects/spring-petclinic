package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class CrashController {

    @GetMapping("/oups")
    public String triggerException() {
        throw new CustomRuntimeException("Expected: controller used to showcase what happens when an exception is thrown");
    }

}