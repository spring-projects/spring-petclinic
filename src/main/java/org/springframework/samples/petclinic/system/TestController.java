package org.springframework.samples.petclinic.system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(String input) {
        // This code has potential issues for testing the review system
        if (input == null) {
            return null; // Should probably throw exception or return default
        }
        
        // No input validation
        return "Hello " + input;
    }
    
    @GetMapping("/unsafe")
    public String unsafeMethod(String data) {
        // Direct string concatenation without validation
        return "Processing: " + data.toLowerCase();
    }
}