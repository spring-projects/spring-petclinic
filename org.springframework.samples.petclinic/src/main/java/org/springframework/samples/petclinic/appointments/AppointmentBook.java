package org.springframework.samples.petclinic.appointments;

import java.util.Date;

public interface AppointmentBook {

	Appointments getAppointmentsForToday();
	
	Appointments getAppointmentsForDay(Date day);

	Long createAppointment(AppointmentForm form);

}