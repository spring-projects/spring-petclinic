package org.springframework.samples.petclinic.product;

public class Product {
    private String description;
    Product(String desc){
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
