package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

/**
 * 스케줄러 실행 순서 확인 예제
 * <p>
 * Created by sthwin on 2020/05/17 10:04 오후
 */
public class SchedulerExample {

    static Scheduler schedulerA = Schedulers.newParallel("scheduler-a", 4);
    static Scheduler schedulerB = Schedulers.newParallel("scheduler-b", 4);
    static Scheduler schedulerC = Schedulers.newParallel("scheduler-c", 4);
    static Scheduler schedulerD = Schedulers.newParallel("scheduler-d", 4);

    public static void main(String[] args) throws Exception {
        testParallel();
    }

    public static void testParallel() {

        Scheduler sch = Schedulers.fromExecutor(Executors.newCachedThreadPool());
        Flux.merge(Flux.just(1), Flux.just(2), Flux.just(3))
                //.publishOn(sch)
                .parallel()
                .runOn(sch)
                .map(val -> {
                    System.out.println(String.format("val - (%s), Thread: %s", val, Thread.currentThread().getName()));
                    return val;
                })
                //.log()
                .subscribe();
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

    public static void test1() throws Exception {
        Flux.range(1, 2)
                .map(i -> {
                    System.out.println(String.format("Outer First map - (%s), Thread: %s", i, Thread.currentThread().getName()));
                    return i;
                })
                .subscribeOn(schedulerB)
                .publishOn(schedulerC)
                .concatMap(i -> {
                    System.out.println(String.format("Outer Second map - (%s), Thread: %s", i, Thread.currentThread().getName()));

                    return Flux
                            .range(1, 2)
                            .map(j -> {
                                System.out.println(String.format("First map - (%s.%s), Thread: %s", i, j, Thread.currentThread().getName()));
                                return j;
                            })
                            //.subscribeOn(schedulerB)
                            .map(j -> {
                                System.out.println(String.format("Second map - (%s.%s), Thread: %s", i, j, Thread.currentThread().getName()));
                                return "value " + j;
                            }).publishOn(schedulerD);
                })
                .subscribeOn(schedulerA)
                .subscribe(val -> {
                    System.out.println("Subscribe-" + Thread.currentThread().getName());
                });

        Thread.sleep(2000);
    }
}
