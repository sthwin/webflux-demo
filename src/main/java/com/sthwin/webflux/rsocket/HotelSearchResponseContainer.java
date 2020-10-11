package com.sthwin.webflux.rsocket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by sthwin on 2020/10/11 5:44 오후
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
public class HotelSearchResponseContainer {
    List<HotelSearchResponse> resList;

    boolean completeFlag = false;
}
