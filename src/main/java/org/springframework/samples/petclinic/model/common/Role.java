package org.springframework.samples.petclinic.model.common;

import org.springframework.samples.petclinic.model.common.NamedEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Role")
@Table(name = "roles")
public class Role extends NamedEntity implements Serializable {

}
