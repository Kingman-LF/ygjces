package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper_oracle.JcCoreWtInfoMapperManual;
import com.xwkj.ygjces.model.DateAndCount;
import com.xwkj.ygjces.model.JcCoreWtInfo;
import com.xwkj.ygjces.service.IntegrativeStatisticsService;
import com.xwkj.ygjces.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class IntegrativeStatisticsServiceImpl implements IntegrativeStatisticsService {

    @Autowired
    JcCoreWtInfoMapperManual jcCoreWtInfoMapperManual;

    /**
     * 获取当年或者指定时间段(根据委托单生成时间筛选)的委托单统计信息
     * @param startTime 指定的开始时间
     * @param endTime   指定的结束时间
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getJcCoreWtInfoInOneYear(Date startTime, Date endTime){
        //存放x轴信息、委托单统计信息的表
        List<Object> list = new ArrayList<>();
        //存放x轴信息的表
        List<String> xList = new ArrayList<>();
        //存放委托单统计信息的表
        List<Object> wtCountList = new ArrayList<>();
        //有合同的委托单列表
        List<Long> wtWithContract = new ArrayList<>();
        //无合同的委托单列表
        List<Long> wtWithoutContract = new ArrayList<>();
        //如果起止时间为空，就查找当年的委托单信息
        if(startTime==null&&endTime==null){
            Calendar calendar = Calendar.getInstance();
            Date first = DateUtil.getCurrYearFirst();
            //构造x轴列表
            for (int i = 0; i < 12 ; i++) {
                String toString = DateUtil.dateToString(first, DateUtil.YYYY_MM);
                xList.add(toString);
                calendar.setTime(first);
                calendar.add(Calendar.MONTH,1);
                first = calendar.getTime();
            }
            //有合同的委托单信息的封装
            List<DateAndCount> jcCoreWtInfoWithContractInThisYear = jcCoreWtInfoMapperManual.getJcCoreWtInfoWithContractInThisYear();
            for (int i = 1; i <= 12; i++) {
                wtWithContract.add(i-1,new Long(0));
                for (int j = 0 ; j < jcCoreWtInfoWithContractInThisYear.size();j++){
                    DateAndCount dateAndCount =jcCoreWtInfoWithContractInThisYear.get(j);
                    String date = dateAndCount.getDate().substring(5);
                    if(i==Integer.parseInt(date)){
                        wtWithContract.add(i-1,dateAndCount.getCount());
                        break;
                    }
                }
            }
            //无合同的委托单信息的封装
            List<DateAndCount> jcCoreWtInfoWithoutContractInThisYear = jcCoreWtInfoMapperManual.getJcCoreWtInfoWithoutContractInThisYear();
            for (int i = 1; i <= 12; i++) {
                wtWithoutContract.add(i-1,new Long(0));
                for (int j = 0 ; j < jcCoreWtInfoWithoutContractInThisYear.size();j++){
                    DateAndCount dateAndCount =jcCoreWtInfoWithoutContractInThisYear.get(j);
                    String date = dateAndCount.getDate().substring(5);
                    if(i==Integer.parseInt(date)){
                        wtWithoutContract.add(i-1,dateAndCount.getCount());
                        break;
                    }
                }
            }
        }else {
            //否则按起止时间查找委托单信息
            Integer difMonth = DateUtil.getDifMonth(startTime, endTime)+1;
            Calendar calendar = Calendar.getInstance();

            //构造x轴列表和委托单列表
            for (int i = 0; i < difMonth ; i++) {
                //将当前日期加入到x轴列表中
                String toString = DateUtil.dateToString(startTime, DateUtil.YYYY_MM);
                xList.add(toString);

                //查出当前日期对应的有合同的委托单数量和无合同的委托单数量，并加入相应的列表中
                Long jcCoreWtInfoCountWithContractByDate = jcCoreWtInfoMapperManual.getJcCoreWtInfoCountWithContractByDate(startTime);
                Long jcCoreWtInfoCountWithoutContractByDate = jcCoreWtInfoMapperManual.getJcCoreWtInfoCountWithoutContractByDate(startTime);
                wtWithContract.add(jcCoreWtInfoCountWithContractByDate);
                wtWithoutContract.add(jcCoreWtInfoCountWithoutContractByDate);

                //给当前日期增加一个月
                calendar.setTime(startTime);
                calendar.add(Calendar.MONTH,1);
                startTime = calendar.getTime();
            }
        }
        //组成每月委托单总数列表
        List<Long> totalWt = new ArrayList<>();
        for (int i = 0; i < wtWithContract.size(); i++) {
            totalWt.add(wtWithContract.get(i)+wtWithoutContract.get(i));
        }
        //计算委托单总数
        long count = 0;
        for (int i = 0; i < wtWithContract.size(); i++) {
            count += wtWithContract.get(i)+wtWithoutContract.get(i);
        }
        wtCountList.add(wtWithContract);
        wtCountList.add(wtWithoutContract);
        wtCountList.add(totalWt);
        list.add(xList);
        list.add(wtCountList);
        list.add(count);
        return list;
    }

    /**
     * 根据日期查找对应的委托单信息列表
     * @param jcCoreWtInfo 委托单信息
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<JcCoreWtInfo> getJcCoreWtListInfoByDate(JcCoreWtInfo jcCoreWtInfo, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<JcCoreWtInfo> list = jcCoreWtInfoMapperManual.getJcCoreWtListInfoByDate(jcCoreWtInfo);
        return new PageInfo<JcCoreWtInfo>(list);
    }
}
