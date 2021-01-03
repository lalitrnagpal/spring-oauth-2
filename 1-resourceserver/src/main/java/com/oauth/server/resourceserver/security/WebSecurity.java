package com.oauth.server.resourceserver.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final Log logger = LogFactory.getLog(WebSecurityConfigurerAdapter.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		this.logger.debug("Using our custom configure method of WebSecurity");
		
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/users/status/check")
			// .hasAuthority("SCOPE_profile")
			.hasRole("developer")
			//.hasAnyRole("developer", "user")
			.anyRequest()
			.authenticated()
			.and()
			.oauth2ResourceServer()
			.jwt()
			.jwtAuthenticationConverter(jwtAuthenticationConverter);
//		http.formLogin();
//		http.httpBasic();
	}

}
