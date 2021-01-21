package com.mvc.ws.clients.photoappwebclient.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.mvc.ws.clients.photoappwebclient.response.AlbumRest;

@Controller
public class AlbumsController {
	
	@Autowired
	OAuth2AuthorizedClientService oauth2ClientService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/test")
	public String test() {
		return "tested";
	}

	@GetMapping("/albums")
	public String getAlbums(Model model, 
			@AuthenticationPrincipal OidcUser principal) {
		
		System.out.println("getAlbums called ");
		
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Authentication " + authentication.getName());
		
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		System.out.println("oauthToken " + oauthToken.getName() +" oauthToken.getAuthorizedClientRegistrationId() " + oauthToken.getAuthorizedClientRegistrationId());
		
		OAuth2AuthorizedClient oauth2Client = oauth2ClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), 
				oauthToken.getName());
		System.out.println("oauth2Client " + oauth2Client);
		
		String jwtAccessToken = oauth2Client.getAccessToken().getTokenValue();
		System.out.println("jwtAccessToken = " + jwtAccessToken);
		
		
		OidcIdToken idToken = principal.getIdToken();
		String idTokenValue = idToken.getTokenValue();
		System.out.println("idTokenValue = " + idTokenValue);
		
		String url = "http://localhost:8182/albums";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwtAccessToken);
		
		HttpEntity<List<AlbumRest>> entity = new HttpEntity<>(headers);
		
		ResponseEntity<List<AlbumRest>> responseEntity =  restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<AlbumRest>>() {});
 
		List<AlbumRest> albums = responseEntity.getBody();
//		
//		AlbumRest album = new AlbumRest();
//		album.setAlbumId("albumOne");
//		album.setAlbumTitle("Album one title");
//		album.setAlbumUrl("http://localhost:8082/albums/1");
//		
//		AlbumRest album2 = new AlbumRest();
//		album2.setAlbumId("albumTwo");
//		album2.setAlbumTitle("Album two title");
//		album2.setAlbumUrl("http://localhost:8082/albums/2");
 
        model.addAttribute("albums", albums);
		
		return "albums";
	}
	
}
