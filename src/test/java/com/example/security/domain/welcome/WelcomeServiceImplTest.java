package com.example.security.domain.welcome;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.security.SpringSecurityDemo;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringSecurityDemo.class)
public class WelcomeServiceImplTest {

    @Autowired
    WelcomeService target;

    @WithMockUser(roles = "ADMIN")
    @Test
    public void testHelloAdmin() {
        Mono<String> message = target.helloAdmin();
        StepVerifier
            .create(message)
            .expectNext("Hello Admin")
            .verifyComplete();
    }

    @Test
    public void testHello() {
        Mono<String> message = target.hello();
        StepVerifier
            .create(message)
            .expectNext("Hello!")
            .verifyComplete();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void testHelloAdminWithToken() {
        Mono<String> message = target.helloAdmin("admin_token");
        StepVerifier
            .create(message)
            .expectNext("Hello Admin!")
            .verifyComplete();
    }

    // not login
    @Test
    public void testHelloAdminError01() {
        Mono<String> message = target.helloAdmin("admin_token");
        StepVerifier
            .create(message)
            .expectError(AccessDeniedException.class)
            .verify();
    }
    
    
    // role not match
    @WithMockUser(roles = "USER")
    @Test
    public void testHelloAdminError02() {
        Mono<String> message = target.helloAdmin("admin_token");
        StepVerifier
            .create(message)
            .expectError(AccessDeniedException.class)
            .verify();
    }
    
    // wrong token
    @WithMockUser(roles = "ADMIN")
    @Test
    public void testHelloAdminError03() {
        Mono<String> message = target.helloAdmin("wrong_token");
        StepVerifier
            .create(message)
            .expectError(AccessDeniedException.class)
            .verify();
    }

}
