/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.owner;

import java.util.Objects;

/**
 * @author Gibran
 */
public class StaticOwner {

    private String address;
    private String city;
    private String telephone;
    private Integer id;
    private String lastName;
    private String firstName;

    public StaticOwner(Integer id, String lastName, String firstName, String address, String city, String telephone)
    {
        this.setId(id);
        this.setLastName(lastName);
        this.setFirstName(firstName);
        this.setAddress(address);
        this.setCity(city);
        this.setTelephone(telephone);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Owner owner = (Owner) o;

        return Objects.equals(address, owner.getAddress()) &&
            Objects.equals(city, owner.getCity()) &&
            Objects.equals(telephone, owner.getTelephone()) &&
            Objects.equals(firstName, owner.getFirstName()) &&
            Objects.equals(lastName, owner.getLastName()) &&
            Objects.equals(id, owner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, city, telephone);
    }

    public String getAddress () {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getTelephone () {
        return telephone;
    }


    public static StaticOwner convertToStaticOwner(Owner owner) {
        return new StaticOwner(owner.getId(), owner.getLastName(), owner.getFirstName(),
            owner.getAddress(), owner.getCity(), owner.getTelephone());
    }

}
