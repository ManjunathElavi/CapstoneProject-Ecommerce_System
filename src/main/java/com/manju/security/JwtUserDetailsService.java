package com.manju.security;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ("javatrainingschool".equals(username)) {
			return new User("javatrainingschool", "$2a$10$vJ/QTBBNEXdwtTI01NRMguhyhSKaBHTYbe7X.0S1YD03rTnfYNHp6",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException(username + " is not found in the records");
		}
	}
}
