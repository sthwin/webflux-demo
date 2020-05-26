package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;

/**
 * Created by User
 * Date: 2020. 5. 26. 오전 8:27
 */
public class ParallelFluxExample {
    public static void main(String[] args) {
        reduceWithTest();
    }

    public static void reduceTest () {

        /**
         * reduce(BiFunction<T,T,T> reducer):
         * 아래 코드 기준
         * 첫번째 진입시 i, j 는 각각 1, 2 값이 설정됨
         * 두번째 i 에는 함수에서 계산된 값이 설정됨, j 에는 3번째 아이템이 설정됨
         * 최종적으로는 함수에 계산된 값이 반환 됨
         */
        ParallelFlux.from(Flux.just(3, 4, 5))
                .reduce((i, j) -> {
                    System.out.println("i: " + i + ", j:" + j);
                    return i * j;
                })
                .subscribe(System.out::println);
    }

    public static void reduceWithTest () {
        /**
         */
        ParallelFlux.from(Flux.just(3,4))
                .reduce(()-> 1, (i, j) -> {
                    //System.out.println("i: " + i + ", j:" + j);
                    return i + j;
                })
                .subscribe(System.out::println);
    }
}
