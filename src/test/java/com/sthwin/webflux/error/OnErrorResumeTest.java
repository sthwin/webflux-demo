package com.sthwin.webflux.error;

import reactor.core.publisher.Flux;

import java.util.Random;

/**
 * Created by sthwin on 2020/10/17 4:45 오후
 */
public class OnErrorResumeTest {
    public static void main(String[] args) {
        Random random = new Random();
        Flux<Integer> seq = Flux.range(1, 10)
                .map(x -> {
                    int rand = random.nextInt(8);
                    if (rand == 0) throw new IllegalArgumentException("illarg");
                    if (rand == 1) throw new IllegalStateException("illstate");
                    //if (rand > 2) throw new RuntimeException("rand > 2");
                    return x;
                })
                .onErrorResume(error -> {
                    if (error instanceof IllegalArgumentException) {
                        return Flux.just(21, 22);
                    }
                    if (error instanceof IllegalStateException) {
                        return Flux.just(31, 32);
                    }
                    return Flux.error(error);
                });

        seq.subscribe(System.out::println,
                ex -> System.out.println("error\n" + ex));
    }
}
