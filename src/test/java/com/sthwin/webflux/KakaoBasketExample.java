package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Created by User
 * Date: 2020. 5. 15. 오후 3:34
 */
public class KakaoBasketExample {
    public static void main(String[] args) {
        final List<String> basket1 = Arrays.asList(new String[]{"kiwi", "orange", "lemon", "orange", "lemon", "kiwi"});
        final List<String> basket2 = Arrays.asList(new String[]{"banana", "lemon", "lemon", "kiwi"});
        final List<String> basket3 = Arrays.asList(new String[]{"strawberry", "orange", "lemon", "grape", "strawberry"});
        final List<List<String>> baskets = Arrays.asList(basket1, basket2, basket3);
        final Flux<List<String>> basketFlux = Flux.fromIterable(baskets);


        basketFlux.concatMap(basket -> {

             Flux.fromIterable(basket).distinct().collectList();
            distinctFruits.subscribe(val -> System.out.println(val.toString()));
            //System.out.println(basket.toString());
            return distinctFruits;
        }).subscribe();


//                .distinct()
//                .collectList()
//                .subscribe(val -> System.out.println(val.toString()));

    }
}
