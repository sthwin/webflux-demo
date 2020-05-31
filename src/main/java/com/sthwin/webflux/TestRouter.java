package com.sthwin.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Created by sthwin on 2020/05/30 6:00 오후
 */
@Component
public class TestRouter {

    @Bean
    public RouterFunction<ServerResponse> routeRequest(TestHandler handler) {
        return RouterFunctions.route(
                RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                handler::handleRequest);
    }
}
