package org.springframework.samples.petclinic.model.common;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class used to manage Authorization providers
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Entity(name = "AuthProvider")
@Table(name = "auth_providers")
public class AuthProvider extends NamedEntity {

}
