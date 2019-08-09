package org.springframework.samples.petclinic.product;

public class Product {
    private String description;

    public Product(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
