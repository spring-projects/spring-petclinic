package org.springframework.cheapy.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.bind.DefaultValue;

import net.bytebuddy.implementation.bind.annotation.Default;

@Entity
@Table(name = "users")
public class User extends BaseEntity{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String username;

	@NotBlank
	private String password;
	
	
	private Boolean enabled;
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

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
