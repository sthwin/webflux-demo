package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

/**
 * Created by User
 * Date: 2020. 6. 4. 오후 1:51
 */
public class FlatMapVsConcatMapExample {

    public static void main(String[] args) throws Exception {
        Flux.range(0, 5)
                //.flatMap(val -> {
                .concatMap(val -> {
                    return Flux.from(Mono.delay(Duration.ofSeconds(2))).map(sec -> val);
                })
                .subscribe(System.out::println);

        Thread.sleep(10000);

                //.elapsed()
//                .subscribe(t -> {
//                    System.out.println(t.getT1());
//                    System.out.println(t.getT2());
//                });

        // System.out.println(t.getT1());
        //        System.out.println(t.getT2());

        //t.getT2().subscribe();

//        Flux.range(0, 1000)
//                .flatMap(val -> Flux.just(val))
//                .elapsed()
//                .subscribe();
    }
}
