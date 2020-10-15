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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 검색이 완료되면 resContainer(레디스라고 가정)에 저장한다.
 *
 * 웹소켓 특성 상, 고객이 페이지를 리프레시 하거나, 페이지를 이동하면 커넥션이 끊기며 프로세스도 즉시 동작하지 않음. *
 *
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
}
