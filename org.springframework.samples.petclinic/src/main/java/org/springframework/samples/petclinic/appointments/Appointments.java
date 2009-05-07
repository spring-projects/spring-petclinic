package org.springframework.samples.petclinic.appointments;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Appointments {
	
	private Map<String, List<Appointment>> vetAppointments = new LinkedHashMap<String, List<Appointment>>();
	
	public Map<String, List<Appointment>> getAllByVet() {
		return vetAppointments;
	}
	
}
