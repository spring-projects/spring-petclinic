package org.springframework.cheapy.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "administrators")
public class Administrator extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
