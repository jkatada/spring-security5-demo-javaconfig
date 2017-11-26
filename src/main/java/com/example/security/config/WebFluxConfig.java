package com.example.security.config;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/resources/app")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES));
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ViewResolver viewResolver = viewResolver();
        registry.viewResolver(viewResolver);
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setDefaultCharset(StandardCharsets.UTF_8);
        return resolver;
    }

    @Bean
    public ISpringWebFluxTemplateEngine templateEngine() {
        SpringWebFluxTemplateEngine engine = new SpringWebFluxTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }
}
