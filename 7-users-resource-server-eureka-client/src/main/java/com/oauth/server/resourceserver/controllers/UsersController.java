package com.oauth.server.resourceserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oauth.server.resourceserver.response.UserRest;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	Environment environment;
	
	@GetMapping("/status/check")
	public String status() {
		return "Working on port number -> " + environment.getProperty("local.server.port");
	}
	
	// @Secured("ROLE_developer")
	//@PreAuthorize("hasRole('developer')")
	@PreAuthorize("#id == #jwt.subject")
	@DeleteMapping(path = "/{id}")
	public String deleteUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
		jwt.getClaims().entrySet().iterator().forEachRemaining( ks -> System.out.println(ks.getKey() + "- " + ks.getValue()));
		return "Deleted user with id: " + id + " and JWT subject " + jwt.getSubject();
	}

	@PostAuthorize("returnObject.userId == #jwt.subject")
	@GetMapping(path = "/{id}")
	public UserRest getUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
		return new UserRest("Lalit", "Nagpal", "adb9a0bf-50ff-4c2f-9e11-3260f4c7");
	}	
}
