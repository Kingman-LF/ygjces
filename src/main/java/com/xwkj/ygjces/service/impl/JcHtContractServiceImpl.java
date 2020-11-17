package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.xwkj.ygjces.Mapper_oracle.JcHtContractMapperManual;
import com.xwkj.ygjces.Mapper_oracle.auto.JcHtContractMapper;
import com.xwkj.ygjces.model.DateAndCount;
import com.xwkj.ygjces.model.JcHtContract;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.JcHtContractService;
import com.xwkj.ygjces.utils.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class JcHtContractServiceImpl implements JcHtContractService {
    @Autowired
    JcHtContractMapperManual jcHtContractMapperManual;
    @Autowired
    JcHtContractMapper jcHtContractMapper;

    /**
     * 根据开始时间结束时间查询该时间段合同数量变化接口实现
     * @param jcHtContract startTime 开始时间 endTime 结束时间
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getCalculationList(JcHtContract jcHtContract){
        //根据开始时间结束时间计算时间段
        List<String> requireTime = new ArrayList<>();
        //根据时间段查询合同总量
        List<Long> requireTotal = new ArrayList<>();
        //相邻月份合同增涨的合同数量
        List<Object> requireIncrease = new ArrayList<>();
        //相邻月份合同下降的合同数量
        List<Object> requireDcline = new ArrayList<>();


        if ((jcHtContract.getStartTime() == null) && (jcHtContract.getEndTime() == null)){
            //日期类
            Calendar calendar = Calendar.getInstance();
            //查询当前年第一天时间
            Date firstYearTime = DateUtil.getCurrYearFirst();
            //没有筛选条件,默认系统时间年12月份
            for(int i=0;i<12;i++){
                //返回年份和月份
                String currYearMonth = DateUtil.dateToString(firstYearTime, DateUtil.YYYY_MM);
                //年份月份添加到requireTime中
                requireTime.add(currYearMonth);


                //根据年份月份查询合同数量
                DateAndCount dateAndCount =jcHtContractMapperManual.getContractQuantity(jcHtContract);
                requireTotal.add(i,0L);
                if(dateAndCount != null && dateAndCount.getDate() != null){
                    if (i == (Integer.parseInt(dateAndCount.getDate().substring(5))-1)){
                        requireTotal.set(i,dateAndCount.getCount());
                    }
                }
                //循环月份加1
                calendar.setTime(firstYearTime);
                calendar.add(Calendar.MONTH,1);
                firstYearTime = calendar.getTime();
                //初始化增涨和下降的合同数量
                requireIncrease.add(i,"-");
                requireDcline.add(i,"-");
            }
            //根据合同数量
            for(int i =0;i<requireTotal.size();i++){
                if(i==0){
                   requireIncrease.set(0,requireTotal.get(i));
                }
                if(i>0){
                    if((requireTotal.get(i)-requireTotal.get(i-1))>=0){
                        //相邻差值大于等于0
                        requireIncrease.set(i,requireTotal.get(i)-requireTotal.get(i-1));
                    }
                    if ((requireTotal.get(i)-requireTotal.get(i-1))<0){
                        //相邻差值小于0
                        requireDcline.set(i,Math.abs(requireTotal.get(i)-requireTotal.get(i-1)));
                        requireTotal.set(i-1,requireTotal.get(i-1)+(requireTotal.get(i)-requireTotal.get(i-1)));
                    }
                }
            }
        }else{
            //根据开始时间和结束时间计算差值
            Integer difMonth = DateUtil.getDifMonth(jcHtContract.getStartTime(),jcHtContract.getEndTime())+1;
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < difMonth ; i++) {
                String requireYearMonth = DateUtil.dateToString(jcHtContract.getStartTime(), DateUtil.YYYY_MM);
                requireTime.add(requireYearMonth);

                //根据开始时间和结束时间查询合同数量
                DateAndCount dateAndCount = jcHtContractMapperManual.getContractQuantity(jcHtContract);
                requireTotal.add(i,0L);
                if(dateAndCount != null && dateAndCount.getCount() != null){
                        requireTotal.set(i,dateAndCount.getCount());
                }
                //循环月份加1
                calendar.setTime(jcHtContract.getStartTime());
                calendar.add(Calendar.MONTH,1);
                jcHtContract.setStartTime(calendar.getTime());
                //初始化增涨和下降的合同数量
                requireIncrease.add(i,"-");
                requireDcline.add(i,"-");
            }
            //根据合同数量
            for(int i =0;i<requireTotal.size();i++){
                if(i==0){
                    requireIncrease.set(0,requireTotal.get(i));
                }
                if(i>0){
                    if((requireTotal.get(i)-requireTotal.get(i-1))>=0){
                        //相邻差值大于等于0
                        requireIncrease.set(i,requireTotal.get(i)-requireTotal.get(i-1));
                    }
                    if ((requireTotal.get(i)-requireTotal.get(i-1))<0){
                        //相邻差值小于0
                        requireDcline.set(i,Math.abs(requireTotal.get(i)-requireTotal.get(i-1)));
                        requireTotal.set(i-1,requireTotal.get(i-1)+(requireTotal.get(i)-requireTotal.get(i-1)));

                    }
                }
            }
        }

        List<Object> list = new ArrayList<>();
        list.add(requireTime);
        requireTotal.remove(requireTotal.size()-1);
        requireTotal.add(0,0L);
        list.add(requireTotal);
        list.add(requireIncrease);
        list.add(requireDcline);

        return list;

    }

    /**
     * 获取合同列表
     * @param jcHtContract 合同信息对象
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<JcHtContract> getContractList(JcHtContract jcHtContract,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<JcHtContract> list = jcHtContractMapperManual.getContractList(jcHtContract);
        return new PageInfo<JcHtContract>(list);
    }
    /**
     * 根据当月时间段年月日获取合同列表
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param contractName 合同名称
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author wanglei
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<JcHtContract> getContractListByOneMonth(Date startDate , Date endDate ,String contractName,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<JcHtContract> list = jcHtContractMapperManual.getContractListByOneMonth(startDate ,endDate ,contractName);
        return new PageInfo<JcHtContract>(list);
    }

    /**
     *按照月份统计当年合同数量
     * @return
     * @author wanglei
     */
    @Override
    public List<Integer> getHtCountByMonth(){
        List<Integer> requireTotal = new ArrayList<>();
        //日期类
        Calendar calendar = Calendar.getInstance();
        //查询当前年第一天时间
        Date firstYearTime = DateUtil.getCurrYearFirst();
        //没有筛选条件,默认系统时间年12月份
        for(int i=0;i<12;i++){
            //返回年份和月份
            String currYearMonth = DateUtil.dateToString(firstYearTime, DateUtil.YYYY_MM);
            //根据年份月份查询合同数量
            Integer htCountWithCurrMonth = jcHtContractMapperManual.getHtCountByMonth(currYearMonth);
            requireTotal.add(htCountWithCurrMonth);
            //循环月份加1
            calendar.setTime(firstYearTime);
            calendar.add(Calendar.MONTH,1);
            firstYearTime = calendar.getTime();
        }
        return requireTotal;

    }

    /**
     * 查询当天合同的数量
     * @return
     * @author wanglei
     */
    @Override
    public Integer getContractCountWithOneDay(){
        return jcHtContractMapperManual.selectContractCountWithOneDay();
    }

    /**
     * 查询当月合同的数量
     * @return
     * @author wanglei
     */
    @Override
    public Integer getContractCountWithOneMonth(){
        return jcHtContractMapperManual.selectContractCountWithOneMonth();
    }

}
