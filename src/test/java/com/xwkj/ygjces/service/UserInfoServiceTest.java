package com.xwkj.ygjces.service;

import com.xwkj.ygjces.Mapper.*;
import com.xwkj.ygjces.Mapper.auto.NoticeModelMapper;
import com.xwkj.ygjces.Mapper.auto.OvertimeModelMapper;
import com.xwkj.ygjces.Mapper.auto.RemindModelMapper;
import com.xwkj.ygjces.Mapper_oracle.JcCoreSampleMapperManual;
import com.xwkj.ygjces.model.*;
import com.xwkj.ygjces.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoServiceTest {
    //试验结束时间比例(第一阶段)
    public final static double proportion_of_end_of_test_time = 0.8;
    //审核结束时间比例(第二阶段)
    public final static double ratio_of_audit_closure_time = 0.8;
    //批准时间比例(第三阶段)
    public final static double ratio_of_approval_time = 0.8;

    @Autowired
    JcCoreSampleMapperManual jcCoreSampleMapperManual;
    @Autowired
    OrganizationInfoMapperManual organizationInfoMapperManual;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    NoticeModelMapper noticeModelMapper;
    @Autowired
    NoticeModelMapperManual noticeModelMapperManual;
    @Autowired
    OvertimeModelMapper overtimeModelMapper;
    @Autowired
    OvertimeModelMapperManual overtimeModelMapperManual;
    @Autowired
    RemindModelMapper remindModelMapper;
    @Autowired
    RemindModelMapperManual remindModelMapperManual;


    //    @Scheduled()
    @Test
    public void task(){

        long start = System.currentTimeMillis();

        //第一阶段的样品
        List<SampleInfo> firstStage = jcCoreSampleMapperManual.getFirstStageSampleInfoList();
        //第一阶段的提醒列表
        List<RemindModel> firstStageRemind = new ArrayList<>();
        //第一阶段的超时列表
        List<OvertimeModel> firstStageOvertime = new ArrayList<>();
        //第一阶段的消息列表
        List<NoticeModel> firstStageNotice = new ArrayList<>();

        //拼装第一阶段的提醒列表和第一阶段的超时列表
        for (int i = 0; i < firstStage.size() ; i++) {
            SampleInfo sampleInfo = firstStage.get(i);
            //获取对应项目的除第一阶段外各个阶段的限定时间
            StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
            //计算第一阶段限定时间(单位：小时)
            Integer firstTime = (Integer.parseInt(time.getTotalTime())-Integer.parseInt(time.getSecondTime())-Integer.parseInt(time.getThirdTime()))*24;
            //计算第一阶段开始时间到现在的时间间隔
            Integer timeInterval = DateUtil.getBetweenHours(new Date(),sampleInfo.getStartTime());

            if(timeInterval<firstTime){
                //没超时
                //需要提醒的样品
                if(timeInterval>firstTime*proportion_of_end_of_test_time){
                    RemindModel remindModel = new RemindModel();
                    remindModel.setSyNum(sampleInfo.getSampleNum());
                    remindModel.setWtNum(sampleInfo.getWtNum());
                    remindModel.setWtUnit(sampleInfo.getPrincipal());
                    remindModel.setXmNum(sampleInfo.getItemId());
                    remindModel.setXmName(sampleInfo.getItemName());
                    remindModel.setStage(1);
                    remindModel.setCreateDate(new Date());
                    firstStageRemind.add(remindModel);
                }

            }else {
                //超时
                OvertimeModel overtimeModel = new OvertimeModel();
                overtimeModel.setSyNum(sampleInfo.getSampleNum());
                overtimeModel.setWtNum(sampleInfo.getWtNum());
                overtimeModel.setWtUnit(sampleInfo.getPrincipal());
                overtimeModel.setXmId(sampleInfo.getItemId());
                overtimeModel.setXmName(sampleInfo.getItemName());
                overtimeModel.setStage(1);
                //判断是插入还是修改时再填充以下两个属性
//                overtimeModel.setCreateDate(new Date());
//                overtimeModel.setUpdateDate(new Date());
                firstStageOvertime.add(overtimeModel);
            }

        }
        //拼装第一阶段的消息列表
        //拼装提醒的消息列表
        //需要发送提醒信息的用户集合
        Set<Long> firstStageRemindUserIds = new HashSet<>();
        for (int j = 0; j < firstStageRemind.size() ; j++) {
            RemindModel remindModel = firstStageRemind.get(j);
            List<Long> userIds = userInfoMapper.getUserIdListByItemId(remindModel.getXmNum());
            for (int k = 0; k < userIds.size(); k++) {
                firstStageRemindUserIds.add(userIds.get(k));
            }
        }
        for (Long userid:firstStageRemindUserIds) {
            NoticeModel noticeModel = new NoticeModel();
            long num = 0;
            //获取当前用户所管理的项目集合
            List<String> itemIdsByUserId = userInfoMapper.getItemIdsByUserId(userid);
            for (int l = 0; l < firstStageRemind.size() ; l++) {
                //若当前遍历到的样品被包含在当前用户所管理的项目集合中，那么应该给当前用户发送这个样品的提醒信息
                if(itemIdsByUserId.contains(firstStageRemind.get(l).getXmNum())){
                    num++;
                }
            }
            noticeModel.setContent("您有"+num+"个样品试验快要超时了，请尽快检测");
            noticeModel.setCreateDate(new Date());
            noticeModel.setCategory(1);
            noticeModel.setUserid(userid);
            noticeModel.setStage(1);
            firstStageNotice.add(noticeModel);
        }
        //需要发送超时信息的用户集合
        Set<Long> firstStageOvertimeUserIds = new HashSet<>();
        for (int j = 0; j < firstStageOvertime.size() ; j++) {
            OvertimeModel overtimeModel = firstStageOvertime.get(j);
            List<Long> userIds = userInfoMapper.getUserIdListByItemId(overtimeModel.getXmId());
            for (int k = 0; k < userIds.size() ; k++) {
                firstStageOvertimeUserIds.add(userIds.get(k));
            }
        }
        for (Long userid: firstStageOvertimeUserIds) {
            NoticeModel noticeModel = new NoticeModel();
            long num = 0;
            //获取当前用户所管理的项目集合
            List<String> itemIdsByUserId = userInfoMapper.getItemIdsByUserId(userid);
            for (int l = 0; l < firstStageOvertime.size() ; l++) {
                //若当前遍历到的样品被包含在当前用户所管理的项目集合中，那么应该给当前用户发送这个样品的提醒信息
                if(itemIdsByUserId.contains(firstStageOvertime.get(l).getXmId())){
                    num++;
                }
            }
            noticeModel.setContent("您有"+num+"个样品试验已经超时了，请尽快检测");
            noticeModel.setCreateDate(new Date());
            noticeModel.setCategory(2);
            noticeModel.setUserid(userid);
            noticeModel.setStage(1);
            firstStageNotice.add(noticeModel);
        }






        //第二阶段的样品
        List<SampleInfo> secondStage = jcCoreSampleMapperManual.getSecondStageSampleInfoList();
        //第二阶段的提醒列表
        List<RemindModel> secondStageRemind = new ArrayList<>();
        //第二阶段的超时列表
        List<OvertimeModel> secondStageOvertime = new ArrayList<>();
        //第二阶段的消息列表
        List<NoticeModel> secondStageNotice = new ArrayList<>();

        //拼装第二阶段的提醒列表和第二阶段的超时列表
        for (int i = 0; i < secondStage.size() ; i++) {
            SampleInfo sampleInfo = secondStage.get(i);
            //获取对应项目的除第一阶段外各个阶段的限定时间
            StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
            //将第二阶段限定时间转换为小时
            Integer secondTime = Integer.parseInt(time.getSecondTime())*24;
            //计算第二阶段开始时间到现在的时间间隔
            Integer timeInterval = DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
            if(timeInterval < secondTime){
                //没超时
                //需要提醒的样品
                if(timeInterval > secondTime*ratio_of_audit_closure_time){
                    RemindModel remindModel = new RemindModel();
                    remindModel.setSyNum(sampleInfo.getSampleNum());
                    remindModel.setWtNum(sampleInfo.getWtNum());
                    remindModel.setWtUnit(sampleInfo.getPrincipal());
                    remindModel.setXmNum(sampleInfo.getItemId());
                    remindModel.setXmName(sampleInfo.getItemName());
                    remindModel.setStage(2);
                    remindModel.setCreateDate(new Date());
                    secondStageRemind.add(remindModel);
                }
            }else {
                //超时
                OvertimeModel overtimeModel = new OvertimeModel();
                overtimeModel.setSyNum(sampleInfo.getSampleNum());
                overtimeModel.setWtNum(sampleInfo.getWtNum());
                overtimeModel.setWtUnit(sampleInfo.getPrincipal());
                overtimeModel.setXmId(sampleInfo.getItemId());
                overtimeModel.setXmName(sampleInfo.getItemName());
                overtimeModel.setStage(2);
                //判断是插入还是修改时再填充以下两个属性
//                overtimeModel.setCreateDate(new Date());
//                overtimeModel.setUpdateDate(new Date());
                secondStageOvertime.add(overtimeModel);

            }

        }

        //拼装第二阶段的消息列表
        //拼装提醒的消息列表
        //需要发送提醒信息的审核人集合
        Set<Long> secondStageRemindUserIds = new HashSet<>();
        for (int j = 0; j < secondStageRemind.size() ; j++) {
            RemindModel remindModel = secondStageRemind.get(j);
            List<Long> userIds = userInfoMapper.getAuditorIdListByItemId(remindModel.getXmNum());
            for (int k = 0; k < userIds.size() ; k++) {
                secondStageRemindUserIds.add(userIds.get(k));
            }
        }
        for (Long userid : secondStageRemindUserIds) {
            NoticeModel noticeModel = new NoticeModel();
            long num = 0;
            //获取当前审核人所审核的项目集合
            List<String> itemIdsByAuditorId = userInfoMapper.getItemIdsByAuditorId(userid);
            for (int l = 0; l < secondStageRemind.size() ; l++) {
                if(itemIdsByAuditorId.contains(secondStageRemind.get(l).getXmNum())){
                    num++;
                }
            }
            noticeModel.setContent("您有"+num+"个样品试验快要超时了，请尽快检测");
            noticeModel.setCreateDate(new Date());
            noticeModel.setCategory(1);
            noticeModel.setUserid(userid);
            noticeModel.setStage(2);
            secondStageNotice.add(noticeModel);
        }
        //需要发送超时信息的审核人集合
        Set<Long> secondStageOvertimeUserIds = new HashSet<>();
        for (int j = 0; j < secondStageOvertime.size() ; j++) {
            OvertimeModel overtimeModel = secondStageOvertime.get(j);
            List<Long> userIds = userInfoMapper.getAuditorIdListByItemId(overtimeModel.getXmId());
            for (int k = 0; k < userIds.size() ; k++) {
                secondStageOvertimeUserIds.add(userIds.get(k));
            }
        }
        for (Long userid : secondStageOvertimeUserIds){
            NoticeModel noticeModel = new NoticeModel();
            long num = 0;
            //获取当前审核人所审核的项目集合
            List<String> itemIdsByAuditorId = userInfoMapper.getItemIdsByAuditorId(userid);
            for (int l = 0; l < secondStageOvertime.size(); l++) {
                if(itemIdsByAuditorId.contains(secondStageOvertime.get(l).getXmId())){
                    num++;
                }
            }
            noticeModel.setContent("您有"+num+"个样品试验已经超时了，请尽快检测");
            noticeModel.setCreateDate(new Date());
            noticeModel.setCategory(2);
            noticeModel.setUserid(userid);
            noticeModel.setStage(2);
            secondStageNotice.add(noticeModel);
        }






        //第三阶段的样品
        List<SampleInfo> thirdStage = jcCoreSampleMapperManual.getThirdStageSampleInfoList();
        //第三阶段的提醒列表
        List<RemindModel> thirdStageRemind = new ArrayList<>();
        //第三阶段的超时列表
        List<OvertimeModel> thirdStageOvertime = new ArrayList<>();
        //第三阶段的消息列表
        List<NoticeModel> thirdStageNotice = new ArrayList<>();

        //拼装第三阶段的提醒列表和第三阶段的超时列表
        for (int i = 0; i < thirdStage.size() ; i++) {
            SampleInfo sampleInfo = secondStage.get(i);
            //获取对应项目的除第一阶段外各个阶段的限定时间
            StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
            //将第三阶段限定时间转换为小时
            Integer thirdTime = Integer.parseInt(time.getThirdTime())*24;
            //计算第三阶段开始时间到现在的时间间隔
            Integer timeInterval = DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
            if(timeInterval < thirdTime){
                //没超时
                //需要提醒的样品
                if(timeInterval < thirdTime*ratio_of_approval_time){
                    RemindModel remindModel = new RemindModel();
                    remindModel.setSyNum(sampleInfo.getSampleNum());
                    remindModel.setWtNum(sampleInfo.getWtNum());
                    remindModel.setWtUnit(sampleInfo.getPrincipal());
                    remindModel.setXmNum(sampleInfo.getItemId());
                    remindModel.setXmName(sampleInfo.getItemName());
                    remindModel.setStage(3);
                    remindModel.setCreateDate(new Date());
                    thirdStageRemind.add(remindModel);
                }
            }else {
                //超时
                OvertimeModel overtimeModel = new OvertimeModel();
                overtimeModel.setSyNum(sampleInfo.getSampleNum());
                overtimeModel.setWtNum(sampleInfo.getWtNum());
                overtimeModel.setWtUnit(sampleInfo.getPrincipal());
                overtimeModel.setXmId(sampleInfo.getItemId());
                overtimeModel.setXmName(sampleInfo.getItemName());
                overtimeModel.setStage(3);
                //判断是插入还是修改时再填充以下两个属性
//                overtimeModel.setCreateDate(new Date());
//                overtimeModel.setUpdateDate(new Date());
                thirdStageOvertime.add(overtimeModel);
            }
        }
        //拼装第三阶段的消息列表
        //拼装提醒的消息列表
        //需要发送提醒信息的批准人集合
        Set<Long> thirdStageRemindUserIds = new HashSet<>();
        for (int j = 0; j < thirdStageRemind.size() ; j++) {
            RemindModel remindModel = thirdStageRemind.get(j);
            List<Long> userIds = userInfoMapper.getApproverIdListByItemId(remindModel.getXmNum());
            for (int k = 0; k < userIds.size() ; k++) {
                thirdStageRemindUserIds.add(userIds.get(k));
            }
        }
        for (Long userid : thirdStageRemindUserIds){
            NoticeModel noticeModel = new NoticeModel();
            long num = 0;
            //获取当前批准人应批准的项目集合
            List<String> itemIdsByApproverId = userInfoMapper.getItemIdsByApproverId(userid);
            for (int l = 0; l < thirdStageRemind.size() ; l++) {
                if(itemIdsByApproverId.contains(thirdStageRemind.get(l).getXmNum())){
                    num++;
                }
            }
            noticeModel.setContent("您有"+num+"个样品试验快要超时了，请尽快检测");
            noticeModel.setCreateDate(new Date());
            noticeModel.setCategory(1);
            noticeModel.setUserid(userid);
            noticeModel.setStage(3);
            thirdStageNotice.add(noticeModel);
        }
        //需要发送超时信息的批准人集合
        Set<Long> thirdStageOvertimeUserIds = new HashSet<>();
        for (int j = 0; j < thirdStageOvertime.size() ; j++) {
            OvertimeModel overtimeModel = thirdStageOvertime.get(j);
            List<Long> userIds = userInfoMapper.getApproverIdListByItemId(overtimeModel.getXmId());
            for (int k = 0; k < userIds.size() ; k++) {
                thirdStageOvertimeUserIds.add(userIds.get(k));
            }
        }
        for (Long userid : thirdStageOvertimeUserIds){
            NoticeModel noticeModel = new NoticeModel();
            long num = 0;
            //获取当前批准人应批准的项目集合
            List<String> itemIdsByApproverId = userInfoMapper.getItemIdsByApproverId(userid);
            for (int l = 0; l < thirdStageOvertime.size() ; l++) {
                if(itemIdsByApproverId.contains(thirdStageOvertime.get(l).getXmId())){
                    num++;
                }
            }
            noticeModel.setContent("您有"+num+"个样品试验已经超时了，请尽快检测");
            noticeModel.setCreateDate(new Date());
            noticeModel.setCategory(2);
            noticeModel.setUserid(userid);
            noticeModel.setStage(3);
            thirdStageNotice.add(noticeModel);
        }

        remindModelMapperManual.insertRemindModelBatch(firstStageRemind);
        remindModelMapperManual.insertRemindModelBatch(secondStageRemind);
        remindModelMapperManual.insertRemindModelBatch(thirdStageRemind);

        for (int i = 0; i < firstStageOvertime.size() ; i++) {
            OvertimeModel overtimeModel = firstStageOvertime.get(i);
            OvertimeModel overtimeModelBySynumAndStage = overtimeModelMapperManual.getOvertimeModelBySynumAndStage(overtimeModel.getSyNum(), overtimeModel.getStage());
            if(overtimeModelBySynumAndStage != null){
                overtimeModel.setPid(overtimeModelBySynumAndStage.getPid());
                overtimeModel.setUpdateDate(new Date());
                overtimeModelMapper.updateByPrimaryKeySelective(overtimeModel);
            }else {
                overtimeModel.setCreateDate(new Date());
                overtimeModel.setUpdateDate(new Date());
                overtimeModelMapper.insertSelective(overtimeModel);
            }
        }
        for (int i = 0; i < secondStageOvertime.size() ; i++) {
            OvertimeModel overtimeModel = secondStageOvertime.get(i);
            OvertimeModel overtimeModelBySynumAndStage = overtimeModelMapperManual.getOvertimeModelBySynumAndStage(overtimeModel.getSyNum(), overtimeModel.getStage());
            if(overtimeModelBySynumAndStage != null){
                overtimeModel.setPid(overtimeModelBySynumAndStage.getPid());
                overtimeModel.setUpdateDate(new Date());
                overtimeModelMapper.updateByPrimaryKeySelective(overtimeModel);
            }else {
                overtimeModel.setCreateDate(new Date());
                overtimeModel.setUpdateDate(new Date());
                overtimeModelMapper.insertSelective(overtimeModel);
            }
        }
        for (int i = 0; i < thirdStageOvertime.size() ; i++) {
            OvertimeModel overtimeModel = thirdStageOvertime.get(i);
            OvertimeModel overtimeModelBySynumAndStage = overtimeModelMapperManual.getOvertimeModelBySynumAndStage(overtimeModel.getSyNum(), overtimeModel.getStage());
            if(overtimeModelBySynumAndStage != null){
                overtimeModel.setPid(overtimeModelBySynumAndStage.getPid());
                overtimeModel.setUpdateDate(new Date());
                overtimeModelMapper.updateByPrimaryKeySelective(overtimeModel);
            }else {
                overtimeModel.setCreateDate(new Date());
                overtimeModel.setUpdateDate(new Date());
                overtimeModelMapper.insertSelective(overtimeModel);
            }
        }


        noticeModelMapperManual.insertNoticeModelBatch(firstStageNotice);
        noticeModelMapperManual.insertNoticeModelBatch(secondStageNotice);
        noticeModelMapperManual.insertNoticeModelBatch(thirdStageNotice);

        long end = System.currentTimeMillis();
        System.out.println((end-start)/(1000*60));

    }
}