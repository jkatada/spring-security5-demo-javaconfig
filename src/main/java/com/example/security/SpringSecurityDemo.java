package com.example.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import io.netty.channel.Channel;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.http.server.HttpServer;

@ComponentScan
@Configuration
public class SpringSecurityDemo {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringSecurityDemo.class);

        HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(context).build();
        HttpServer httpServer = HttpServer.create("0.0.0.0", 8080);
        Mono<? extends NettyContext> handler = httpServer.newHandler(new ReactorHttpHandlerAdapter(httpHandler));

        Channel channel = handler.block().channel();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                channel.eventLoop().shutdownGracefully().sync();
            } catch (InterruptedException ignore) {
            }
        }));
        channel.closeFuture().sync();

    }

}
