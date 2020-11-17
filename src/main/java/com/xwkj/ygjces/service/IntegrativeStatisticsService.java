package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.model.JcCoreWtInfo;

import java.util.Date;
import java.util.List;

public interface IntegrativeStatisticsService {
    /**
     * 获取当年或者指定时间段(根据委托单生成时间筛选)的委托单统计信息
     * @param startTime 指定的开始时间
     * @param endTime   指定的结束时间
     * @return
     */
    public List<Object> getJcCoreWtInfoInOneYear(Date startTime,Date endTime);


    /**
     * 根据日期查找对应的委托单信息列表
     * @param jcCoreWtInfo 委托单信息
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    public PageInfo<JcCoreWtInfo> getJcCoreWtListInfoByDate(JcCoreWtInfo jcCoreWtInfo, Integer pageNum, Integer pageSize);
}
