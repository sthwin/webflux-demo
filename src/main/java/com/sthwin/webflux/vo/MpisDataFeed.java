package com.sthwin.webflux.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * mpis_data_feed
 * @author 
 */
@Data
public class MpisDataFeed implements Serializable {
    private Integer seq;

    private String fileName;

    private static final long serialVersionUID = 1L;
}