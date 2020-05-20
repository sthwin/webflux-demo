package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Created by sthwin on 2020/05/17 10:04 오후
 */
public class SchedulerExample {

    static Scheduler schedulerA = Schedulers.newParallel("scheduler-a", 4);
    static Scheduler schedulerB = Schedulers.newParallel("scheduler-b", 4);
    static Scheduler schedulerC = Schedulers.newParallel("scheduler-c", 4);

    public static void main(String[] args) throws Exception {
        test1();
    }

    public static void test2() {
        Flux.range(1, 2)
                .map(i -> {
                    System.out.println(String.format("First map - (%s), Thread: %s", i, Thread.currentThread().getName()));
                    return i;
                })
                .subscribeOn(schedulerA)
                .map(i -> {
                    System.out.println(String.format("Second map - (%s), Thread: %s", i, Thread.currentThread().getName()));
                    return i;
                })
                .subscribeOn(schedulerB)
                .map(i -> {
                    System.out.println(String.format("Third map - (%s), Thread: %s", i, Thread.currentThread().getName()));
                    return i;
                })
                .subscribe(val -> System.out.println(String.format("Subscribe - (%s), Thread: %s", val, Thread.currentThread().getName())));
    }

    public static void test1() {
        Flux.range(1, 5)
                .map(i -> {
                    System.out.println(String.format("Outer First map - (%s), Thread: %s", i, Thread.currentThread().getName()));
                    return i;
                })
                .subscribeOn(schedulerA)
                .publishOn(schedulerC)
                .concatMap(i -> {
                    System.out.println(String.format("Outer Second map - (%s), Thread: %s", i, Thread.currentThread().getName()));

                    return Flux
                            .range(1, 2)
                            .map(j -> {
                                System.out.println(String.format("First map - (%s.%s), Thread: %s", i, j, Thread.currentThread().getName()));
                                return j;
                            })
                            .subscribeOn(schedulerB)
                            .map(j -> {
                                System.out.println(String.format("Second map - (%s.%s), Thread: %s", i, j, Thread.currentThread().getName()));
                                return "value " + j;
                            });
                })
                .blockLast();
    }
}
