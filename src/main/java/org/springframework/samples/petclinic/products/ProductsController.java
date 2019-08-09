package org.springframework.samples.petclinic.products;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ProductsController {

    @GetMapping("/products")
    public String showProductList(Model model){
        List<Product> prods = new ArrayList<Product>();
        model.addAttribute("products", prods);
        prods.add(new Product("Produto 1", "p1"));
        prods.add(new Product("Produto 2", "p2"));

        return "products/productsList";
    }
}
