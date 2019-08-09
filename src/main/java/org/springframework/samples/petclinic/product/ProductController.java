package org.springframework.samples.petclinic.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
@RestController
public class ProductController {
    @GetMapping("/products")
    public String showProductList(Model model){
        List<Product> prods = new ArrayList<Product>();
        model.addAttribute("product",prods);
        //prods.add(new Product("p1");
        //prods.add(new Product("P2"));
        return "products/productsList";
    }

    }

