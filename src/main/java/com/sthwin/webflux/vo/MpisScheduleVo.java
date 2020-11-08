package com.sthwin.webflux.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class MpisScheduleVo implements Serializable {

    private Integer seq;
    private String queryKey;
    private String agtCd;
    private String depCityCd1;
    private String arrCityCd1;
    private String depDate1;
    private String depCityCd2;
    private String arrCityCd2;
    private String depDate2;
}
