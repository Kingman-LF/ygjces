package com.xwkj.ygjces.service.impl;

import com.xwkj.ygjces.Mapper.TaskRemindMapperManual;
import com.xwkj.ygjces.Mapper.auto.OrganizationInfoMapper;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.TaskRemind;
import com.xwkj.ygjces.service.TaskRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskRemindServiceImpl implements TaskRemindService {

    @Autowired
    TaskRemindMapperManual taskRemindMapperManual;
    @Autowired
    OrganizationInfoMapper organizationInfoMapper;

    /**
     * 根据负责部门列表和日期获取任务提醒列表
     * @param depts
     * @param date
     * @return
     * @author zyh
     */
    @Override
    public List<TaskRemind> getTaskRemindListByByDeptsAndDate(List<String> depts, Date date) {
        List<TaskRemind> taskRemindListByByDeptsAndDate = taskRemindMapperManual.getTaskRemindListByByDeptsAndDate(depts, date);
        for (TaskRemind t : taskRemindListByByDeptsAndDate){
            OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(t.getDeptid()));
            t.setDeptName(organizationInfo.getName());
        }
        return taskRemindListByByDeptsAndDate;
    }

    /**
     * 根据任务编号和创建时间修改任务提醒信息的状态
     * @param taskId
     * @param createTime
     * @author zyh
     */
    @Override
    public void updateTaskRemindStatusByTaskIdAndCreatTime(Long taskId, Date createTime) {
        taskRemindMapperManual.updateTaskRemindStatusByTaskIdAndCreatTime(taskId,createTime);
    }
}
