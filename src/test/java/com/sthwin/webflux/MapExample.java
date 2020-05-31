package com.sthwin.webflux;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by sthwin on 2020/05/24 12:28 오후
 */
public class MapExample {

    public static void main(String[] args) throws Exception {
//        flatMap();
//        concatMap();
        testMapAndConcatMap();
        Thread.sleep(1000);
    }

    public static void flatMap() {
        Flux.range(1, 10)
                .flatMap(val -> {
                    return Flux.just(val).delayElements(Duration.ofMillis(1));
                })
                .subscribe(val -> System.out.println("flatMap: " + val));
    }

    public static void concatMap() {
        Flux.range(1, 10)
                .concatMap(val -> {
                    return Flux.just(val).delayElements(Duration.ofMillis(1));
                })
                .subscribe(val -> System.out.println("concatMap: " + val));
    }

    public static void concatMapVariation() {
        Flux.range(1, 3)
                .concatMap(val -> {
                    return Flux.range(1 * val, 5).delayElements(Duration.ofMillis(1));
                })
                .subscribe(System.out::println);
    }

    public static void concatMapWithReduce() {
        Flux.range(1, 10)
                .map(val -> {
                    List<Integer> list = new ArrayList();
                    list.add(val);
                    return list;
                })
                .reduce((init, acc) -> {
                    List<Integer> list = new ArrayList();
                    list.addAll(init);
                    list.addAll(acc);
                    return list;
                })
                .subscribe(val -> System.out.println("concatMapVariation: " + val));
    }


    public static void testMapAndConcatMap() {
        Flux.just(1)
                .concatMap(val -> Flux.just(val))
                .subscribe(System.out::println);

        Flux.just(1)
                .map(val -> Flux.just(val))
                .subscribe(System.out::println);

        Flux.just(1)
                .map(val -> val)
                .subscribe(System.out::println);
    }
}
