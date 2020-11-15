package org.springframework.samples.petclinic.service;

public interface SecurityService {

	String findLoggedInUsername();

	void autoLogin(String email, String motDePasse);

}
