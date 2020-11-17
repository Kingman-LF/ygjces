package com.xwkj.ygjces.config;

import com.xwkj.ygjces.Mapper.*;
import com.xwkj.ygjces.Mapper.auto.*;
import com.xwkj.ygjces.Mapper_oracle.JcCoreSampleMapperManual;
import com.xwkj.ygjces.model.*;
import com.xwkj.ygjces.service.SendMsgService;
import com.xwkj.ygjces.service.WxService;
import com.xwkj.ygjces.utils.DateUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.*;

/**
 * 定时器
 * @author zyh
 */
@PropertySource(value = {"classpath:/config/FilePathConfig.properties","classpath:/config/NoticeConfig.properties"})
@Configuration
@EnableScheduling
public class ScheduleTaskConfig {

    //项目试验结束时间比例(第一阶段)
    private final static double proportion_of_end_of_test_time = 0.8;
    //项目审核结束时间比例(第二阶段)
    private final static double ratio_of_audit_closure_time = 0.8;
    //项目批准时间比例(第三阶段)
    private final static double ratio_of_approval_time = 0.8;
    //任务完成时间比例
    private final static double task_complete_time = 0.8;

    @Value("${tn.url}")
    private String url;
    @Value("${schedule-task-notice-mode}")
    private Integer mode;

    @Autowired
    JcCoreSampleMapperManual jcCoreSampleMapperManual;
    @Autowired
    OrganizationInfoMapper organizationInfoMapper;
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
    @Autowired
    TaskInfoMapperManual taskInfoMapperManual;
    @Autowired
    TaskInfoNoticeMapper taskInfoNoticeMapper;
    @Autowired
    TaskInfoNoticeMapperManual taskInfoNoticeMapperManual;
    @Autowired
    TaskRemindMapper taskRemindMapper;
    @Autowired
    TaskOverTimeMapper taskOverTimeMapper;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    WxService myWxService;

    /**
     * 存储项目提醒信息
     * @author zyh
     */
//    @Scheduled(fixedRate = 50000000)
    @Scheduled(cron = "0 0 1 * * ?")
    public void item(){
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
            Double firstTime = (Double.parseDouble(time.getTotalTime())-Double.parseDouble(time.getSecondTime())-Double.parseDouble(time.getThirdTime()))*24;
            //计算第一阶段开始时间到现在的时间间隔
            Double timeInterval = (double) DateUtil.getBetweenHours(new Date(),sampleInfo.getStartTime());

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
                    remindModel.setWtDate(sampleInfo.getWtDate());
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
                overtimeModel.setWtDate(sampleInfo.getWtDate());
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
            Double secondTime = Double.parseDouble(time.getSecondTime())*24;
            //计算第二阶段开始时间到现在的时间间隔
            Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
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
                    remindModel.setWtDate(sampleInfo.getWtDate());
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
                overtimeModel.setWtDate(sampleInfo.getWtDate());
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
            noticeModel.setContent("您有"+num+"个样品审核快要超时了，请尽快检测");
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
            noticeModel.setContent("您有"+num+"个样品审核已经超时了，请尽快检测");
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
            SampleInfo sampleInfo = thirdStage.get(i);
            //获取对应项目的除第一阶段外各个阶段的限定时间
            StageTime time = jcCoreSampleMapperManual.getStageTimeByItemId(sampleInfo.getItemId());
            //将第三阶段限定时间转换为小时
            Double thirdTime = Double.parseDouble(time.getThirdTime())*24;
            //计算第三阶段开始时间到现在的时间间隔
            Double timeInterval = (double)DateUtil.getBetweenHours(new Date(), sampleInfo.getStartTime());
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
                    remindModel.setWtDate(sampleInfo.getWtDate());
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
                overtimeModel.setWtDate(sampleInfo.getWtDate());
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
            noticeModel.setContent("您有"+num+"个样品批准快要超时了，请尽快检测");
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
            noticeModel.setContent("您有"+num+"个样品批准已经超时了，请尽快检测");
            noticeModel.setCreateDate(new Date());
            noticeModel.setCategory(2);
            noticeModel.setUserid(userid);
            noticeModel.setStage(3);
            thirdStageNotice.add(noticeModel);
        }

        if (firstStageRemind.size()!=0){
            remindModelMapperManual.insertRemindModelBatch(firstStageRemind);
        }
        if (secondStageRemind.size()!=0){
            remindModelMapperManual.insertRemindModelBatch(secondStageRemind);
        }
        if (thirdStageRemind.size()!=0){
            remindModelMapperManual.insertRemindModelBatch(thirdStageRemind);
        }


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

        if (firstStageNotice.size()!=0){
            noticeModelMapperManual.insertNoticeModelBatch(firstStageNotice);
        }
        if (secondStageNotice.size()!=0){
            noticeModelMapperManual.insertNoticeModelBatch(secondStageNotice);
        }
        if (thirdStageNotice.size()!=0){
            noticeModelMapperManual.insertNoticeModelBatch(thirdStageNotice);
        }
    }


    /**
     * 发送项目提醒信息
     * @throws IOException
     * @author zyh
     */
//    @Scheduled(fixedRate = 50000000)
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendItemMessage() throws IOException {
        List<NoticeModel> list = noticeModelMapperManual.getTodayNoticeModelList();
        for(NoticeModel noticeModel : list){
            UserInfo userInfo = userInfoMapper.getUserInfoById(noticeModel.getUserid());
            noticeModel.setSendDate(new Date());
            noticeModelMapper.updateByPrimaryKeySelective(noticeModel);
            WeChatData weChatData = new WeChatData();
            weChatData.setTouser(userInfo.getAccount());
            weChatData.setMsgtype("text");
            weChatData.setAgentid(1000002);
            Map<String,Object> map = new HashMap<>();
            map.put("content","<a href=\""+url+"/jumpPage/"+noticeModel.getPid()+"\">"+noticeModel.getContent()+"</a>");
            weChatData.setText(map);
            sendMsgService.post(weChatData);
        }
    }


    /**
     * 存储任务提醒信息
     * @author zyh
     */
//    @Scheduled(fixedRate = 50000000)
    @Scheduled(cron = "0 0 1 * * ?")
    public void task(){
        //任务提醒列表
        List<TaskRemind> remindList = new ArrayList<>();
        //任务超时列表
        List<TaskOverTime> overTimeList = new ArrayList<>();
        //任务消息列表
        List<TaskInfoNotice> noticeList = new ArrayList<>();
        //获取所有未完成的任务
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setStatus(2);
        List<TaskInfo> taskInfoListSelective = taskInfoMapperManual.getTaskInfoListSelective(taskInfo);
        for (TaskInfo t : taskInfoListSelective){
            //规定的期限时间
            Integer term = DateUtil.getBetweenHours(t.getCreateTime(), t.getEndDate());
            //当前日期与开始日期之间的时间间隔
            Integer time = DateUtil.getBetweenHours(t.getCreateTime(), new Date());
            if (time < term){
                //没超期
                if (time > term * task_complete_time){
                    TaskRemind taskRemind = new TaskRemind();
                    taskRemind.setTaskId(t.getPid());
                    taskRemind.setTaskDes(t.getTaskDes());
                    taskRemind.setInitiator(t.getInitiator());
                    taskRemind.setEndDate(t.getEndDate());
                    taskRemind.setDeptid(t.getDeptid());
                    taskRemind.setSource(t.getSource());
                    taskRemind.setLevel(t.getLevel());
                    taskRemind.setNote(t.getNote());
                    taskRemind.setCreateTime(new Date());
                    remindList.add(taskRemind);
                }

            }else {
                //超期
                TaskOverTime taskOverTime = new TaskOverTime();
                taskOverTime.setTaskId(t.getPid());
                taskOverTime.setTaskDes(t.getTaskDes());
                taskOverTime.setInitiator(t.getInitiator());
                taskOverTime.setEndDate(t.getEndDate());
                taskOverTime.setDeptid(t.getDeptid());
                taskOverTime.setSource(t.getSource());
                taskOverTime.setLevel(t.getLevel());
                taskOverTime.setNote(t.getNote());
                taskOverTime.setCreateTime(new Date());
                overTimeList.add(taskOverTime);
            }
        }
        //接收提醒消息的父部门领导集合
        if (mode == 2){
            Set<Long> remindPLeaderIds = new HashSet<>();
            for (TaskRemind taskRemind : remindList){
                OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(taskRemind.getDeptid()));
                List<Long> leaderIdByOrgId = organizationInfoMapperManual.getLeaderIdByOrgId(organizationInfo.getpId());
                for (Long id : leaderIdByOrgId) {
                    remindPLeaderIds.add(id);
                }
            }
            for (Long id : remindPLeaderIds){
                TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
                long num = 0;
                List<Long> orgIdsListByLeaderId = organizationInfoMapperManual.getOrgIdsListByLeaderId(id);
                for (int i = 0; i < remindList.size() ; i++) {
                    OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(remindList.get(i).getDeptid()));
                    if (orgIdsListByLeaderId.contains(organizationInfo.getpId())){
                        num++;
                    }
                }
                taskInfoNotice.setContent("您的子部门有"+num+"个任务将要超期，请尽快完成！");
                taskInfoNotice.setUserId(new Long(0));
                taskInfoNotice.setIsRender(0);
                taskInfoNotice.setCreatTime(new Date());
                taskInfoNotice.setUserId(id);
                taskInfoNotice.setIsOvertime(0);
//                taskInfoNoticeMapper.insertSelective(taskInfoNotice);
                noticeList.add(taskInfoNotice);

            }
        }
        //接收提醒消息的部门领导集合
        Set<Long> remindLeaderIds = new HashSet<>();
        for (TaskRemind taskRemind : remindList){
            List<Long> leaderIdByOrgId = organizationInfoMapperManual.getLeaderIdByOrgId(Long.parseLong(taskRemind.getDeptid()));
            for (Long id : leaderIdByOrgId){
                remindLeaderIds.add(id);
            }
        }
        for (Long id : remindLeaderIds){
            TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
            long num = 0;
            List<Long> orgIdsListByLeaderId = organizationInfoMapperManual.getOrgIdsListByLeaderId(id);
            for (int i = 0; i < remindList.size() ; i++) {
                if (orgIdsListByLeaderId.contains(Long.parseLong(remindList.get(i).getDeptid()))){
                    num++;
                }
            }
            taskInfoNotice.setContent("您有"+num+"个任务将要超期，请尽快完成！");
            taskInfoNotice.setIsRender(0);
            taskInfoNotice.setCreatTime(new Date());
            taskInfoNotice.setUserId(id);
            taskInfoNotice.setIsOvertime(0);
//            taskInfoNoticeMapper.insertSelective(taskInfoNotice);
            noticeList.add(taskInfoNotice);
        }
        //接收超时信息的父部门领导集合
        if (mode == 2){
            Set<Long> overtimePLeaderIds = new HashSet<>();
            for (TaskOverTime taskOverTime : overTimeList){
                OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(taskOverTime.getDeptid()));
                List<Long> leaderIdByOrgId = organizationInfoMapperManual.getLeaderIdByOrgId(organizationInfo.getpId());
                for (Long id : leaderIdByOrgId){
                    overtimePLeaderIds.add(id);
                }
            }
            for (Long id : overtimePLeaderIds){
                TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
                long num = 0;
                List<Long> orgIdsListByLeaderId = organizationInfoMapperManual.getOrgIdsListByLeaderId(id);
                for (int i = 0; i < overTimeList.size() ; i++) {
                    OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(overTimeList.get(i).getDeptid()));
                    if (orgIdsListByLeaderId.contains(organizationInfo.getpId())){
                        num++;
                    }
                }
                taskInfoNotice.setContent("您的子部门有"+num+"个任务已经超期，请尽快完成！");
                taskInfoNotice.setTaskId(new Long(0));
                taskInfoNotice.setIsRender(0);
                taskInfoNotice.setCreatTime(new Date());
                taskInfoNotice.setUserId(id);
                taskInfoNotice.setIsOvertime(1);
//                taskInfoNoticeMapper.insertSelective(taskInfoNotice);
                noticeList.add(taskInfoNotice);
            }
        }
        //接收超时信息的部门领导集合
        Set<Long> overtimeLeaderIds = new HashSet<>();
        for (TaskOverTime taskOverTime : overTimeList){
            List<Long> leaderIdByOrgId = organizationInfoMapperManual.getLeaderIdByOrgId(Long.parseLong(taskOverTime.getDeptid()));
            for (Long id : leaderIdByOrgId){
                overtimeLeaderIds.add(id);
            }
        }
        for (Long id : overtimeLeaderIds){
            TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
            long num = 0;
            List<Long> orgIdsListByLeaderId = organizationInfoMapperManual.getOrgIdsListByLeaderId(id);
            for (int i = 0; i < overTimeList.size() ; i++) {
                if (orgIdsListByLeaderId.contains(Long.parseLong(overTimeList.get(i).getDeptid()))){
                    num++;
                }
            }
            taskInfoNotice.setContent("您有"+num+"个任务已经超期，请尽快完成！");
            taskInfoNotice.setIsRender(0);
            taskInfoNotice.setCreatTime(new Date());
            taskInfoNotice.setUserId(id);
            taskInfoNotice.setIsOvertime(1);
//            taskInfoNoticeMapper.insertSelective(taskInfoNotice);
            noticeList.add(taskInfoNotice);
        }

        for (TaskRemind taskRemind : remindList){
            taskRemindMapper.insertSelective(taskRemind);
        }
        for (TaskOverTime taskOverTime : overTimeList){
            taskOverTimeMapper.insertSelective(taskOverTime);
        }
        for (TaskInfoNotice taskInfoNotice : noticeList){
            taskInfoNoticeMapper.insertSelective(taskInfoNotice);
        }


    }

    /**
     * 发送任务提醒信息
     * @author zyh
     */
//    @Scheduled(fixedRate = 50000000)
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendTaskMessage() throws WxErrorException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        //前一天的时间
        Date time = calendar.getTime();
        List<TaskInfoNotice> list = taskInfoNoticeMapperManual.getTodayNoticeInfo();
        for (TaskInfoNotice t : list){
            TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
            taskInfoNotice.setUserId(t.getUserId());
            taskInfoNotice.setSendTime(time);
            TaskInfoNotice taskInfoNoticeByUserIdAndSendTime = taskInfoNoticeMapperManual.getTaskInfoNoticeByUserIdAndSendTime(taskInfoNotice);
            if (taskInfoNoticeByUserIdAndSendTime == null){
                //修改当前提醒信息的发送时间
                t.setSendTime(date);
                taskInfoNoticeMapper.updateByPrimaryKeySelective(t);
                //发送消息
                UserInfo userInfoById = userInfoMapper.getUserInfoById(t.getUserId());
                WxCpMessage wxCpMessage = new WxCpMessage();
                wxCpMessage.setToUser(userInfoById.getAccount());
                wxCpMessage.setMsgType(WxConsts.MassMsgType.TEXT);
                String content = "<a href=\""+url+"/wx/getTaskMessageList?id="+t.getId()+"\">"+t.getContent()+"</a>";
                wxCpMessage.setContent(content);
                myWxService.messageSend(wxCpMessage);
            }
        }
    }


}
