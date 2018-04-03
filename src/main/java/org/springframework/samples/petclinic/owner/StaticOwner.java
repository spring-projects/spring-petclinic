/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.owner;

/**
 * @author Gibran
 */
public class StaticOwner {

    private String address;
    private String city;
    private String telephone;

    public StaticOwner(String address, String city, String telephone){
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }
}
