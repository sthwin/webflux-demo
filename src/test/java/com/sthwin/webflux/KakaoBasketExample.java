package com.sthwin.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by User
 * Date: 2020. 5. 15. 오후 3:34
 */
public class KakaoBasketExample {
    public static void main(String[] args) throws Exception {
        final List<String> basket1 = Arrays.asList(new String[]{"kiwi", "orange", "lemon", "orange", "lemon", "kiwi"});
        final List<String> basket2 = Arrays.asList(new String[]{"banana", "lemon", "lemon", "kiwi"});
        final List<String> basket3 = Arrays.asList(new String[]{"strawberry", "orange", "lemon", "grape", "strawberry"});
        final List<List<String>> baskets = Arrays.asList(basket1, basket2, basket3);
        final Flux<List<String>> basketFlux = Flux.fromIterable(baskets);

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        basketFlux.concatMap(basket -> {
            final Flux<String> source = Flux.fromIterable(basket).log().publish().autoConnect(2).subscribeOn(Schedulers.single());
            Mono<List<String>> distinctFruits = source.publishOn(Schedulers.parallel()).distinct().collectList().log();
            Mono<Map<String, Long>> countFruitsMono = source.publishOn(Schedulers.parallel())
                    .groupBy(fruit -> fruit)
                    .concatMap(groupedFlux -> groupedFlux.count()
                            .map(count -> {
                                final Map<String, Long> fruitCountMap = new LinkedHashMap<>();
                                fruitCountMap.put(groupedFlux.key(), count);
                                return fruitCountMap;
                            }))
                    .reduce((accumulatedMap, currentMap) -> {
                        // 새로운 맵에 누적되어 있는 데이터와 추가할 데이터를 계속 통합해서 전달한다.
                        Map<String, Long> tempMap = new LinkedHashMap<>();
                        tempMap.putAll(accumulatedMap);
                        tempMap.putAll(currentMap);
                        return tempMap;
                    }).log();
            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).log()
                .subscribe(System.out::println,  // 값이 넘어올 때 호출 됨, onNext(T)
                        error -> {
                            System.err.println(error);
                            countDownLatch.countDown();
                        }, // 에러 발생시 출력하고 countDown, onError(Throwable)
                        () -> {
                            System.out.println("complete");
                            countDownLatch.countDown();
                        }); // 정상적 종료시 countDown, onComplete());
        countDownLatch.await(2, TimeUnit.SECONDS);
    }
}

class FruitInfo {
    private final List<String> distinctFruits;
    private final Map<String, Long> countFruits;


    FruitInfo(List<String> distinctFruits, Map<String, Long> countFruits) {
        this.distinctFruits = distinctFruits;
        this.countFruits = countFruits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FruitInfo fruitInfo = (FruitInfo) o;
        return Objects.equals(distinctFruits, fruitInfo.distinctFruits) &&
                Objects.equals(countFruits, fruitInfo.countFruits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinctFruits, countFruits);
    }

    @Override
    public String toString() {
        return "FruitInfo{" +
                "distinctFruits=" + distinctFruits +
                ", countFruits=" + countFruits +
                '}';
    }
}