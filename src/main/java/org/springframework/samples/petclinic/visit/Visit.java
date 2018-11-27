/*
 * Copyright 2002-2018 the original author or authors.
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
package org.springframework.samples.petclinic.visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 * @author Dave Syer
 */
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {
    
    @Column(name = "visit_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @Column(name = "vet_id")
    private Integer vetId;
    
    @Column(name = "pet_id")
    private Integer petId;


    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "visit_cd")
    private char visitCd;
    
    @Column(name = "visit_category")
    private char visitCategory;
    
    @Column(name = "result_cd")
    private char resultCd;
    
    @Column(name = "result_category")
    private char resultCategory;
    
    @ManyToOne
    @JoinColumn(name = "petid")
    private Pet pet;

	//
    /**
     * Creates a new instance of Visit for the current date
     */
    public Visit() {
        this.date = LocalDate.now();
    }
    
    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVetId() {
        return this.vetId;
    }

    public void setVetId(Integer vetId) {
        this.vetId = vetId;
    }
    
    public Integer getPetId() {
        return this.petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public char getVisitCd() {
        return this.visitCd;
    }

    public void setVisitCd(char visitCd) {
        this.visitCd = visitCd;
    }

    public char getVisitCategory() {
        return this.visitCategory;
    }

    public void setVisitCategory(char visitCategory) {
        this.visitCategory = visitCategory;
    }
    
    public char getResultCd() {
        return this.resultCd;
    }

    public void setResultCd(char resultCd) {
        this.resultCd = resultCd;
    }
    
    public char getResultCategory() {
        return this.resultCategory;
    }

    public void setResultCategory(char resultCategory) {
        this.resultCategory = resultCategory;
    }
    
    
}
