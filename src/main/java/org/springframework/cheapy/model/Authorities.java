package org.springframework.cheapy.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "authorities")
public class Authorities extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	String username;
	
	String authority;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	
//	@ManyToOne
//    @JoinColumn(name = "username")
//	private Usuario user;
//
//    @Size(min = 3, max = 50)
//    private String authority;
//
//	public Usuario getUser() {
//		return user;
//	}
//
//	public void setUser(Usuario usern) {
//		this.user = usern;
//	}
//
//	public String getAuthority() {
//		return authority;
//	}
//
//	public void setAuthority(String authority) {
//		this.authority = authority;
//	}
//
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
	
	
}
