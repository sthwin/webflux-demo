package com.sthwin.webflux;

import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User
 * Date: 2020. 5. 15. 오후 4:23
 */
public class DistinctExample {
    public static void main(String[] args) throws Exception {
        final List<String> basket1 = Arrays.asList("kiwi", "orange", "lemon", "orange", "lemon", "kiwi");

        Flux.fromIterable(basket1)
                .distinct()
                .collectList()
                .subscribe(v -> System.out.println(v.toString()));

        // Map<String, Long> accumulatedMap = new LinkedHashMap<>();
        Flux.fromIterable(basket1)
                .groupBy(fruit -> fruit)
                .concatMap(groupedFlux -> groupedFlux.count()
                        .map(count -> {
                            final Map<String, Long> fruitCountMap = new LinkedHashMap<>();
                            fruitCountMap.put(groupedFlux.key(), count);
                            return fruitCountMap;
                        }))
                .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<>() {{
                    putAll(accumulatedMap);
                    putAll(currentMap);
                }})
                .subscribe(v -> {
                    System.out.println(v);
                });
    }

}
