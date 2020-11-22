package org.springframework.samples.petclinic.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.common.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {

		UserDTO userDTO = userService.findByEmail(email);

		if (userDTO == null)
			throw new UsernameNotFoundException("User not found with email :" + email);

		return userDTO;
	}

}
