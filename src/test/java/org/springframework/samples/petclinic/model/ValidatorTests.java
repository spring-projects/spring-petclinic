package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 * when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
public class ValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test //fistnameが空白の時
    public void shouldNotValidateWhenFirstNameEmpty() {

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Person person = new Person();
        person.setFirstName("");
        person.setLastName("ああ");

        Validator validator = createValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator
                .validate(person);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }
    
    @Test //lastnameが空白の時
    public void shouldNotValidateWhenLastNameEmpty() {

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Person person = new Person();
        person.setFirstName("aa");
        person.setLastName("");

        Validator validator = createValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator
                .validate(person);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("lastName");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

    @Test //addressが空白の時
    public void shouldNotValidateWhenAddressEmpty() {

	    LocaleContextHolder.setLocale(Locale.ENGLISH);
        
        Owner owner = new Owner();
        owner.setFirstName("aa");
        owner.setLastName("aa");
        owner.setAddress("");
        owner.setCity("sss");
        owner.setTelephone("1988");

        Validator validator = createValidator();
        Set<ConstraintViolation<Owner>> constraintViolations = validator
                .validate(owner);

        
        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }
    
    @Test //cityが空白の時
    public void shouldNotValidateWhenCityEmpty() {

	    LocaleContextHolder.setLocale(Locale.ENGLISH);
        
        Owner owner = new Owner();
        owner.setFirstName("aa");
        owner.setLastName("aa");
        owner.setAddress("aaa");
        owner.setCity("");
        owner.setTelephone("1988");

        Validator validator = createValidator();
        Set<ConstraintViolation<Owner>> constraintViolations = validator
                .validate(owner);

        
        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("city");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }
    
   @Test //telephoneが空白の時
    public void shouldNotValidateWhenTelephoneEmpty() {

	    LocaleContextHolder.setLocale(Locale.ENGLISH);
        
        Owner owner = new Owner();
        owner.setFirstName("aa");
        owner.setLastName("aa");
        owner.setAddress("aaa");
        owner.setCity("sss");
        owner.setTelephone("");

        Validator validator = createValidator();
        Set<ConstraintViolation<Owner>> constraintViolations = validator
                .validate(owner);

        
        //assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("telephone");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    } 
   
}
