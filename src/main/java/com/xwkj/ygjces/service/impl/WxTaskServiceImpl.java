package com.xwkj.ygjces.service.impl;

import com.xwkj.ygjces.Mapper.*;
import com.xwkj.ygjces.Mapper.auto.ApproverLogMapper;
import com.xwkj.ygjces.Mapper.auto.OrganizationInfoMapper;
import com.xwkj.ygjces.Mapper.auto.TaskInfoMapper;
import com.xwkj.ygjces.Mapper.auto.TaskInfoNoticeMapper;
import com.xwkj.ygjces.model.*;
import com.xwkj.ygjces.service.WxService;
import com.xwkj.ygjces.service.WxTaskService;
import com.xwkj.ygjces.utils.DateUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@PropertySource(value = {"classpath:/config/FilePathConfig.properties", "classpath:/config/NoticeConfig.properties", "classpath:/config/TaskConfig.properties"})
@Service
public class WxTaskServiceImpl implements WxTaskService {

    @Value("${upload.file.path}")
    public String upLoadFilePath;
    @Value("${temporary.file.path}")
    public String temporaryFilePath;
    @Value("${tn.addNotice}")
    public String addNotice;
    @Value("${tn.approvalNotice}")
    public String approvalNotice;
    @Value("${tn.rejectNotice}")
    public String rejectNotice;
    @Value("${tn.completeNotice}")
    public String completeNotice;
    @Value("${tn.lenderNotice}")
    public String lenderNotice;
    @Value("${tn.url}")
    public String taskUrl;
    @Value("${task-notice-mode}")
    public Integer mode;

    @Autowired
    TaskInfoMapper taskInfoMapper;
    @Autowired
    TaskInfoMapperManual taskInfoMapperManual;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    OrganizationInfoMapperManual organizationInfoMapperManual;
    @Autowired
    ApproverLogMapper approverLogMapper;
    @Autowired
    ApproverLogMapperManual approverLogMapperManual;
    @Autowired
    ExigencyInfoMapper exigencyInfoMapper;
    @Autowired
    OrganizationInfoMapper organizationInfoMapper;
    @Autowired
    WxService myWxService;
    @Autowired
    TaskInfoNoticeMapper taskInfoNoticeMapper;

    /**
     * 新增任务
     *
     * @param taskInfo
     * @param request
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTaskInfo(TaskInfo taskInfo, HttpServletRequest request) throws WxErrorException {
        Date date = new Date();
        //向任务表中插入数据
        Long user = (Long) request.getSession().getAttribute("user");
        UserInfo userInfo = userInfoMapper.getUserInfoById(user);
        taskInfo.setInitiator(userInfo.getId());
        taskInfo.setStatus(1);
        taskInfo.setCreateTime(date);
        taskInfoMapper.insertSelective(taskInfo);

        //向日志表中插入数据
        ApproverLog approverLog = new ApproverLog();
        approverLog.setTaskid(taskInfo.getPid());
        approverLog.setUserid(userInfo.getId());
        approverLog.setStatus(1);
        approverLog.setCreateDate(date);
        approverLogMapper.insertSelective(approverLog);

        //获取审批人编号列表
        List<Long> list = userInfoMapper.getUserIdThatHaveAprroverRole();
        for (Long id : list) {
            //将发送的消息存入数据库
            TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
            taskInfoNotice.setUserId(id);
            taskInfoNotice.setCreatTime(date);
            taskInfoNotice.setTaskId(taskInfo.getPid());
            taskInfoNotice.setIsRender(0);
            taskInfoNotice.setContent(addNotice + "任务描述：" + taskInfo.getTaskDes() + ",任务来源：" + taskInfo.getSource() + "任务备注：" + taskInfo.getNote());
            taskInfoNoticeMapper.insertSelective(taskInfoNotice);

            //发送消息
            UserInfo userInfoById = userInfoMapper.getUserInfoById(id);
            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setToUser(userInfoById.getAccount());
            wxCpMessage.setMsgType(WxConsts.MassMsgType.TEXT);
            String content = "<a href=\"" + taskUrl + "/wx/getApprovalTaskNoticeInfo?id=" + taskInfoNotice.getId() + "\">" + addNotice +
                    "\n任务描述：" + taskInfo.getTaskDes() + ",任务来源：" + taskInfo.getSource() + "任务备注：" + taskInfo.getNote() + "</a>";
            wxCpMessage.setContent(content);
            myWxService.messageSend(wxCpMessage);
        }


    }

    /**
     * 获取所有具有子组织机构的组织机构
     *
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrganizationInfo> getOrganizationInfoThatHasSubOrganization() {
        return organizationInfoMapperManual.getOrganizationInfoThatHasSubOrganization();
    }

    /**
     * 根据组织机构id获取子组织机构
     *
     * @param pId
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrganizationInfo> getOrganizationInfoByPid(Long pId) {
        return organizationInfoMapperManual.getOrganizationInfoByPid(pId);
    }

    /**
     * 获取紧急程度列表
     *
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExigencyInfo> getExigencyInfoList() {
        return exigencyInfoMapper.getExigencyInfoList(null, 1);
    }

    /**
     * 获取待审批的任务列表
     *
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TaskInfo> getTaskInfoToApproval() {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setStatus(1);
        List<TaskInfo> list = taskInfoMapperManual.getTaskInfoListSelective(taskInfo);
        for (TaskInfo t : list) {
            String[] deptIds = t.getDeptid().split(",");
            String deptNames = "";
            for (int i = 0; i < deptIds.length; i++) {
                OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(deptIds[i]));
                if (i == deptIds.length - 1) {
                    deptNames = deptNames + organizationInfo.getName();
                } else {
                    deptNames = deptNames + organizationInfo.getName() + ",";
                }
            }
            t.setDeptName(deptNames);
        }
        return list;
    }

    /**
     * 根据任务编号获取任务信息
     *
     * @param id
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskInfo getTaskInfoById(Long id) {
        TaskInfo taskInfo = taskInfoMapper.selectByPrimaryKey(id);
        String[] deptIds = taskInfo.getDeptid().split(",");
        String deptNames = "";
        for (int i = 0; i < deptIds.length; i++) {
            OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(deptIds[i]));
            if (i == deptIds.length - 1) {
                deptNames = deptNames + organizationInfo.getName();
            } else {
                deptNames = deptNames + organizationInfo.getName() + ",";
            }
        }
        ExigencyInfo level = exigencyInfoMapper.getExigencyInfoByLevel(taskInfo.getLevel().intValue());
        taskInfo.setDeptName(deptNames);
        taskInfo.setLevelName(level.getExName());
        return taskInfo;
    }

    /**
     * 审批任务
     *
     * @param taskInfo
     * @param reason
     * @param request
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approvalTasks(TaskInfo taskInfo, String reason, HttpServletRequest request) throws WxErrorException {
        Long user = (Long) request.getSession().getAttribute("user");
        if (taskInfo.getStatus() == 2){
            taskInfo.setpId(new Long(0));
        }
        taskInfoMapper.updateByPrimaryKeySelective(taskInfo);
        ApproverLog approverLog = new ApproverLog();
        approverLog.setTaskid(taskInfo.getPid());
        approverLog.setUserid(user);
        approverLog.setCreateDate(new Date());
        approverLog.setStatus(taskInfo.getStatus());
        approverLog.setReason(reason);
        approverLogMapper.insertSelective(approverLog);

        Date date = new Date();
        TaskInfo info = taskInfoMapper.selectByPrimaryKey(taskInfo.getPid());
        if (taskInfo.getStatus() == 2) {
            String[] deptIds = info.getDeptid().split(",");
            for (int i = 0; i < deptIds.length; i++) {
                //将任务按部门编号分成多个任务,插入
                TaskInfo tInfo = new TaskInfo();
                tInfo.setTaskDes(info.getTaskDes());
                tInfo.setInitiator(info.getInitiator());
                tInfo.setEndDate(info.getEndDate());
                tInfo.setDeptid(deptIds[i]);
                tInfo.setStatus(2);
                tInfo.setSource(info.getSource());
                tInfo.setLevel(info.getLevel());
                tInfo.setNote(info.getNote());
                tInfo.setCreateTime(date);
                tInfo.setpId(info.getPid());
                taskInfoMapper.insertSelective(tInfo);

                if (mode == 2) {
                    //给当前部门的父部门领导发送消息
                    OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(deptIds[i]));
                    //获取父部门领导
                    List<Long> leaderIdByOrgId = organizationInfoMapperManual.getLeaderIdByOrgId(organizationInfo.getpId());
                    for (Long id : leaderIdByOrgId) {
                        TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
                        taskInfoNotice.setUserId(id);
                        taskInfoNotice.setCreatTime(date);
                        taskInfoNotice.setTaskId(tInfo.getPid());
                        taskInfoNotice.setIsRender(0);
                        taskInfoNotice.setContent(approvalNotice + "任务描述：" + tInfo.getTaskDes() + ",任务来源：" + tInfo.getSource() + "任务备注：" + tInfo.getNote());
                        taskInfoNoticeMapper.insertSelective(taskInfoNotice);

                        //发送消息
                        UserInfo userInfoById = userInfoMapper.getUserInfoById(id);
                        WxCpMessage wxCpMessage = new WxCpMessage();
                        wxCpMessage.setToUser(userInfoById.getAccount());
                        wxCpMessage.setMsgType(WxConsts.MassMsgType.TEXT);
                        String content = "<a href=\"" + taskUrl + "/wx/getNotCompleteTaskNoticeInfo?id=" +
                                taskInfoNotice.getId() + "\">" + lenderNotice +" "
                                +organizationInfo.getName()+" 有一个待完成的任务需要完成："+
                                "\n任务描述：" + tInfo.getTaskDes() + ",任务来源：" + tInfo.getSource() + "任务备注：" + tInfo.getNote() + "</a>";
                        wxCpMessage.setContent(content);
                        myWxService.messageSend(wxCpMessage);
                    }
                }

                //获取负责该任务的部门领导编号列表
                List<Long> leaderIdByOrgId = organizationInfoMapperManual.getLeaderIdByOrgId(Long.parseLong(deptIds[i]));
                for (Long id : leaderIdByOrgId) {
                    TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
                    taskInfoNotice.setUserId(id);
                    taskInfoNotice.setCreatTime(date);
                    taskInfoNotice.setTaskId(tInfo.getPid());
                    taskInfoNotice.setIsRender(0);
                    taskInfoNotice.setContent(approvalNotice + "任务描述：" + tInfo.getTaskDes() + ",任务来源：" + tInfo.getSource() + "任务备注：" + tInfo.getNote());
                    taskInfoNoticeMapper.insertSelective(taskInfoNotice);

                    //发送消息
                    UserInfo userInfoById = userInfoMapper.getUserInfoById(id);
                    WxCpMessage wxCpMessage = new WxCpMessage();
                    wxCpMessage.setToUser(userInfoById.getAccount());
                    wxCpMessage.setMsgType(WxConsts.MassMsgType.TEXT);
                    String content = "<a href=\"" + taskUrl + "/wx/getNotCompleteTaskNoticeInfo?id=" + taskInfoNotice.getId() + "\">" + approvalNotice +
                            "\n任务描述：" + tInfo.getTaskDes() + ",任务来源：" + tInfo.getSource() + "任务备注：" + tInfo.getNote() + "</a>";
                    wxCpMessage.setContent(content);
                    myWxService.messageSend(wxCpMessage);
                }

            }
        } else {
            TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
            taskInfoNotice.setUserId(info.getInitiator());
            taskInfoNotice.setCreatTime(date);
            taskInfoNotice.setTaskId(info.getPid());
            taskInfoNotice.setIsRender(0);
            taskInfoNotice.setContent(rejectNotice + "任务描述：" + info.getTaskDes() + ",任务来源：" + info.getSource() + "任务备注：" + info.getNote());
            taskInfoNoticeMapper.insertSelective(taskInfoNotice);

            //发送消息
            UserInfo userInfoById = userInfoMapper.getUserInfoById(info.getInitiator());
            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setToUser(userInfoById.getAccount());
            wxCpMessage.setMsgType(WxConsts.MassMsgType.TEXT);
            String content = "<a href=\"" + taskUrl + "/wx/getRejectTaskNoticeInfo?id=" + taskInfoNotice.getId() + "\">" + rejectNotice +
                    "\n任务描述：" + info.getTaskDes() + ",任务来源：" + info.getSource() + "任务备注：" + info.getNote() + "</a>";
            wxCpMessage.setContent(content);
            myWxService.messageSend(wxCpMessage);
        }

    }

    /**
     * 根据当前用户所在部门获取未完成的任务信息和已完成的任务信息
     *
     * @param request
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getTaskInfoByUserDepartments(HttpServletRequest request) {

        Date date = new Date();

        //查找当前登录用户所在的所有部门，并将部门编号封装成一个集合
        Long user = (Long) request.getSession().getAttribute("user");
        List<OrganizationInfo> organizationInfoByUid = organizationInfoMapperManual.getOrganizationInfoByUid(user);
        List<Long> orgIds = new ArrayList<>();
        for (OrganizationInfo o : organizationInfoByUid) {
            orgIds.add(o.getId());
        }
        //查找未完成的任务信息和已完成的任务信息，封装到一个map中
        Map<String, Object> map = new HashMap<>();
        List<TaskInfo> notCompleted = taskInfoMapperManual.getNotCompletedTaskInfoListByDeptIds(orgIds);
        for (TaskInfo t : notCompleted) {
            OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(t.getDeptid()));
            t.setDeptName(organizationInfo.getName());
            t.setNowTime(date);
        }
        List<TaskInfo> completed = taskInfoMapperManual.getCompletedTaskInfoListByDeptIds(orgIds);
        for (TaskInfo t : completed) {
            OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(t.getDeptid()));
            t.setDeptName(organizationInfo.getName());
            t.setNowTime(date);
        }
        map.put("notCompleted", notCompleted);
        map.put("completed", completed);
        return map;
    }

    /**
     * 根据主键获取未完成的任务信息
     *
     * @param id
     * @param request
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskInfo getDetailsOfUnfinishedTasksById(Long id, HttpServletRequest request) {
        Long user = (Long) request.getSession().getAttribute("user");
        UserInfo userInfo = userInfoMapper.getUserInfoById(user);
        Date date = new Date();
        TaskInfo taskInfo = taskInfoMapper.selectByPrimaryKey(id);
        OrganizationInfo dept = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(taskInfo.getDeptid()));
        ExigencyInfo level = exigencyInfoMapper.getExigencyInfoByLevel(taskInfo.getLevel().intValue());
        taskInfo.setDeptName(dept.getName());
        taskInfo.setLevelName(level.getExName());
        taskInfo.setActualEndDate(date);
        taskInfo.setUserName(userInfo.getTrueName());
        if (date.compareTo(taskInfo.getEndDate()) <= 0) {
            //提前完成
            Integer days = DateUtil.getBetweenDay(date, taskInfo.getEndDate());
            taskInfo.setFirstDate(days);
        } else {
            //超时完成
            Integer days = DateUtil.getBetweenDay(date, taskInfo.getEndDate());
            taskInfo.setOverDate(days);
        }

        //判断当前用户是否是传递过来的任务信息所负责部门的领导
        Long leaderAndOrgInfoByLeaderIdAndOrgId
                = organizationInfoMapperManual.getLeaderAndOrgInfoByLeaderIdAndOrgId(user, Long.parseLong(taskInfo.getDeptid()));
        if (leaderAndOrgInfoByLeaderIdAndOrgId != null) {
            taskInfo.setIsLeader(1);
        }

        return taskInfo;
    }


    /**
     * 完成任务
     *
     * @param file
     * @param taskInfo
     * @param reason
     * @param request
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(MultipartFile file, TaskInfo taskInfo, String reason, HttpServletRequest request) throws IOException, WxErrorException {
        TaskInfo taskInfo1 = taskInfoMapper.selectByPrimaryKey(taskInfo.getPid());
        if (taskInfo1.getStatus() == 3){

        }else {
            Long user = (Long) request.getSession().getAttribute("user");
            taskInfo.setStatus(3);
            taskInfoMapper.updateByPrimaryKeySelective(taskInfo);

            ApproverLog approverLog = new ApproverLog();
            approverLog.setTaskid(taskInfo.getPid());
            approverLog.setUserid(user);
            approverLog.setReason(reason);
            approverLog.setStatus(3);
            approverLog.setCreateDate(taskInfo.getActualEndDate());
            if (file == null) {
                approverLog.setFilepath("无上传文件");
            } else {
                String fileNewName = DateUtil.dateToString(new Date(), "yyyyMMddHHmmss") + file.getOriginalFilename();
//            String path = new String("D:/Users/zhang/Company/upload/");
                File fileDir = new File(upLoadFilePath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                //压缩图片并保存
                Thumbnails.of(file.getInputStream()).scale(0.5f).outputQuality(0.15f).toFile(upLoadFilePath + fileNewName);
//            File newFile = new File(fileDir.getAbsolutePath() + File.separator + fileNewName);
//            file.transferTo(newFile);
                approverLog.setFilepath(fileNewName);
            }
            approverLogMapper.insertSelective(approverLog);

            //向任务消息列表插入信息
            TaskInfo info = taskInfoMapper.selectByPrimaryKey(taskInfo.getPid());
            TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
            taskInfoNotice.setUserId(info.getInitiator());
            taskInfoNotice.setCreatTime(new Date());
            taskInfoNotice.setTaskId(info.getPid());
            taskInfoNotice.setIsRender(0);
            taskInfoNotice.setContent(completeNotice + "任务描述：" + info.getTaskDes() + ",任务来源：" + info.getSource() + "任务备注：" + info.getNote());
            taskInfoNoticeMapper.insertSelective(taskInfoNotice);

            //发送消息
            UserInfo userInfoById = userInfoMapper.getUserInfoById(info.getInitiator());
            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setToUser(userInfoById.getAccount());
            wxCpMessage.setMsgType(WxConsts.MassMsgType.TEXT);
            String content = "<a href=\"" + taskUrl + "/wx/getCompleteTaskNoticeInfo?id=" + taskInfoNotice.getId() + "\">" + completeNotice +
                    "\n任务描述：" + info.getTaskDes() + ",任务来源：" + info.getSource() + "任务备注：" + info.getNote() + "</a>";
            wxCpMessage.setContent(content);
            myWxService.messageSend(wxCpMessage);
        }


    }

    /**
     * 根据任务主键获取已经完成的任务的相关信息
     *
     * @param id
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getDetailsOfFinishedTasksById(Long id) {
        Map<String, Object> map = new HashMap<>();
        //任务详情
        TaskInfo taskInfo = taskInfoMapper.selectByPrimaryKey(id);
        OrganizationInfo dept = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(taskInfo.getDeptid()));
        ExigencyInfo level = exigencyInfoMapper.getExigencyInfoByLevel(taskInfo.getLevel().intValue());
        UserInfo taskUser = userInfoMapper.getUserInfoById(taskInfo.getInitiator());
        taskInfo.setDeptName(dept.getName());
        taskInfo.setLevelName(level.getExName());
        taskInfo.setUserName(taskUser.getTrueName());
        //审批记录
        ApproverLog approverLog = approverLogMapperManual.getApproverLogByTaskIdAndStatus(taskInfo.getpId(), 2);
        UserInfo approverUser = userInfoMapper.getUserInfoById(approverLog.getUserid());
        approverLog.setUserName(approverUser.getTrueName());
        //完成记录
        ApproverLog completeLog = approverLogMapperManual.getApproverLogByTaskIdAndStatus(id, 3);
        UserInfo completeUser = userInfoMapper.getUserInfoById(completeLog.getUserid());
        completeLog.setUserName(completeUser.getTrueName());
        map.put("taskInfo", taskInfo);
        map.put("approverLog", approverLog);
        map.put("completeLog", completeLog);
        return map;
    }

    /**
     * 回显图片
     *
     * @param file
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String echoPicture(MultipartFile file) throws IOException {
        String fileNewName = DateUtil.dateToString(new Date(), "yyyyMMddHHmmss") + file.getOriginalFilename();
//        String path = new String("D:/Users/zhang/Company/temporary/");
        File fileDir = new File(temporaryFilePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        //压缩图片并保存
        Thumbnails.of(file.getInputStream()).scale(0.5f).outputQuality(0.15f).toFile(temporaryFilePath + fileNewName);
        return fileNewName;
    }

    /**
     * 根据任务状态获取任务列表
     *
     * @param taskInfo
     * @param request
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TaskInfo> getTaskInfoListByStatus(TaskInfo taskInfo, HttpServletRequest request) {
        Long user = (Long) request.getSession().getAttribute("user");
        taskInfo.setInitiator(user);
        List<TaskInfo> list = taskInfoMapperManual.getTaskInfoListSelective(taskInfo);
        for (TaskInfo t : list) {
            String[] deptIds = t.getDeptid().split(",");
            String deptNames = "";
            for (int i = 0; i < deptIds.length; i++) {
                OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(deptIds[i]));
                if (i == deptIds.length - 1) {
                    deptNames = deptNames + organizationInfo.getName();
                } else {
                    deptNames = deptNames + organizationInfo.getName() + ",";
                }
            }
            t.setDeptName(deptNames);
        }
        return list;
    }

    /**
     * 根据任务编号和该任务所处的状态获取不同的任务详情
     *
     * @param id
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getMyTaskInfo(Long id) {
        Map<String, Object> map = new HashMap<>();
        TaskInfo taskInfo = taskInfoMapper.selectByPrimaryKey(id);
        String[] deptIds = taskInfo.getDeptid().split(",");
        String deptNames = "";
        for (int i = 0; i < deptIds.length; i++) {
            OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(deptIds[i]));
            if (i == deptIds.length - 1) {
                deptNames = deptNames + organizationInfo.getName();
            } else {
                deptNames = deptNames + organizationInfo.getName() + ",";
            }
        }
        ExigencyInfo level = exigencyInfoMapper.getExigencyInfoByLevel(taskInfo.getLevel().intValue());
        UserInfo taskUser = userInfoMapper.getUserInfoById(taskInfo.getInitiator());
        taskInfo.setDeptName(deptNames);
        taskInfo.setLevelName(level.getExName());
        taskInfo.setUserName(taskUser.getTrueName());
        if (taskInfo.getStatus() == 1 || taskInfo.getStatus() == 6) {
            map.put("taskInfo", taskInfo);
        } else if (taskInfo.getStatus() == 2) {
            ApproverLog approverLog = approverLogMapperManual.getApproverLogByTaskIdAndStatus(taskInfo.getpId(), taskInfo.getStatus());
            UserInfo userInfoById = userInfoMapper.getUserInfoById(approverLog.getUserid());
            approverLog.setUserName(userInfoById.getTrueName());
            map.put("taskInfo", taskInfo);
            map.put("approverLog", approverLog);
        } else if (taskInfo.getStatus() == 3 || taskInfo.getStatus() == 4) {
            ApproverLog approverLog = approverLogMapperManual.getApproverLogByTaskIdAndStatus(taskInfo.getpId(), 2);
            UserInfo userInfoById = userInfoMapper.getUserInfoById(approverLog.getUserid());
            approverLog.setUserName(userInfoById.getTrueName());


            ApproverLog completeLog = approverLogMapperManual.getApproverLogByTaskIdAndStatus(taskInfo.getPid(), taskInfo.getStatus());
            UserInfo userInfoById1 = userInfoMapper.getUserInfoById(completeLog.getUserid());
            completeLog.setUserName(userInfoById1.getTrueName());
            if (taskInfo.getStatus() == 4) {
                ApproverLog midLog = approverLogMapperManual.getApproverLogByTaskIdAndStatus(taskInfo.getPid(), 3);
                completeLog.setFilepath(midLog.getFilepath());
            }
            map.put("taskInfo", taskInfo);
            map.put("approverLog", approverLog);
            map.put("completeLog", completeLog);
        } else if (taskInfo.getStatus() == 5) {
            ApproverLog approverLog = approverLogMapperManual.getApproverLogByTaskIdAndStatus(taskInfo.getPid(), taskInfo.getStatus());
            UserInfo userInfoById = userInfoMapper.getUserInfoById(approverLog.getUserid());
            approverLog.setUserName(userInfoById.getTrueName());
            map.put("taskInfo", taskInfo);
            map.put("approverLog", approverLog);
        }

        return map;
    }

    /**
     * 确认完成任务
     *
     * @param taskInfo
     * @param request
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmationOfCompletion(TaskInfo taskInfo, HttpServletRequest request) {
        Long user = (Long) request.getSession().getAttribute("user");
        taskInfo.setStatus(4);
        taskInfoMapper.updateByPrimaryKeySelective(taskInfo);
        ApproverLog approverLog = new ApproverLog();
        approverLog.setTaskid(taskInfo.getPid());
        approverLog.setUserid(user);
        approverLog.setCreateDate(new Date());
        approverLog.setReason("已完成");
        approverLog.setStatus(4);
        approverLogMapper.insertSelective(approverLog);
    }

    /**
     * 任务再次提交
     *
     * @param taskInfo
     * @param request
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskResubmit(TaskInfo taskInfo, HttpServletRequest request) throws WxErrorException {
        taskInfo.setStatus(1);
        taskInfoMapper.updateByPrimaryKeySelective(taskInfo);
        Long user = (Long) request.getSession().getAttribute("user");
        ApproverLog approverLog = new ApproverLog();
        approverLog.setTaskid(taskInfo.getPid());
        approverLog.setStatus(1);
        approverLog.setUserid(user);
        approverLog.setReason("重新提交");
        approverLog.setCreateDate(new Date());
        approverLogMapper.insertSelective(approverLog);

        //获取审批人编号列表
        List<Long> list = userInfoMapper.getUserIdThatHaveAprroverRole();
        for (Long id : list) {
            //将发送的消息存入数据库
            TaskInfoNotice taskInfoNotice = new TaskInfoNotice();
            taskInfoNotice.setUserId(id);
            taskInfoNotice.setCreatTime(new Date());
            taskInfoNotice.setTaskId(taskInfo.getPid());
            taskInfoNotice.setIsRender(0);
            taskInfoNotice.setContent(addNotice + "任务描述：" + taskInfo.getTaskDes() + ",任务来源：" + taskInfo.getSource() + "任务备注：" + taskInfo.getNote());
            taskInfoNoticeMapper.insertSelective(taskInfoNotice);

            //发送消息
            UserInfo userInfoById = userInfoMapper.getUserInfoById(id);
            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setToUser(userInfoById.getAccount());
            wxCpMessage.setMsgType(WxConsts.MassMsgType.TEXT);
            String content = "<a href=\"" + taskUrl + "/wx/getApprovalTaskNoticeInfo?id=" + taskInfoNotice.getId() + "\">" + addNotice +
                    "\n任务描述：" + taskInfo.getTaskDes() + ",任务来源：" + taskInfo.getSource() + "任务备注：" + taskInfo.getNote() + "</a>";
            wxCpMessage.setContent(content);
            myWxService.messageSend(wxCpMessage);
        }
    }

    /**
     * 任务废弃
     *
     * @param taskInfo
     * @param request
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskDiscard(TaskInfo taskInfo, HttpServletRequest request) {
        taskInfo.setStatus(6);
        taskInfoMapper.updateByPrimaryKeySelective(taskInfo);
        Long user = (Long) request.getSession().getAttribute("user");
        ApproverLog approverLog = new ApproverLog();
        approverLog.setTaskid(taskInfo.getPid());
        approverLog.setStatus(6);
        approverLog.setUserid(user);
        approverLog.setReason("废弃");
        approverLog.setCreateDate(new Date());
        approverLogMapper.insertSelective(approverLog);
    }

    /**
     * 获取超时完成和提前完成的任务排行信息
     *
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getOverTimeAndAdvanceTaskRankingList() {
        List<Object> list = new ArrayList<>();
        List<String> overTimeTaskName = new ArrayList<>();
        List<Long> overTimeTaskCount = new ArrayList<>();
        List<String> advanceTaskName = new ArrayList<>();
        List<Long> advanceTaskCount = new ArrayList<>();

        List<NameAndCount> overTimeTaskRankingList = taskInfoMapperManual.getOverTimeTaskRankingList();
        for (NameAndCount n : overTimeTaskRankingList) {
            OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(n.getName()));
            overTimeTaskName.add(organizationInfo.getName());
            overTimeTaskCount.add(n.getCount());
        }
        List<NameAndCount> advanceTaskRankingList = taskInfoMapperManual.getAdvanceTaskRankingList();
        for (NameAndCount n : advanceTaskRankingList) {
            OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(n.getName()));
            advanceTaskName.add(organizationInfo.getName());
            advanceTaskCount.add(n.getCount());
        }
        list.add(overTimeTaskName);
        list.add(overTimeTaskCount);
        list.add(advanceTaskName);
        list.add(advanceTaskCount);

        return list;
    }

    /**
     * 根据父部门领导编号获取获取其领导部门的所有子部门
     * @param id
     * @return
     * @author zyh
     */
    @Override
    public List<String> getSubOrgIdsByParentLeaderId(Long id) {
        ArrayList<String> list = new ArrayList<>();
        List<Long> orgIdsListByLeaderId = organizationInfoMapperManual.getOrgIdsListByLeaderId(id);
        for (Long oid : orgIdsListByLeaderId){
            List<Long> subOrgIdsByPid = organizationInfoMapperManual.getSubOrgIdsByPid(oid);
            for (Long subId : subOrgIdsByPid){
                list.add(String.valueOf(subId));
            }
        }
        return list;
    }

    /**
     * @Description : 根据部门名称获取超时完成的任务列表
     * @methodName : getOverTimeTaskInfoByDeptName
     * @param deptName :
     * @return : java.util.List<com.xwkj.ygjces.model.TaskInfo>
     * @exception :
     * @author : zyh
     */
    @Override
    public List<TaskInfo> getOverTimeTaskInfoByDeptName(String deptName) {
        OrganizationInfo organizationInfo = organizationInfoMapperManual.getOrganizationInfoByOrgName(deptName);
        List<TaskInfo> overTimeTaskInfoByDeptId = taskInfoMapperManual.getOverTimeTaskInfoByDeptId(String.valueOf(organizationInfo.getId()));
        for (TaskInfo taskInfo :overTimeTaskInfoByDeptId){
            taskInfo.setDeptName(deptName);
        }
        return overTimeTaskInfoByDeptId;
    }

    /**
     * @Description : 根据部门名称获取提前完成的任务列表
     * @methodName : getAdvanceTaskInfoByDeptName
     * @param deptName :
     * @return : java.util.List<com.xwkj.ygjces.model.TaskInfo>
     * @exception :
     * @author : zyh
     */
    @Override
    public List<TaskInfo> getAdvanceTaskInfoByDeptName(String deptName) {
        OrganizationInfo organizationInfo = organizationInfoMapperManual.getOrganizationInfoByOrgName(deptName);
        List<TaskInfo> advanceTaskInfoByDeptId = taskInfoMapperManual.getAdvanceTaskInfoByDeptId(String.valueOf(organizationInfo.getId()));
        for (TaskInfo taskInfo :advanceTaskInfoByDeptId ){
            taskInfo.setDeptName(deptName);
        }
        return advanceTaskInfoByDeptId;
    }

    /**
     * @Description : 取消任务
     * @methodName : cancelTask
     * @param id :
     * @return : void
     * @exception :
     * @author : zyh
     */
    @Override
    public void cancelTask(Long id){
        taskInfoMapper.deleteByPrimaryKey(id);
        approverLogMapperManual.deleteApproverLogByTaskId(id);
    }
}
