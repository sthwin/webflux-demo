package com.sthwin.webflux.rsocket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sthwin on 2020/10/11 4:39 오후
 */

@Getter
@Setter
@Builder
public class HotelInfo {
    String name;
    String price;
}
