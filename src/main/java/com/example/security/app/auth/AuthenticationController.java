package com.example.security.app.auth;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Controller
public class AuthenticationController {

    @ModelAttribute("_csrf")
    public Mono<CsrfToken> setupCsrf(ServerWebExchange exchange) {
        return exchange.getAttribute("csrf");
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "auth/loginForm";
    }

}
