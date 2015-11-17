package com.maxclay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
	
	@RequestMapping("/")
	public String hello(@RequestParam("name") String userName, Model model) {
		model.addAttribute("message", "Hello, " + userName);
		return "resultPage";
	}

}
