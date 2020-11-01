package com.sthwin.webflux.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
public class MPISScheduleVo implements Serializable {

    private long seqNo;
    private String queryKey;
    private String agtCd;
    private String depCityCd1;
    private String arrCityCd1;
    private String depDate1;
    private String depCityCd2;
    private String arrCityCd2;
    private String depDate2;
}
