package org.springframework.samples.petclinic.appointments;

import java.util.Date;

public class Appointment {
	
	private String owner;
	
	private String ownerPhone;
	
	private String pet;
	
	private String notes;
	
	private Date dateTime;
	
	public String getOwner() {
		return owner;
	}
	
	public String getOwnerPhone() {
		return ownerPhone;
	}
	
	public String getPet() {
		return pet;
	}
	
	public Date getDateTime() {
		return dateTime;
	}

	public String getNotes() {
		return notes;
	}	
	
}
