package com.sthwin.webflux;

import reactor.core.publisher.Flux;

/**
 * Created by sthwin on 2020/05/30 7:29 오후
 */
public class ErrorHandlingExample {
    public static void main(String[] args) {
        Flux.just(0, 2, 1)
                .map(i -> "100 / " + i + " = " + (100 / i)) //this triggers an error with 0
                .onErrorReturn("Divided by zero :(") // error handling example
                .map(val -> {
                    return "second map: " + val;
                })
                .subscribe(System.out::println, err -> System.out.println(err.getMessage()));
    }
}
