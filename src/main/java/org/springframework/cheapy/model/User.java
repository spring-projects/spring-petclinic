package org.springframework.cheapy.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


//@Entity
//@Table(name = "users")
@MappedSuperclass
public class User{
	
	@Id
	private String username;

	@NotBlank
	private String password;
	
	boolean enabled;
	
	//@OneToMany(cascade = CascadeType.ALL, mappedBy = "usern")
	//Set<Authorities> authorities;

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

//	public Set<Authorities> getAuthority() {
//		return authorities;
//	}
//
//	public void setAuthorities(Set<Authorities> authorities) {
//		this.authorities = authorities;
//	}
}
