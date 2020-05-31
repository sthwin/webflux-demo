package com.sthwin.webflux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Created by sthwin on 2020/05/30 6:06 오후
 */

@Component
public class TestHandler {
    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        return sayHello(request).onErrorResume(e -> Mono.just("Error -> " + e.getMessage()))
                .flatMap(s -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(s));
    }

    private Mono<String> sayHello(ServerRequest request) {
        try {
            return Mono.just("Hello, " + request.queryParam("name").get());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
