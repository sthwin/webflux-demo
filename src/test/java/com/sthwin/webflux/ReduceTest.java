package com.sthwin.webflux;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User
 * Date: 2020. 5. 15. 오전 11:26
 */
public class ReduceTest {
    public static void main(String[] args) {
        List<Integer> mergedList = new ArrayList<Integer>();
        Flux.just(1, 2, 3, 4)
                .log()
                .reduce(mergedList, (a, b) -> {
                    mergedList.add(b);
                    return mergedList;
                }).subscribe(System.out::println);
    }
}
