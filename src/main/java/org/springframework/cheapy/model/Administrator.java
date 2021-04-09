package org.springframework.cheapy.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "administrators")
public class Administrator extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User usuar;

	public User getUsuar() {
		return usuar;
	}

	public void setUsuar(User usuar) {
		this.usuar = usuar;
	}
}
