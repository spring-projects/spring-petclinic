package org.springframework.samples.petclinic.appointments;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/appointments")
public class AppointmentsController {

	@RequestMapping(method = RequestMethod.GET)
	public void get() {
		
	}
	
}
