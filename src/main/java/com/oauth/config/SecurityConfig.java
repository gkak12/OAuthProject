package com.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.oauth.service.CustomOAuth2Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final CustomOAuth2Service customOAtuh2Service;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.headers().frameOptions().disable()
			.and()
				.authorizeRequests()
				.antMatchers("/", "/css/**", "/images/**", "/js/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.logout()
					.logoutSuccessUrl("/")
			.and()
				.oauth2Login()
					.userInfoEndpoint()
						.userService(customOAtuh2Service)
					.and()
					.successHandler(oauth2LoginSuccessHandler())
		;
    }
	
	@Bean
    public AuthenticationSuccessHandler oauth2LoginSuccessHandler() {
        return (request, response, authentication) -> {
            // 로그인 성공 후 리다이렉트할 URL 지정
            response.sendRedirect("/test.do");
        };
    }
}
