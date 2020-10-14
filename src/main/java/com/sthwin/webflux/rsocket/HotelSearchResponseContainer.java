package com.sthwin.webflux.rsocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sthwin on 2020/10/11 5:44 오후
 */


public class HotelSearchResponseContainer {

    List<HotelSearchResponse> hotelSearchResponseList = null;
    boolean completeFlag = false;

    public HotelSearchResponseContainer() {

    }

    public List<HotelSearchResponse> getHotelSearchResponseList() {
        if (hotelSearchResponseList == null)
            hotelSearchResponseList = new ArrayList<>();
        return hotelSearchResponseList;
    }

    public void setHotelSearchResponseList(List<HotelSearchResponse> hotelSearchResponseList) {
        this.hotelSearchResponseList = hotelSearchResponseList;
    }

    public boolean isCompleteFlag() {
        return completeFlag;
    }

    public void setCompleteFlag(boolean completeFlag) {
        this.completeFlag = completeFlag;
    }
}
