package org.springframework.samples.petclinic.service.common;

public interface SecurityService {

	String findLoggedInUsername();

	void autoLogin(String email, String motDePasse);

}
