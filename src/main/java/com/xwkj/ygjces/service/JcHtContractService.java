package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.model.JcHtContract;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface JcHtContractService {
    /**
     * 获取合同列表
     * @param jcHtContract 合同信息对象
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    PageInfo<JcHtContract> getContractList(JcHtContract jcHtContract,Integer pageNum, Integer pageSize);

    /**根据当月时间段年月日获取合同列表
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param contractName 合同名称
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author wanglei
     */
    PageInfo<JcHtContract> getContractListByOneMonth(Date startDate , Date endDate , String contractName,Integer pageNum, Integer pageSize);

    /**
     * 根据开始时间结束时间查询该时间段合同数量变化接口
     * @param jcHtContract startTime 开始时间 endTime 结束时间
     * @return
     */
    List<Object> getCalculationList(JcHtContract jcHtContract);

    /**
     *按照月份统计当年合同数量
     * @return
     * @author wanglei
     */
    public List<Integer> getHtCountByMonth();

    /**
     * 查询当天合同的数量
     * @return
     * @author wanglei
     */
    public Integer getContractCountWithOneDay();

    /**
     * 查询当月合同的数量
     * @return
     * @author wanglei
     */
    public Integer getContractCountWithOneMonth();

}
