package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.model.SampleInfo;

import java.util.Date;
import java.util.List;

public interface OvertimeModelService {

    /**
     * 获取超时排名数据列表
     * @return
     * @author zyh
     */
    public List<Object> getOverTimeRankingDataList();


    /**
     * @Description : 获取不同阶段的排名信息
     * @methodName : getRankingDataList
     * @param stage :
     * @param mode :
     * @param percentage :
     * @return : java.util.List<java.lang.Object>
     * @exception :
     * @author : zyh
     */
    public List<Object> getRankingDataList(Date startTime, Date endTime, Integer stage, Integer mode, Double percentage);

    /**
     * @Description : 获取排名信息详情
     * @methodName : getRankingInfoList
     * @param pageNum :
     * @param pageSize :
     * @param stage :
     * @param mode :
     * @param percentage :
     * @return : com.github.pagehelper.PageInfo<com.xwkj.ygjces.model.SampleInfo>
     * @exception :
     * @author : zyh
     */
    public PageInfo<SampleInfo> getRankingInfoList(Date startTime, Date endTime,Integer pageNum,Integer pageSize,Integer stage, Integer mode, Double percentage,String itemName);

}
