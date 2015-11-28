package com.maxclay.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginLogoutController {
		 
	 @RequestMapping("/login")
	 public String authenticate() {

		 return "login";
	 }
	 
	 @RequestMapping("/logout")
		public String admin(HttpServletRequest request) throws ServletException {
		 	request.logout();
			return "redirect:/";
		}

}
