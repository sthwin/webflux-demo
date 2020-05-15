package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User
 * Date: 2020. 5. 15. 오전 10:57
 */
public class FluxTest {
    public static void main(String[] args) throws Exception {

        List<String> mergedList = new ArrayList<>();
        ParallelFlux.from(Flux.just(1, 2, 3, 4))
                .runOn(Schedulers.parallel())
                .map(val -> {
                    int i = 0;
                    while (i < 5) {
                        try {
                            Thread.sleep(1000);
                            System.out.println(Thread.currentThread().getName());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                    return Thread.currentThread().getName();
                })
                //.reduce(mergedList::add)
                .subscribe(val -> { });

        Thread.sleep(10000);

//        Flux.just(1, 2, 3, 4)
//                .parallel(2)
//                .map(val -> val * 2)
//                //.subscribeOn(Schedulers.boundedElastic())
//                .subscribe(val -> System.out.println(Thread.currentThread().getName() + ""));


    }
}
