open module spring.petclinic {
    requires cache.api;

    requires java.activation;
    requires java.persistence;
    requires java.sql;
    requires java.transaction;
    requires java.validation;
    requires java.xml.bind;

    requires org.hibernate.validator;

    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
