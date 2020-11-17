package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper.OvertimeModelMapperManual;
import com.xwkj.ygjces.Mapper_oracle.JcCoreSampleMapperManual;
import com.xwkj.ygjces.model.NameAndCount;
import com.xwkj.ygjces.model.SampleInfo;
import com.xwkj.ygjces.model.StageTime;
import com.xwkj.ygjces.service.OvertimeModelService;
import com.xwkj.ygjces.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OvertimeModelServiceImpl implements OvertimeModelService{
    @Autowired
    OvertimeModelMapperManual overtimeModelMapperManual;
    @Autowired
    JcCoreSampleMapperManual jcCoreSampleMapperManual;

    /**
     * 获取超时排名数据列表
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getOverTimeRankingDataList() {
        List<Object> list = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<Long> countList = new ArrayList<>();
        List<NameAndCount> overTimeRankingDataList = overtimeModelMapperManual.getOverTimeRankingDataList();
        for (NameAndCount n : overTimeRankingDataList){
            nameList.add(n.getName());
            countList.add(n.getCount());
        }
        list.add(nameList);
        list.add(countList);
        return list;
    }

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
    @Override
    public List<Object> getRankingDataList(Date startTime,Date endTime,Integer stage, Integer mode, Double percentage) {
        List<Object> list = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<Long> countList = new ArrayList<>();
        if(stage == 1){
            if (mode == 1){
                Set<String> itemName = new HashSet<>();
                //第一阶段的样品
                List<SampleInfo> firstStage = jcCoreSampleMapperManual.getFirstStageSampleInfoListByCondition(startTime,endTime,null);
                List<SampleInfo> firstStageOvertime = new ArrayList<>();
                for (int i = 0; i < firstStage.size() ; i++) {
                    SampleInfo sampleInfo = firstStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //计算第一阶段限定时间(单位：小时)
                    Double firstTime = (Double.parseDouble(time.getTotalTime())-Double.parseDouble(time.getSecondTime())-Double.parseDouble(time.getThirdTime()))*24;
                    //计算第一阶段开始时间到现在的时间间隔
                    Double timeInterval = (double) DateUtil.getBetweenHours(new Date(),sampleInfo.getStartTime());
                    if(timeInterval>firstTime){
                        firstStageOvertime.add(sampleInfo);
                    }
                }
                for (SampleInfo s : firstStageOvertime){
                    itemName.add(s.getItemName());
                }
                nameList = new ArrayList<>(itemName);
                for (String s : nameList){
                    long num = 0;
                    for (SampleInfo s1 : firstStageOvertime){
                        if (s1.getItemName().equals(s)){
                            num++;
                        }
                    }
                    countList.add(num);
                }
            }else {
                Set<String> itemName = new HashSet<>();
                //第一阶段的样品
                List<SampleInfo> firstStage = jcCoreSampleMapperManual.getFirstStageSampleInfoListByCondition(startTime,endTime,null);
                List<SampleInfo> firstStageRemind = new ArrayList<>();
                for (int i = 0; i < firstStage.size() ; i++) {
                    SampleInfo sampleInfo = firstStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //计算第一阶段限定时间(单位：小时)
                    Double firstTime = (Double.parseDouble(time.getTotalTime())-Double.parseDouble(time.getSecondTime())-Double.parseDouble(time.getThirdTime()))*24;
                    //计算第一阶段开始时间到现在的时间间隔
                    Double timeInterval = (double) DateUtil.getBetweenHours(new Date(),sampleInfo.getStartTime());
                    if(timeInterval<firstTime && timeInterval>firstTime*(1-percentage)){
                        firstStageRemind.add(sampleInfo);
                    }
                }
                for (SampleInfo s : firstStageRemind){
                    itemName.add(s.getItemName());
                }
                nameList = new ArrayList<>(itemName);
                for (String s : nameList){
                    long num = 0;
                    for (SampleInfo s1 : firstStageRemind){
                        if (s1.getItemName().equals(s)){
                            num++;
                        }
                    }
                    countList.add(num);
                }

            }
        }else if (stage == 2){
            if (mode == 1){
                Set<String> itemName = new HashSet<>();
                //第二阶段的样品
                List<SampleInfo> secondStage = jcCoreSampleMapperManual.getSecondStageSampleInfoListByCondition(startTime,endTime,null);
                List<SampleInfo> secondStageOvertime = new ArrayList<>();
                for (int i = 0; i < secondStage.size() ; i++) {
                    SampleInfo sampleInfo = secondStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //将第二阶段限定时间转换为小时
                    Double secondTime = Double.parseDouble(time.getSecondTime())*24;
                    //计算第二阶段开始时间到现在的时间间隔
                    Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                    if (timeInterval>secondTime){
                        secondStageOvertime.add(sampleInfo);
                    }
                }
                for (SampleInfo s : secondStageOvertime){
                    itemName.add(s.getItemName());
                }
                nameList = new ArrayList<>(itemName);
                for (String s : nameList){
                    long num = 0;
                    for (SampleInfo s1 : secondStageOvertime){
                        if (s1.getItemName().equals(s)){
                            num++;
                        }
                    }
                    countList.add(num);
                }


            }else {
                Set<String> itemName = new HashSet<>();
                //第二阶段的样品
                List<SampleInfo> secondStage = jcCoreSampleMapperManual.getSecondStageSampleInfoListByCondition(startTime,endTime,null);
                List<SampleInfo> secondStageRemind = new ArrayList<>();
                for (int i = 0; i < secondStage.size() ; i++) {
                    SampleInfo sampleInfo = secondStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //将第二阶段限定时间转换为小时
                    Double secondTime = Double.parseDouble(time.getSecondTime())*24;
                    //计算第二阶段开始时间到现在的时间间隔
                    Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                    if (timeInterval<secondTime && timeInterval>secondTime*(1-percentage)){
                        secondStageRemind.add(sampleInfo);
                    }
                }
                for (SampleInfo s : secondStageRemind){
                    itemName.add(s.getItemName());
                }
                nameList = new ArrayList<>(itemName);
                for (String s : nameList){
                    long num = 0;
                    for (SampleInfo s1 : secondStageRemind){
                        if (s1.getItemName().equals(s)){
                            num++;
                        }
                    }
                    countList.add(num);
                }
            }


        }else if (stage == 3){
            if (mode == 1){
                Set<String> itemName = new HashSet<>();
                //第三阶段的样品
                List<SampleInfo> thirdStage = jcCoreSampleMapperManual.getThirdStageSampleInfoListByCondition(startTime,endTime,null);
                List<SampleInfo> thirdStageOvertime = new ArrayList<>();
                for (int i = 0; i < thirdStage.size() ; i++) {
                    SampleInfo sampleInfo = thirdStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //将第三阶段限定时间转换为小时
                    Double thirdTime = Double.parseDouble(time.getThirdTime())*24;
                    //计算第三阶段开始时间到现在的时间间隔
                    Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                    if(timeInterval > thirdTime){
                        thirdStageOvertime.add(sampleInfo);
                    }
                }
                for (SampleInfo s : thirdStageOvertime){
                    itemName.add(s.getItemName());
                }
                nameList = new ArrayList<>(itemName);
                for (String s : nameList){
                    long num = 0;
                    for (SampleInfo s1 : thirdStageOvertime){
                        if (s1.getItemName().equals(s)){
                            num++;
                        }
                    }
                    countList.add(num);
                }

            }else {
                Set<String> itemName = new HashSet<>();
                //第三阶段的样品
                List<SampleInfo> thirdStage = jcCoreSampleMapperManual.getThirdStageSampleInfoListByCondition(startTime,endTime,null);
                List<SampleInfo> thirdStageRemind = new ArrayList<>();
                for (int i = 0; i < thirdStage.size() ; i++) {
                    SampleInfo sampleInfo = thirdStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //将第三阶段限定时间转换为小时
                    Double thirdTime = Double.parseDouble(time.getThirdTime())*24;
                    //计算第三阶段开始时间到现在的时间间隔
                    Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                    if(timeInterval < thirdTime && timeInterval > thirdTime*(1-percentage)){
                        thirdStageRemind.add(sampleInfo);
                    }
                }
                for (SampleInfo s : thirdStageRemind){
                    itemName.add(s.getItemName());
                }
                nameList = new ArrayList<>(itemName);
                for (String s : nameList){
                    long num = 0;
                    for (SampleInfo s1 : thirdStageRemind){
                        if (s1.getItemName().equals(s)){
                            num++;
                        }
                    }
                    countList.add(num);
                }
            }
        }else if (stage == 4){
            Set<String> itemName = new HashSet<>();
            //获取所有已完成的样品列表
            List<SampleInfo> finishedList = jcCoreSampleMapperManual.getFinishedListByCondition(startTime,endTime,null);
            List<SampleInfo> finishedListOvertime = new ArrayList<>();
            for (int i = 0; i < finishedList.size() ; i++) {
                SampleInfo sampleInfo = finishedList.get(i);
                //获取对应项目的除第一阶段外各个阶段的限定时间
                StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                //将限定时间转换为小时
                Double totalTime = Double.parseDouble(time.getTotalTime())*24;
                //计算第三阶段开始时间到现在的时间间隔
                Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                if(timeInterval > totalTime){
                    finishedListOvertime.add(sampleInfo);
                }
            }
            for (SampleInfo s : finishedListOvertime){
                itemName.add(s.getItemName());
            }
            nameList = new ArrayList<>(itemName);
            for (String s : nameList){
                long num = 0;
                for (SampleInfo s1 : finishedListOvertime){
                    if (s1.getItemName().equals(s)){
                        num++;
                    }
                }
                countList.add(num);
            }

        }

        for (int i = 0; i < countList.size() - 1 ; i++) {
            for (int j = 0; j < countList.size() - 1 -i  ; j++) {
                if (countList.get(j) > countList.get(j+1)){
                    long count = countList.get(j);
                    countList.set(j,countList.get(j+1));
                    countList.set(j+1,count);
                    String name = nameList.get(j);
                    nameList.set(j,nameList.get(j+1));
                    nameList.set(j+1,name);
                }

            }
        }

        list.add(nameList);
        list.add(countList);
        return list;
    }

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
    @Override
    public PageInfo<SampleInfo> getRankingInfoList(Date startTime, Date endTime,Integer pageNum, Integer pageSize, Integer stage, Integer mode, Double percentage,String itemName) {
//        List<String> syNums = new ArrayList<>();
        StringBuffer syNums = new StringBuffer();
        int inNum = 1;//将要拼装的IN的条件数量
        PageInfo<SampleInfo> pageInfo = null;
        if (stage == 1){
            if (mode == 1){
                List<SampleInfo> firstStage = jcCoreSampleMapperManual.getFirstStageSampleInfoListByCondition(startTime, endTime, itemName);
                List<SampleInfo> firstStageOvertime = new ArrayList<>();
                for (int i = 0; i < firstStage.size() ; i++) {
                    SampleInfo sampleInfo = firstStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //计算第一阶段限定时间(单位：小时)
                    Double firstTime = (Double.parseDouble(time.getTotalTime())-Double.parseDouble(time.getSecondTime())-Double.parseDouble(time.getThirdTime()))*24;
                    //计算第一阶段开始时间到现在的时间间隔
                    Double timeInterval = (double) DateUtil.getBetweenHours(new Date(),sampleInfo.getStartTime());
                    if(timeInterval>firstTime){
                        firstStageOvertime.add(sampleInfo);
                    }
                }
                //构造符合条件的样品编号列表
//                for (SampleInfo s1 : firstStageOvertime){
//                    syNums.add(s1.getSampleNum());
//                }

                for (int i = 0; i < firstStageOvertime.size(); i++) {
                    if (i == firstStageOvertime.size() - 1){
                        syNums.append("'" + firstStageOvertime.get(i).getSampleNum() + "'");//SQL拼装，最后一条不加“,”
                    }else if (inNum==1000 && i>0){
                        syNums.append("'" + firstStageOvertime.get(i).getSampleNum() + "') OR t.sy_num in (");//解决ORA-01795问题
                        inNum = 1;
                    }else {
                        syNums.append("'" + firstStageOvertime.get(i).getSampleNum() + "',");
                        inNum++;
                    }
                }
                //最终的样品编号条件
                String str = "("+syNums.toString()+")";
                System.out.println(str);
                //为了分页重新去表里查一遍
                PageHelper.startPage(pageNum, pageSize);
                List<SampleInfo> sampleListBySyNums = jcCoreSampleMapperManual.getSampleListBySyNums(str);
                pageInfo = new PageInfo<SampleInfo>(sampleListBySyNums);
            }else {
                List<SampleInfo> firstStage = jcCoreSampleMapperManual.getFirstStageSampleInfoListByCondition(startTime, endTime, itemName);
                List<SampleInfo> firstStageRemind = new ArrayList<>();
                for (int i = 0; i < firstStage.size() ; i++) {
                    SampleInfo sampleInfo = firstStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //计算第一阶段限定时间(单位：小时)
                    Double firstTime = (Double.parseDouble(time.getTotalTime())-Double.parseDouble(time.getSecondTime())-Double.parseDouble(time.getThirdTime()))*24;
                    //计算第一阶段开始时间到现在的时间间隔
                    Double timeInterval = (double) DateUtil.getBetweenHours(new Date(),sampleInfo.getStartTime());
                    if(timeInterval<firstTime && timeInterval>firstTime*(1-percentage)){
                        firstStageRemind.add(sampleInfo);
                    }
                }
                //构造符合条件的样品编号列表
//                for (SampleInfo s1 : firstStageRemind){
//                    syNums.add(s1.getSampleNum());
//                }
                for (int i = 0; i < firstStageRemind.size(); i++) {
                    if (i == firstStageRemind.size() - 1){
                        syNums.append("'" + firstStageRemind.get(i).getSampleNum() + "'");//SQL拼装，最后一条不加“,”
                    }else if (inNum==1000 && i>0){
                        syNums.append("'" + firstStageRemind.get(i).getSampleNum() + "' ) OR t.sy_num in ( ");//解决ORA-01795问题
                        inNum = 1;
                    }else {
                        syNums.append("'" + firstStageRemind.get(i).getSampleNum() + "', ");
                        inNum++;
                    }
                }
                //最终的样品编号条件
                String str = "("+syNums.toString()+")";
                //为了分页重新去表里查一遍
                PageHelper.startPage(pageNum, pageSize);
                List<SampleInfo> sampleListBySyNums = jcCoreSampleMapperManual.getSampleListBySyNums(str);
                pageInfo = new PageInfo<SampleInfo>(sampleListBySyNums);
            }
        }else if(stage == 2){
            if (mode == 1){
                List<SampleInfo> secondStage = jcCoreSampleMapperManual.getSecondStageSampleInfoListByCondition(startTime, endTime, itemName);
                List<SampleInfo> secondStageOvertime = new ArrayList<>();
                for (int i = 0; i < secondStage.size() ; i++) {
                    SampleInfo sampleInfo = secondStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //将第二阶段限定时间转换为小时
                    Double secondTime = Double.parseDouble(time.getSecondTime())*24;
                    //计算第二阶段开始时间到现在的时间间隔
                    Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                    if (timeInterval>secondTime){
                        secondStageOvertime.add(sampleInfo);
                    }
                }
                //构造符合条件的样品编号列表
//                for (SampleInfo s1 : secondStageOvertime){
//                    syNums.add(s1.getSampleNum());
//                }
                for (int i = 0; i < secondStageOvertime.size(); i++) {
                    if (i == secondStageOvertime.size() - 1){
                        syNums.append("'" + secondStageOvertime.get(i).getSampleNum() + "'");//SQL拼装，最后一条不加“,”
                    }else if (inNum==1000 && i>0){
                        syNums.append("'" + secondStageOvertime.get(i).getSampleNum() + "' ) OR t.sy_num in ( ");//解决ORA-01795问题
                        inNum = 1;
                    }else {
                        syNums.append("'" + secondStageOvertime.get(i).getSampleNum() + "', ");
                        inNum++;
                    }
                }
                //最终的样品编号条件
                String str = "("+syNums.toString()+")";
                //为了分页重新去表里查一遍
                PageHelper.startPage(pageNum, pageSize);
                List<SampleInfo> sampleListBySyNums = jcCoreSampleMapperManual.getSampleListBySyNums(str);
                pageInfo = new PageInfo<SampleInfo>(sampleListBySyNums);


            }else {
                List<SampleInfo> secondStage = jcCoreSampleMapperManual.getSecondStageSampleInfoListByCondition(startTime, endTime, itemName);
                List<SampleInfo> secondStageRemind = new ArrayList<>();
                for (int i = 0; i < secondStage.size() ; i++) {
                    SampleInfo sampleInfo = secondStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //将第二阶段限定时间转换为小时
                    Double secondTime = Double.parseDouble(time.getSecondTime())*24;
                    //计算第二阶段开始时间到现在的时间间隔
                    Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                    if (timeInterval<secondTime && timeInterval>secondTime*(1-percentage)){
                        secondStageRemind.add(sampleInfo);
                    }
                }
                //构造符合条件的样品编号列表
//                for (SampleInfo s1 : secondStageRemind){
//                    syNums.add(s1.getSampleNum());
//                }
                for (int i = 0; i < secondStageRemind.size(); i++) {
                    if (i == secondStageRemind.size() - 1){
                        syNums.append("'" + secondStageRemind.get(i).getSampleNum() + "'");//SQL拼装，最后一条不加“,”
                    }else if (inNum==1000 && i>0){
                        syNums.append("'" + secondStageRemind.get(i).getSampleNum() + "' ) OR t.sy_num in ( ");//解决ORA-01795问题
                        inNum = 1;
                    }else {
                        syNums.append("'" + secondStageRemind.get(i).getSampleNum() + "', ");
                        inNum++;
                    }
                }
                //最终的样品编号条件
                String str = "("+syNums.toString()+")";
                //为了分页重新去表里查一遍
                PageHelper.startPage(pageNum, pageSize);
                List<SampleInfo> sampleListBySyNums = jcCoreSampleMapperManual.getSampleListBySyNums(str);
                pageInfo = new PageInfo<SampleInfo>(sampleListBySyNums);
            }
        }else if(stage == 3){
            if (mode == 1){
                List<SampleInfo> thirdStage = jcCoreSampleMapperManual.getThirdStageSampleInfoListByCondition(startTime, endTime, itemName);
                List<SampleInfo> thirdStageOvertime = new ArrayList<>();
                for (int i = 0; i < thirdStage.size() ; i++) {
                    SampleInfo sampleInfo = thirdStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //将第三阶段限定时间转换为小时
                    Double thirdTime = Double.parseDouble(time.getThirdTime())*24;
                    //计算第三阶段开始时间到现在的时间间隔
                    Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                    if(timeInterval > thirdTime){
                        thirdStageOvertime.add(sampleInfo);
                    }
                }
                //构造符合条件的样品编号列表
//                for (SampleInfo s1 : thirdStageOvertime){
//                    syNums.add(s1.getSampleNum());
//                }
                for (int i = 0; i < thirdStageOvertime.size(); i++) {
                    if (i == thirdStageOvertime.size() - 1){
                        syNums.append("'" + thirdStageOvertime.get(i).getSampleNum() + "'");//SQL拼装，最后一条不加“,”
                    }else if (inNum==1000 && i>0){
                        syNums.append("'" + thirdStageOvertime.get(i).getSampleNum() + "' ) OR t.sy_num in ( ");//解决ORA-01795问题
                        inNum = 1;
                    }else {
                        syNums.append("'" + thirdStageOvertime.get(i).getSampleNum() + "', ");
                        inNum++;
                    }
                }
                //最终的样品编号条件
                String str = "("+syNums.toString()+")";
                //为了分页重新去表里查一遍
                PageHelper.startPage(pageNum, pageSize);
                List<SampleInfo> sampleListBySyNums = jcCoreSampleMapperManual.getSampleListBySyNums(str);
                pageInfo = new PageInfo<SampleInfo>(sampleListBySyNums);
            }else {
                List<SampleInfo> thirdStage = jcCoreSampleMapperManual.getThirdStageSampleInfoListByCondition(startTime, endTime, itemName);
                List<SampleInfo> thirdStageRemind = new ArrayList<>();
                for (int i = 0; i < thirdStage.size() ; i++) {
                    SampleInfo sampleInfo = thirdStage.get(i);
                    //获取对应项目的除第一阶段外各个阶段的限定时间
                    StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                    //将第三阶段限定时间转换为小时
                    Double thirdTime = Double.parseDouble(time.getThirdTime())*24;
                    //计算第三阶段开始时间到现在的时间间隔
                    Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                    if(timeInterval < thirdTime && timeInterval > thirdTime*(1-percentage)){
                        thirdStageRemind.add(sampleInfo);
                    }
                }
                //构造符合条件的样品编号列表
//                for (SampleInfo s1 : thirdStageRemind){
//                    syNums.add(s1.getSampleNum());
//                }
                for (int i = 0; i < thirdStageRemind.size(); i++) {
                    if (i == thirdStageRemind.size() - 1){
                        syNums.append("'" + thirdStageRemind.get(i).getSampleNum() + "'");//SQL拼装，最后一条不加“,”
                    }else if (inNum==1000 && i>0){
                        syNums.append("'" + thirdStageRemind.get(i).getSampleNum() + "' ) OR t.sy_num in ( ");//解决ORA-01795问题
                        inNum = 1;
                    }else {
                        syNums.append("'" + thirdStageRemind.get(i).getSampleNum() + "', ");
                        inNum++;
                    }
                }
                //最终的样品编号条件
                String str = "("+syNums.toString()+")";
                //为了分页重新去表里查一遍
                PageHelper.startPage(pageNum, pageSize);
                List<SampleInfo> sampleListBySyNums = jcCoreSampleMapperManual.getSampleListBySyNums(str);
                pageInfo = new PageInfo<SampleInfo>(sampleListBySyNums);
            }

        }else if(stage == 4){
            List<SampleInfo> finishedList = jcCoreSampleMapperManual.getFinishedListByCondition(startTime, endTime, itemName);
            List<SampleInfo> finishedListOvertime = new ArrayList<>();
            for (int i = 0; i < finishedList.size() ; i++) {
                SampleInfo sampleInfo = finishedList.get(i);
                //获取对应项目的除第一阶段外各个阶段的限定时间
                StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
                //将限定时间转换为小时
                Double totalTime = Double.parseDouble(time.getTotalTime())*24;
                //计算第三阶段开始时间到现在的时间间隔
                Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
                if(timeInterval > totalTime){
                    finishedListOvertime.add(sampleInfo);
                }
            }
            //构造符合条件的样品编号列表
//            for (SampleInfo s1 : finishedListOvertime){
//                syNums.add(s1.getSampleNum());
//            }
            for (int i = 0; i < finishedListOvertime.size(); i++) {
                if (i == finishedListOvertime.size() - 1){
                    syNums.append("'" + finishedListOvertime.get(i).getSampleNum() + "'");//SQL拼装，最后一条不加“,”
                }else if (inNum==1000 && i>0){
                    syNums.append("'" + finishedListOvertime.get(i).getSampleNum() + "' ) OR t.sy_num in ( ");//解决ORA-01795问题
                    inNum = 1;
                }else {
                    syNums.append("'" + finishedListOvertime.get(i).getSampleNum() + "', ");
                    inNum++;
                }
            }
            //最终的样品编号条件
            String str = "("+syNums.toString()+")";
            //为了分页重新去表里查一遍
            PageHelper.startPage(pageNum, pageSize);
            List<SampleInfo> sampleListBySyNums = jcCoreSampleMapperManual.getSampleListBySyNums(str);
            pageInfo = new PageInfo<SampleInfo>(sampleListBySyNums);

        }
        return pageInfo;
    }
}
