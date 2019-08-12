package org.springframework.samples.petclinic.product;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
public class ProductController {

    private final ProductRepository products;

    public ProductController(ProductRepository products){
        this.products = products;
    }

    @GetMapping("/products")
    public String showProductList(Model model){
        Collection<Product> prods = products.findAll();
        model.addAttribute("products",prods);
        return "products/productsList";
    }
}
