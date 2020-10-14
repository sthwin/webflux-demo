package com.sthwin.webflux.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sthwin.webflux.rsocket.HotelInfo;
import com.sthwin.webflux.rsocket.HotelSearchResponse;
import com.sthwin.webflux.rsocket.HotelSearchResponseContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 캐싱중인 조건에 대한 검색 요청을 받았을 경우 어떻게 할 것인지 ?
 * - 캐싱중인 데이터를 반환한다.
 * - 캐싱이 완료될 때까지 체크한다.
 * - 3개의 공급사 중, 하나의 공급사에서 오류가 발생했을 때 2개의 공급사 데이터만 캐싱할지 아니면, 모든 공급사 데이터를
 * 가져오지 않았으므로 캐싱을 하지 말지.. 결정 필요
 * - 2개의 공급사 데이터만 캐싱이 되었을 경우, 같은 검색 조건에 대한 응답은 계속 2개의 공급사 데이터만 반환됨.
 * - 캐싱을 하지 않을 경우, 조회되지 않은 공급사가 계속 오류가 있는 상태면 공급사가 복구 될때 까지 캐싱이 안됨.
 * <p>
 * Created by sthwin on 2020/10/06 9:50 오후
 */

@Controller
public class HotelSearchController {

    static int id = 0;

    Map<String, HotelSearchResponseContainer> resContainer = new ConcurrentHashMap<>();

    @MessageMapping("hotelSearchRequest")
    Flux<HotelSearchResponse> search(JsonNode payload) {
        final String key = payload.toString();
        System.out.println("key: " + key);
        HotelSearchResponseContainer container = resContainer.get(key);
        if (container == null) {
            id++;
            final HotelSearchResponseContainer newContainer = new HotelSearchResponseContainer();
            //
            return Flux.just(whcFlux(id, newContainer), rtsFlux(id, newContainer), eanFlux(id, newContainer))
                    .flatMap(flux -> {
                        flux.subscribeOn(Schedulers.boundedElastic());
                        return flux;
                    }).doOnComplete(() -> {
                        // 검색이 완료되었으나 호텔응답이 하나도 없을 경우 컨테이너를 저장하지 않는다.
                        System.out.println("Flux Complete totally");
                        if (!newContainer.getHotelSearchResponseList().isEmpty())
                            resContainer.put(key, newContainer);
                    });
        } else {
            System.out.println("caching");
            return Flux.fromIterable(container.getHotelSearchResponseList());
        }

    }

    Flux<HotelSearchResponse> whcFlux(int id, final HotelSearchResponseContainer newContainer) {
        List<HotelInfo> list = new ArrayList<>();
        list.add(HotelInfo.builder().name("whc_a_hotel").price("100").build());
        list.add(HotelInfo.builder().name("whc_b_hotel").price("200").build());
        HotelSearchResponse res = HotelSearchResponse.builder().id(id).providerName("whc").hotelList(list).build();
        return Flux.just(res)
                .delayElements(Duration.ofSeconds(10)).doOnComplete(() -> {
                    newContainer.getHotelSearchResponseList().add(res);
                    System.out.println("whc Complete");
                });
    }

    Flux<HotelSearchResponse> rtsFlux(int id, final HotelSearchResponseContainer newContainer) {
        List<HotelInfo> list = new ArrayList<>();
        list.add(HotelInfo.builder().name("rts_c_hotel").price("300").build());
        list.add(HotelInfo.builder().name("rts_d_hotel").price("400").build());
        HotelSearchResponse res = HotelSearchResponse.builder().id(id).providerName("rts").hotelList(list).build();

        return Flux.just(res)
                .delayElements(Duration.ofSeconds(2)).doOnComplete(() -> {
                            newContainer.getHotelSearchResponseList().add(res);
                            System.out.println("rts Complete");
                        }
                );
    }

    Flux<HotelSearchResponse> eanFlux(int id, final HotelSearchResponseContainer newContainer) {
        List<HotelInfo> list = new ArrayList<>();
        list.add(HotelInfo.builder().name("ean_e_hotel").price("500").build());
        list.add(HotelInfo.builder().name("ean_f_hotel").price("600").build());
        HotelSearchResponse res = HotelSearchResponse.builder().id(id).providerName("ean").hotelList(list).build();

        return Flux.just(res).doOnComplete(() -> {
            newContainer.getHotelSearchResponseList().add(res);
            System.out.println("ean Complete");
        });
    }
//
//    class HotelResponseGenerator implements Consumer<SynchronousSink<HotelSearchResponse>> {
//
//        private final String key;
//
//        private final String[] providers = {"whc", "rts", "ean"};
//
//        public HotelResponseGenerator(String key) {
//            this.key = key;
//        }
//
//        public void accept(SynchronousSink<HotelSearchResponse> tSynchronousSink) {
//            HotelSearchResponseContainer hotelSearchResponseContainer = resContainer.get(key);
//            if (hotelSearchResponseContainer.isCompleteFlag()) {
//                for (HotelSearchResponse tmp : hotelSearchResponseContainer.getHotelSearchResponseList())
//                    tSynchronousSink.next(tmp);
//                tSynchronousSink.complete();
//            } else {
//                List<String> providerList = Arrays.asList(providers);  // 가져와야할 공급사
//                for (HotelSearchResponse tmp : hotelSearchResponseContainer.getHotelSearchResponseList()) {
//                    providerList.removeIf(providerName -> providerName.equals(tmp.getProviderName()));
//                    tSynchronousSink.next(tmp);
//                }
//                Timer timer = new Timer();
//                // Set the schedule function
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    int tryCount = 0;
//
//                    @Override
//                    public void run() {
//                        tryCount++;
//                        // resContainer 를 레디스라고 가정했을 때, 다시 가져옴
//                        HotelSearchResponseContainer hotelSearchResponseContainer = resContainer.get(key);
//
//                        List<String> tmpList = new ArrayList<>();
//                        for (HotelSearchResponse tmpRes : hotelSearchResponseContainer.getHotelSearchResponseList()) {
//                            for (String providerName : providers) {
//                                if (tmpRes.getProviderName().equals(providerName)) {
//                                    tSynchronousSink.next(tmpRes);
//                                    tmpList.add(providerName);
//                                }
//                            }
//                        }
//
//                        // 가져온 공급사들에 대해서는 삭제
//                        for (String removeProviderName : tmpList) {
//                            providerList.removeIf(providerName -> providerName.equals(removeProviderName));
//                        }
//
//                        // 검색이 완료되었거나 5초가 넘어갈 경우 종료한다.
//                        if (providerList.size() == 0 || hotelSearchResponseContainer.isCompleteFlag() || tryCount > 9) {
//                            tSynchronousSink.complete();
//                        }
//                    }
//                }, 0, 500);
//            }
//        }
//    }

}
