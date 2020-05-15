package com.sthwin.webflux;

import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

/**
 * Created by User
 * Date: 2020. 5. 15. 오후 4:23
 */
public class DistinctExample {
    public static void main(String[] args) throws Exception {
        final List<String> basket1 = Arrays.asList(new String[]{"kiwi", "orange", "lemon", "orange", "lemon", "kiwi"});

        Flux.fromIterable(basket1)
                .distinct()
                .collectList()
                .subscribe(v -> System.out.println(v.toString()));

        Flux.fromIterable(basket1)
                .groupBy(fruit -> fruit)
                .concatMap(groupedFlux -> groupedFlux.count().map(count -> count))
                .subscribe(v -> {
                    System.out.println(v);
                });


        Flux.fromIterable(basket1)
                .groupBy(fruit -> fruit)
                .map(groupedFlux -> groupedFlux.count().map(count -> count))
                .subscribe(v -> {
                    System.out.println(v);
                });
    }

}
