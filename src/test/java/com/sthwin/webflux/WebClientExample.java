package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

/**
 * Created by User
 * Date: 2020. 5. 25. 오전 8:40
 */
public class WebClientExample {
    public static void main(String[] args) throws Exception {
        Flux.merge(Flux.just(1), Flux.just(2))
                .parallel()
                .runOn(Schedulers.elastic())
                .ordered((val1, val2) -> val2 - val1)
                .subscribe(System.out::println);

        List<Integer> list = List.of(3, 2, 1);
        Flux.fromIterable(list)
                .parallel()
                .runOn(Schedulers.elastic())
                //.flatMap(WebClientExample::echo)
                .ordered((v1, v2) -> v1 - v2)
                .subscribe(System.out::println);


        Thread.sleep(1000);
    }

    public static Mono<Integer> echo(int val) {
        return Mono.just(val);
    }
}
