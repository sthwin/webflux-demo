package com.sthwin.webflux.mapper;

import com.sthwin.webflux.vo.MpisDataFeed;
import com.sthwin.webflux.vo.MpisDataFeedExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MpisDataFeeMapper {
    long countByExample(MpisDataFeedExample example);

    int deleteByExample(MpisDataFeedExample example);

    int deleteByPrimaryKey(Integer seq);

    int insert(MpisDataFeed record);

    int insertSelective(MpisDataFeed record);

    List<MpisDataFeed> selectByExample(MpisDataFeedExample example);

    MpisDataFeed selectByPrimaryKey(Integer seq);

    int updateByExampleSelective(@Param("record") MpisDataFeed record, @Param("example") MpisDataFeedExample example);

    int updateByExample(@Param("record") MpisDataFeed record, @Param("example") MpisDataFeedExample example);

    int updateByPrimaryKeySelective(MpisDataFeed record);

    int updateByPrimaryKey(MpisDataFeed record);
}