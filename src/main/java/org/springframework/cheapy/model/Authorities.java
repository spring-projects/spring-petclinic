package org.springframework.cheapy.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "authorities")
public class Authorities extends BaseEntity{
	
	
	//@ManyToOne
    //@JoinColumn(name = "username")
	//private User usern;

    @Size(min = 3, max = 50)
    private String authority;
	
//	public User getUsername() {
//		return usern;
//	}
//	
//	public void setUser(User user) {
//		this.usern = user;
//	}
	
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
