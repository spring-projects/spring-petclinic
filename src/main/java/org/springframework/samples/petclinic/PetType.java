package org.springframework.samples.petclinic;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Juergen Hoeller
 */
@Entity @Table(name="types")
public class PetType extends NamedEntity {

}
