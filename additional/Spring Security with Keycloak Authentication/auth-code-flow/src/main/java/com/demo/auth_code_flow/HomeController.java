package com.demo.auth_code_flow;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String hello(OAuth2AuthenticationToken token) {
        System.out.println(token.getPrincipal());
        String email = token.getPrincipal().getAttribute("email");
        String name = token.getPrincipal().getAttribute("name");
        String roles = token.getAuthorities().toString();
        return "Welcome " + name + ", " + email + ", " + roles;
    }
}
