package com.oauth.service;

import java.util.Collections;

import javax.persistence.Entity;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.oauth.domain.User;
import com.oauth.dto.OAuthAttributes;
import com.oauth.dto.SessionUser;
import com.oauth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	private final HttpSession httpSession;
	
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuthUser = delegate.loadUser(userRequest);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
												.getUserInfoEndpoint().getUserNameAttributeName();
		
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuthUser.getAttributes());
		User user = saveOrUpdate(attributes);
		
		httpSession.setAttribute("user", new SessionUser(user));
		
		return new DefaultOAuth2User(
						Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey()))
						, attributes.getAttributes()
						, attributes.getNameAttributeKey()
					);
	}
	
	private User saveOrUpdate(OAuthAttributes attributes) {
		User user = userRepository.findByEmail(attributes.getEmail())
						.map(entity -> entity.update(attributes.getName()))
						.orElse(attributes.toEntity());
		
		return userRepository.save(user);
	}
}
