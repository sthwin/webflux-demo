package com.sthwin.webflux.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sthwin.webflux.rsocket.HotelInfo;
import com.sthwin.webflux.rsocket.HotelSearchResponse;
import com.sthwin.webflux.rsocket.HotelSearchResponseContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 캐싱중인 조건에 대한 검색 요청을 받았을 경우 어떻게 할 것인지 ?
 * - 캐싱중인 데이터를 반환한다.
 * - 캐싱이 완료될 때까지 체크한다.
 * -
 * <p>
 * Created by sthwin on 2020/10/06 9:50 오후
 */

@Controller
public class HotelSearchController {

    static int id = 0;

    Map<String, Object> resContainer = new ConcurrentHashMap<>();

    @MessageMapping("messages")
    Flux<HotelSearchResponse> search(JsonNode payload) {
        String key = payload.toString();
        System.out.println("key: " + key);
        if (!resContainer.containsKey(key)) {

        } else {
            resContainer.put(key, new HotelSearchResponseContainer());
        }
        id++;
        return Flux.just(whcFlux(id), rtsFlux(id), eanFlux(id))
                .flatMap(flux -> {
                    flux.subscribeOn(Schedulers.boundedElastic());
                    return flux;
                }).doOnComplete(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
    }

    Flux<HotelSearchResponse> whcFlux(int id) {
        List<HotelInfo> list = new ArrayList<>();
        list.add(HotelInfo.builder().name("whc_a_hotel").price("100").build());
        list.add(HotelInfo.builder().name("whc_b_hotel").price("200").build());
        HotelSearchResponse res = HotelSearchResponse.builder().id(id).providerName("whc").hotelList(list).build();
        return Flux.just(res)
                .delayElements(Duration.ofSeconds(3));
    }

    Flux<HotelSearchResponse> rtsFlux(int id) {
        List<HotelInfo> list = new ArrayList<>();
        list.add(HotelInfo.builder().name("rts_c_hotel").price("300").build());
        list.add(HotelInfo.builder().name("rts_d_hotel").price("400").build());
        HotelSearchResponse res = HotelSearchResponse.builder().id(id).providerName("rts").hotelList(list).build();
        return Flux.just(res)
                .delayElements(Duration.ofSeconds(2));
    }

    Flux<HotelSearchResponse> eanFlux(int id) {
        List<HotelInfo> list = new ArrayList<>();
        list.add(HotelInfo.builder().name("ean_e_hotel").price("500").build());
        list.add(HotelInfo.builder().name("ean_f_hotel").price("600").build());
        HotelSearchResponse res = HotelSearchResponse.builder().id(id).providerName("ean").hotelList(list).build();
        return Flux.just(res);
    }

}
