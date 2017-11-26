package com.example.security.domain.welcome;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class WelcomeServiceImpl implements WelcomeService {

    public Mono<String> hello() {
        return Mono.just("Hello!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<String> helloAdmin() {
        return Mono.just("Hello Admin");
    }

    @PreAuthorize("hasRole('ADMIN') and #token == 'admin_token'")
    public Mono<String> helloAdmin(String token) {
        return Mono.just("Hello Admin!");
    }
}
