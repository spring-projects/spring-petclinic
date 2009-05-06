package org.springframework.samples.petclinic.util;

import org.springframework.ui.Model;

// This is an idea as a context object to make avail to @Controller methods to interact with Dispatcher
public interface ExternalContext {

	Model getModel();
	
	void selectView(String viewName);
	
	void renderFragment(String fragment);
	
	void redirect(Object resource);

	ExternalContext forResource(Object resource);

	Object getNativeRequest();

	Object getNativeResponse();

}
