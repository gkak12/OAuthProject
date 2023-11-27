package com.oauth.dto;

import java.util.Map;

import com.oauth.domain.Role;
import com.oauth.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
	}
	
	public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		return ofNaver("id", attributes);
	}
	
	public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		
		return OAuthAttributes.builder()
				.name((String) response.get("name"))
				.email((String) response.get("email"))
				.attributes(response)
				.nameAttributeKey(userNameAttributeName)
				.build();
	}
	
	public User toEntity() {
		return User.builder()
					.name(name)
					.email(email)
					.role(Role.GUEST)
					.build();
	}
}
