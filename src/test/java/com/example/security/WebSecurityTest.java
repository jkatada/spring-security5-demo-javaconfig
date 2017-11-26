package com.example.security;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringSecurityDemo.class)
public class WebSecurityTest {

    @Autowired
    ApplicationContext context;

    WebTestClient client;

    @Before
    public void setUp() {
        client = WebTestClient
                    .bindToApplicationContext(context)
                    .apply(springSecurity())
                    .configureClient()
                    .build();
    }

    @Test
    public void testFormLogin() throws Exception {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("username", "user");
        data.add("password", "password");
        client
            .mutateWith(csrf())
            .post()
            .uri("/login")
            .body(BodyInserters.fromFormData(data))
            .exchange()
            .expectStatus().isFound()
            .expectHeader().valueEquals("Location", "/");
    }

    @WithMockUser // simulate user login state
    @Test
    public void testLogout() throws Exception {
        client
            .mutateWith(csrf())
            .post()
            .uri("/logout")
            .exchange()
            .expectStatus().isFound()
            .expectHeader().valueEquals("Location", "/login?logout");
    } 

    @WithMockUser(roles = "USER")
    @Test
    public void testPermission01() throws Exception {
        client
            .get()
            .uri("/")
            .exchange()
            .expectStatus().is2xxSuccessful();
    }

    @Test
    public void testPermission02() throws Exception {
        client
            .get()
            .uri("/")
            .exchange()
            .expectStatus().isFound()
            .expectHeader().valueEquals("Location", "/login");
    }

    @WithMockUser(roles = "USER")
    @Test
    public void testCsrf() throws Exception {
        client
            .mutateWith(csrf())
            .post()
            .uri("/")
            .exchange()
            .expectStatus().is2xxSuccessful();
    }

    @WithMockUser(roles = "USER")
    @Test
    public void testCsrfError() throws Exception {
        client
            .post()
            .uri("/")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

}
