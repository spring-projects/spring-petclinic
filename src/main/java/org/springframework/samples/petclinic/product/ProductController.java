package org.springframework.samples.petclinic.product;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/products")
    public String showProductList(){
        return "Hacker 2.0";
    }
}
