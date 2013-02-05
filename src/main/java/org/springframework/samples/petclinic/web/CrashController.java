
package org.springframework.samples.petclinic.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller used to showcase what happens when an exception is thrown
 * 
 * @author Michael Isvy
 */
@Controller
public class CrashController {	

	@RequestMapping(value="/oups", method = RequestMethod.GET)
	public String triggerException() {
		throw new RuntimeException("Something went wrong...");
	}

	

}
