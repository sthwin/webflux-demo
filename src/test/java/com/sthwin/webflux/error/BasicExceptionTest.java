package com.sthwin.webflux.error;

import reactor.core.publisher.Flux;

/**
 * Created by sthwin on 2020/10/17 4:06 오후
 */
public class BasicExceptionTest {
    public static void main(String[] args) {
        Flux.range(1, 10)
                .map(x -> {
                    if (x == 5) throw new RuntimeException("exception"); // 에러 발생
                    else return x;
                })
                .subscribe(
                        i -> System.out.println(i), // next 신호 처리
                        ex -> System.err.println(ex.getMessage()), // error 신호 처리
                        () -> System.out.println("complete") // complete 신호 처리
                );
    }
}
