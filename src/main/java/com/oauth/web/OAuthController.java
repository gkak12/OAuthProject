package com.oauth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OAuthController {

	@RequestMapping(value="/")
	public String getMainPage() {
		return "main";
	}
	
	@RequestMapping(value="/test.do")
	public String getTestPage() {
		return "test";
	}
}
