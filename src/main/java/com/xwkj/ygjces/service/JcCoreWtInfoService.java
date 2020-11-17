package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.controller.vo.JcCoreWtInfoVo;
import com.xwkj.ygjces.model.JcCoreWtInfo;

import java.util.Date;
import java.util.List;

public interface JcCoreWtInfoService {

    /**
     * 根据合同编号查找对应委托单列表
     * @param contractCode 合同编号
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    PageInfo<JcCoreWtInfo> getJcCoreWtInfoListByContractCode(String contractCode , Integer pageNum, Integer pageSize );

    /**
     * 根据合同编号查询对应委托单时间和总数接口
     * @param contractCode 合同编号
     * @return
     * @author wanglei
     */
    List<Object> getJcCoreWtInfoByContractCode(String contractCode);

    /**
     * 根据合同编号获取对应已结算委托单数量和未结算委托单数量的列表
     * @param contractCode 合同编号
     * @return
     * @author zyh
     */
    List<Object> getJsAndWjsCountListByContractCode(String contractCode);

    /**
     * 根据城市区域按照区域统计委托单业务层接口
     * @param city
     * @param area
     * @param startTime endTime
     * @return
     * @author wanglei
     */
    Integer getMapCityAreaWtCount(String city,String area,Date startTime,Date endTime);

    /**
     *根据城市区域时间查询委托单详情
     * @param wt_num
     * @param city
     * @param area
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     * @author wanglei
     */
    PageInfo<JcCoreWtInfoVo> getMapCityAreaWtInfo(String wt_num,String city, String area, Date startTime, Date endTime, Integer pageNum, Integer pageSize);

    /**
     * 查询当天委托单金额
     * @return
     * @author wanglei
     */
    public Integer getWtRealMoneyWithOneDay();

    /**
     * 查询当月委托单金额
     * @return
     * @author wanglei
     */
    public Integer getWtRealMoneyWithOneMonth();

    /**
     * 查询当天委托单数量
     * @return
     * @author wanglei
     */
    public Integer getWtCountWithOneDay();

    /**
     * 查询当月委托单数量
     * @return
     * @author wanglei
     */
    public Integer getWtCountWithOneMonth();

}
