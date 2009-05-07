package org.springframework.samples.petclinic.appointments;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/appointments")
public class AppointmentsController {

	private AppointmentBook appointmentBook;
	
	@Autowired
	public AppointmentsController(AppointmentBook appointmentBook) {
		this.appointmentBook = appointmentBook;
	}

	@RequestMapping(method = RequestMethod.GET)
	public Appointments get() {
		return appointmentBook.getAppointmentsForToday();
	}

	@RequestMapping(value="/{day}", method = RequestMethod.GET)
	public Appointments getForDay(@PathVariable Date day) {
		return appointmentBook.getAppointmentsForDay(day);
	}

	@RequestMapping(value="/new", method = RequestMethod.GET)
	public AppointmentForm getNewForm() {
		return new AppointmentForm();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String post(AppointmentForm form) {
		appointmentBook.createAppointment(form);
		return "redirect:/appointments";
	}	
}
