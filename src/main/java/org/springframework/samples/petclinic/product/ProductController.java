package org.springframework.samples.petclinic.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



    @RestController

    class ProductController {




        @GetMapping("/products")
        public String showProductList() {
            return "Retornou o Product List!!!";


        }

    }

