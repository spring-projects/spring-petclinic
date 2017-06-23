/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.kidclinic.parent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.kidclinic.model.Person;

/**
 * Simple JavaBean domain object representing an parent.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Entity
@Table(name = "parents")
public class Parent extends Person {
    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "city")
    @NotEmpty
    private String city;
    
    @Column(name = "state")
    @NotEmpty
    private String state;
    
    @Column(name = "telephone")
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    private Set<Kid> kids;


    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState(){
    	return this.state;
    }
    
    public void setState(String state){
    	this.state = state;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    protected Set<Kid> getKidsInternal() {
        if (this.kids == null) {
            this.kids = new HashSet<>();
        }
        return this.kids;
    }

    protected void setKidsInternal(Set<Kid> kids) {
        this.kids = kids;
    }

    public List<Kid> getKids() {
        List<Kid> sortedKids = new ArrayList<>(getKidsInternal());
        PropertyComparator.sort(sortedKids, new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedKids);
    }

    public void addKid(Kid kid) {
        if (kid.isNew()) {
            getKidsInternal().add(kid);
        }
        kid.setParent(this);
    }

    /**
     * Return the Kid with the given name, or null if none found for this Parent.
     *
     * @param name to test
     * @return true if kid name is already in use
     */
    public Kid getKid(String name) {
        return getKid(name, false);
    }

    /**
     * Return the Kid with the given name, or null if none found for this Kid.
     *
     * @param name to test
     * @return true if kid name is already in use
     */
    public Kid getKid(String name, boolean ignoreNew) {
        name = name.toLowerCase();
        for (Kid kid : getKidsInternal()) {
            if (!ignoreNew || !kid.isNew()) {
                String compName = kid.getName();
                compName = compName.toLowerCase();
                if (compName.equals(name)) {
                    return kid;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)

            .append("id", this.getId())
            .append("new", this.isNew())
            .append("lastName", this.getLastName())
            .append("firstName", this.getFirstName())
            .append("address", this.address)
            .append("city", this.city)
            .append("telephone", this.telephone)
            .toString();
    }
}
