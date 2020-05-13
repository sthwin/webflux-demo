package com.sthwin.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class WebfluxApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4)
                .map(val -> val * 2)
                .subscribe(System.out::println);
    }

}
