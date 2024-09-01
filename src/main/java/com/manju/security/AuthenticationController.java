package com.manju.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthenticationController {
	

		@Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtTokenUtil jwtTokenUtil;

	    @Autowired
	    private JwtUserDetailsService userDetailsService;

	    @PostMapping("/getToken")
	    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

	        try {
	            try {
					authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
				} catch (Exception e) {
					e.printStackTrace();
				}
	        } catch (DisabledException e) {
	            return new ResponseEntity<>(new JwtResponse("User disabled"), HttpStatus.FORBIDDEN);
	        } catch (BadCredentialsException e) {
	            return new ResponseEntity<>(new JwtResponse("Invalid credentials"), HttpStatus.UNAUTHORIZED);
	        }

	        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	        final String token = jwtTokenUtil.generateToken(userDetails);

	        return ResponseEntity.ok(new JwtResponse(token));
	    }

	    private void authenticate(String username, String password) throws Exception {
	        try {
	            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	        } catch (DisabledException | BadCredentialsException e) {
	            throw e;  // Re-throw to be handled in the controller method
	        }
	    }
	}


