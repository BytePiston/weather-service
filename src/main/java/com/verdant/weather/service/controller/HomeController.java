package com.verdant.weather.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HomeController {

	@GetMapping("/")
	public RedirectView home() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/swagger-ui/index.html");
		return redirectView;
	}

}
