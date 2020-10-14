package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.*;

/**
 * Created by User
 * Date: 2020. 8. 4. 오전 9:27
 */
public class SubscribeOnTest {
    public static void main(String[] args) throws Exception {
//        Scheduler dbScheduler = Schedulers.newParallel("db");
//        Scheduler appScheduler = Schedulers.newParallel("app");
//
//        Flux.range(1, 1)
//                .subscribeOn(appScheduler)
//                .flatMap(val -> {
//                    Flux<Integer> first = Flux.range(2, 1).log().publishOn(dbScheduler);
//                    Flux<Integer> second = Flux.range(3, 1);
//                    return Flux.zip(second, first).flatMap(tuple2 -> {
//                        System.out.println(String.format("Thread: %s", Thread.currentThread().getName()));
//                        int t1 = tuple2.getT1();
//                        int t2 = tuple2.getT2();
//                        return Flux.just(t1, t2);
//                    }).publishOn(appScheduler);
//                })
//                .subscribe(val -> {
//                    //System.out.println(String.format("Thread: %s", Thread.currentThread().getName()));
//                    System.out.println(val);
//                });
//
//        Thread.sleep(100);
        test();
    }

    public static void test() throws Exception {
        Scheduler dbScheduler = Schedulers.newParallel("db");
        Scheduler appScheduler = Schedulers.newParallel("app");

        Flux.range(1, 1)
                .flatMap(val -> {
                    Flux<Integer> firstFlux = Flux.just(1).log();
                    Flux<Integer> secondFlux = Flux.just(2).log();
                    return Flux.zip(firstFlux, secondFlux).subscribeOn(dbScheduler).publishOn(appScheduler).flatMap(tuple2 -> {
                        System.out.println(String.format("Thread: %s", Thread.currentThread().getName()));
                        return Mono.just(Arrays.asList(tuple2.toArray()));
                    });
                })
                .subscribeOn(appScheduler)
                .subscribe(val -> {
                    //System.out.println(String.format("Thread: %s", Thread.currentThread().getName()));
                    //System.out.println(val);
                });

        //Thread.sleep(100);
    }
}
