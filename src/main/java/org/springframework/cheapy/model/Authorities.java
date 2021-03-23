package org.springframework.cheapy.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "authorities")
public class Authorities extends BaseEntity{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name = "username")
	private Usuario user;

    @Size(min = 3, max = 50)
    private String authority;

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario usern) {
		this.user = usern;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
