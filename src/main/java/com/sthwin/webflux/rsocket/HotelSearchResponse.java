package com.sthwin.webflux.rsocket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by sthwin on 2020/10/11 1:01 오후
 */
@Getter
@Setter
@Builder
public class HotelSearchResponse {
    int id;
    String providerName;
    List<HotelInfo> hotelList;
}
