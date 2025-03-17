package com.example.babee_transmission_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
	
	@GetMapping(value = "/{path:^(?!api$).*$}")
	public String redirect() {
		return "index";
	}
	
}