package org.springframework.cheapy.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
//@MappedSuperclass
public class User{
	
	@Id
	private String username;

	@NotBlank
	private String password;
	
	boolean enabled;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
