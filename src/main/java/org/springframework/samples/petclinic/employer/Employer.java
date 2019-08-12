package org.springframework.samples.petclinic.employer;

public class Employer {
    private String name;
    private double salarie;

    public Employer(String name, double salarie) {
        this.name = name;
        this.salarie = salarie;
    }

    public double getSalarie() {
        return salarie;
    }

    public void setSalarie(double salarie) {
        this.salarie = salarie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
