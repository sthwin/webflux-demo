package com.sthwin.webflux.mapper;


import com.sthwin.webflux.vo.MpisScheduleVo;

/**
 * Created by sthwin on 2020/11/08 12:50 오후
 */
public interface MpisScheduleMapper {

    int deleteByPrimaryKey(Integer seq);

    int insert(MpisScheduleVo record);

    int insertSelective(MpisScheduleVo record);

    MpisScheduleVo selectByPrimaryKey(Integer seq);

    int updateByPrimaryKeySelective(MpisScheduleVo record);

    int updateByPrimaryKey(MpisScheduleVo record);
}
