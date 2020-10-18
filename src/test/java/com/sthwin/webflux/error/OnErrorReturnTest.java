package com.sthwin.webflux.error;

import reactor.core.publisher.Flux;

/**
 * Created by sthwin on 2020/10/17 4:28 오후
 */
public class OnErrorReturnTest {
    public static void main(String[] args) {
        Flux<Integer> seq = Flux.range(1, 10)
                .map(x -> {
                    if (x == 5) throw new RuntimeException("exception");
                    else return x;
                })
                //.onErrorReturn(Integer.valueOf(-1));
                //.onErrorReturn(Exception.class, Integer.valueOf(-1));
                .onErrorReturn(ex -> ex instanceof Exception, Integer.valueOf(-1));
        seq.subscribe(System.out::println);
    }
}
