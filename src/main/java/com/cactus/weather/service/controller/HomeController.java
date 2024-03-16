package com.cactus.weather.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "Home", description = "Redirect to Swagger UI")
@RestController
public class HomeController {

  @GetMapping("/")
  public RedirectView home() {
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl("/swagger-ui/index.html");
    return redirectView;
  }
}
