package com.example.security.app.welcome;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;

import com.example.security.domain.welcome.WelcomeService;

import reactor.core.publisher.Mono;

@Controller
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    WelcomeService service;

    @ModelAttribute("_csrf")
    public Mono<CsrfToken> setupCsrf(ServerWebExchange exchange) {
        return exchange.getAttribute("csrf");
    }

    @RequestMapping(value = "/")
    public String home(Model model, @AuthenticationPrincipal UserDetails user) {
        logger.info("user: " + user.toString());

        Mono<String> message = service.hello();
        model.addAttribute("helloMessage", message);
        model.addAttribute("hasUserRole", hasUserRole(user));
        model.addAttribute("username", user.getUsername());

        return "welcome/home";
    }

    @RequestMapping(value = "/admin", params = "token")
    public String helloAdmin(@RequestParam String token, Model model, @AuthenticationPrincipal UserDetails user) {
        Mono<String> message = service.helloAdmin(token);
        model.addAttribute("hasUserRole", hasUserRole(user));
        model.addAttribute("username", user.getUsername());
        model.addAttribute("helloMessage", message);

        return "welcome/home";
    }

    private boolean hasUserRole(UserDetails user) {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if ("ROLE_USER".equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
