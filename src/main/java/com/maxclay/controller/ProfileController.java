package com.maxclay.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maxclay.service.UserDto;

@Controller
public class ProfileController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	 public String showRegistrationForm(Model model) {
		
		UserDto userDto = new UserDto();
		model.addAttribute("user", userDto);
		return "signup";
	 }
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String registerUserAccount(@ModelAttribute("user") @Valid UserDto accountDto, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
			return "/signup";
		
		System.out.println(accountDto);
		return "redirect:/";
	}
	
}
