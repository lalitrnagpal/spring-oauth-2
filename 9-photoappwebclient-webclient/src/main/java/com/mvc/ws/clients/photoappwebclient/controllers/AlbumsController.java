package com.mvc.ws.clients.photoappwebclient.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.mvc.ws.clients.photoappwebclient.response.AlbumRest;

@Controller
public class AlbumsController {
	
	@Autowired
	OAuth2AuthorizedClientService oauth2ClientService;
	
	@Autowired
	WebClient webClient;
	
	@GetMapping("/test")
	public String test() {
		return "tested";
	}

	@GetMapping("/albums")
	public String getAlbums(Model model, 
			@AuthenticationPrincipal OidcUser principal) {
		
		System.out.println("getAlbums called ");
		
		String url = "http://localhost:8182/albums";
		
		List<AlbumRest> albums = webClient.get()
									.uri(url)
									.retrieve()
									.bodyToMono(new ParameterizedTypeReference<List<AlbumRest>>() { })
									.block();
 
        model.addAttribute("albums", albums);
		
		return "albums";
	}
	
}
