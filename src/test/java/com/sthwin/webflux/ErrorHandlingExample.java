package com.sthwin.webflux;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by sthwin on 2020/05/30 7:29 오후
 */
public class ErrorHandlingExample {
    public static void main(String[] args) {
        testRetryWhen();
        //testRetry();
    }

    public static void testRetryWhen() {
        Flux<String> flux = Flux
                .<String>error(new IllegalArgumentException())
                .doOnError(err -> System.out.println("doOnError ->" + err))
                // companion 은 Flux<RetrySignal> 이다.
                .retryWhen(Retry.from(companion -> {
                    System.out.println("companion ->" + companion.toString());
                    return companion.take(3);
                    //return companion;
                }));

        flux.subscribe(val -> System.out.println("sub ->" + val));
        testFluxError();
    }

    public static void testFluxError() {
        Flux.interval(Duration.ofSeconds(1))
                .flatMap(val -> {
                    if (val > 5) {
                        return Flux.error(new Exception("test"));
                    }
                    return Flux.just(val);
                })
                .retry(1)
                .subscribe(val -> {
                    System.out.println(val);
                });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void testOnError() {
        Flux<String> flux =
                Flux.interval(Duration.ofMillis(250))
                        .map(input -> {
                            if (input < 3) return "tick " + input;
                            throw new RuntimeException("boom");
                        })
                        .onErrorReturn("Uh oh");

        flux.subscribe(System.out::println);
        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testUsing() {
        AtomicBoolean isDisposed = new AtomicBoolean();

        Flux<Integer> flux =
                Flux.using(
                        () -> {
                            return new Disposable() {
                                @Override
                                public void dispose() {
                                    System.out.println("called dispose !!");
                                    isDisposed.set(true);
                                }

                                @Override
                                public String toString() {
                                    return "DISPOSABLE";
                                }

                                public Flux getData() {
                                    return Flux.just(1, 2, 3, 4);
                                }
                            };
                        },
                        //disposable -> Flux.just(disposable.toString()),
                        disposable -> disposable.getData(),
                        Disposable::dispose
                );
        flux.subscribe(System.out::println);
    }

    public static void testDoFinally() {
        //Stats stats = new Stats();
        LongAdder statsCancel = new LongAdder();

        Flux<String> flux =
                Flux.just("foo", "bar")
                        //.doOnSubscribe(s -> stats.startTimer())
                        .doFinally(type -> {
                            System.out.println("stats stop");
                            //  stats.stopTimerAndRecordTiming();
                            if (type == SignalType.CANCEL)
                                statsCancel.increment();
                        })
                        .take(1);
        flux.subscribe(System.out::println);
    }

    public static void test_1() {
        Flux.just(0, 2, 1)
                .map(ErrorHandlingExample::doSomethingDangerous) //this triggers an error with 0
                .onErrorReturn(e -> e.getMessage().equals("/ by zero"), "recovered10")
                .onErrorReturn("Divided by zero :(") // error handling example
//                .map(val -> {
//                    return "second map: " + val;
//                })
                .subscribe(System.out::println, err -> System.out.println(err.getMessage()));
    }

    public static String doSomethingDangerous(int i) {
        return "100 / " + i + " = " + (100 / i);
    }

    public static void testRetry() {
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) return "tick " + input;
                    throw new RuntimeException("boom");
                })
                .doOnError(System.out::println)
                .retry(1)
                .elapsed()
                .subscribe(System.out::println, System.err::println);

        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
