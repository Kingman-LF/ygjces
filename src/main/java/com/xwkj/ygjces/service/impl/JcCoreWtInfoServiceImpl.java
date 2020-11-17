package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper_oracle.JcCoreWtInfoMapperManual;
import com.xwkj.ygjces.controller.vo.JcCoreWtInfoVo;
import com.xwkj.ygjces.model.DateAndCount;
import com.xwkj.ygjces.model.JcCoreWtInfo;
import com.xwkj.ygjces.service.JcCoreWtInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class JcCoreWtInfoServiceImpl implements JcCoreWtInfoService {

    @Autowired
    JcCoreWtInfoMapperManual jcCoreWtInfoMapperManual;

    /**
     * 根据城市区域时间查询委托单详情功能实现
     * @param city
     * @param area
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<JcCoreWtInfoVo> getMapCityAreaWtInfo(String wt_num,String city, String area,Date startTime, Date endTime,Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum , pageSize);
        List<JcCoreWtInfoVo> list = jcCoreWtInfoMapperManual.getMapCityAreaWtInfo(wt_num,city,area,startTime,endTime);
        return new PageInfo<JcCoreWtInfoVo>(list);
    }

    /**
     * 根据城市区域按照区域统计委托单业务层接口实现
     * @param city
     * @param area
     * @return
     * @author wanglei
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer getMapCityAreaWtCount(String city, String area, Date startTime,Date endTime){
        return jcCoreWtInfoMapperManual.getMapCityAreaWtCount(city,area,startTime,endTime);
    }

    /**
     * 根据合同编号查询对应委托单时间和总数接口实现
     * @param contractCode 合同编号
     * @return
     * @author wanglei
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getJcCoreWtInfoByContractCode(String contractCode){
        List<Object> list = new ArrayList<>();
        //根据开始时间结束时间计算时间段
        List<String> requireTime = new ArrayList<>();
        //根据时间段查询合同总量
        List<Long> requireTotal = new ArrayList<>();
        List<DateAndCount> jcCoreWtInfoList = jcCoreWtInfoMapperManual.getJcCoreWtInfoByContractCode(contractCode);
        for (int i=0;i<jcCoreWtInfoList.size();i++){
            requireTime.add(jcCoreWtInfoList.get(i).getDate());
            requireTotal.add(jcCoreWtInfoList.get(i).getCount());
        }
        list.add(requireTime);
        list.add(requireTotal);
        return list;
    }

    /**
     * 根据合同编号查找对应委托单列表
     * @param contractCode 合同编号
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<JcCoreWtInfo> getJcCoreWtInfoListByContractCode(String contractCode, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<JcCoreWtInfo> list = jcCoreWtInfoMapperManual.getJcCoreWtInfoListByContractCode(contractCode);
        return new PageInfo<JcCoreWtInfo>(list);
    }


    /**
     * 根据合同编号获取对应已结算委托单数量和未结算委托单数量的列表
     * @param contractCode 合同编号
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getJsAndWjsCountListByContractCode(String contractCode) {
        List<Object> list = new ArrayList<>();
        Map<String , Object> jsMap = new HashMap<>();
        Map<String , Object> wjsMap = new HashMap<>();
        Long js = jcCoreWtInfoMapperManual.getJcCoreWtInfoJsAmountByContractCode(contractCode);
        Long wjs = jcCoreWtInfoMapperManual.getJcCoreWtInfoWjsAmountByContractCode(contractCode);
        jsMap.put("value",js);
        jsMap.put("name","已结算");
        wjsMap.put("value",wjs);
        wjsMap.put("name","未结算");
        list.add(jsMap);
        list.add(wjsMap);
        return list;
    }

    /**
     * 查询当天委托单金额
     * @return
     * @author wanglei
     */
    @Override
    public Integer getWtRealMoneyWithOneDay(){
        return jcCoreWtInfoMapperManual.selectWtRealMoneyWithOneDay();
    }

    /**
     * 查询当月委托单金额
     * @return
     * @author wanglei
     */
    @Override
    public Integer getWtRealMoneyWithOneMonth(){
        return jcCoreWtInfoMapperManual.selectWtRealMoneyWithOneMonth();
    }

    /**
     * 查询当天委托单数量
     * @return
     * @author wanglei
     */
    @Override
    public Integer getWtCountWithOneDay(){
        return jcCoreWtInfoMapperManual.selectWtCountWithOneDay();
    }

    /**
     * 查询当月委托单数量
     * @return
     * @author wanglei
     */
    @Override
    public Integer getWtCountWithOneMonth(){
        return jcCoreWtInfoMapperManual.selectWtCountWithOneMonth();
    }

}
