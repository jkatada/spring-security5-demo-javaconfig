package com.example.security.domain.welcome;

import reactor.core.publisher.Mono;

public interface WelcomeService {

    Mono<String> hello();

    Mono<String> helloAdmin();

    Mono<String> helloAdmin(String token);

}
