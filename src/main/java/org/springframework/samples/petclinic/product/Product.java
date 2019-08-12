package org.springframework.samples.petclinic.product;

import org.springframework.samples.petclinic.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="products")
public class Product extends BaseEntity {
    private String description;

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
