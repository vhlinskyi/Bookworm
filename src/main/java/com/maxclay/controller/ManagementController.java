package com.maxclay.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maxclay.dao.CategoryDao;


@Controller
public class ManagementController {
	
	private final CategoryDao categoryDao;
	
	@Autowired
	public ManagementController(CategoryDao categoryDao) {
		
		this.categoryDao = categoryDao;
	}
	
	@RequestMapping("/management/category")
	public String managementCategory(Model model) {
		
		model.addAttribute("categories", categoryDao.getAll());
		
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		model.addAttribute("list", list);
		return "management_category";
	}

}
