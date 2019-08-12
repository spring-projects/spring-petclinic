package org.springframework.samples.petclinic.product;

import org.springframework.data.repository.Repository;

import java.util.Collection;

public interface ProductRepository extends Repository<Product, Integer>{
    Collection<Product> findAll();
}
