package com.xwkj.ygjces.service.impl;

import com.xwkj.ygjces.Mapper.TaskOverTimeMapperManual;
import com.xwkj.ygjces.Mapper.auto.OrganizationInfoMapper;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.TaskOverTime;
import com.xwkj.ygjces.service.TaskOverTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskOverTimeServiceImpl implements TaskOverTimeService {

    @Autowired
    TaskOverTimeMapperManual taskOverTimeMapperManual;
    @Autowired
    OrganizationInfoMapper organizationInfoMapper;

    /**
     * 根据负责部门列表和日期获取任务超时列表
     * @param depts
     * @param date
     * @return
     * @author zyh
     */
    @Override
    public List<TaskOverTime> getTaskOverTimeListByDeptsAndDate(List<String> depts, Date date) {
        List<TaskOverTime> taskOverTimeListByDeptsAndDate = taskOverTimeMapperManual.getTaskOverTimeListByDeptsAndDate(depts, date);
        for (TaskOverTime t : taskOverTimeListByDeptsAndDate){
            OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(Long.parseLong(t.getDeptid()));
            t.setDeptName(organizationInfo.getName());
        }
        return taskOverTimeListByDeptsAndDate;
    }

    /**
     * 根据任务编号和创建时间修改任务超时信息的状态
     * @param taskId
     * @param createTime
     * @author zyh
     */
    @Override
    public void updateTaskOverTimeStatusByTaskIdAndCreatTime(Long taskId, Date createTime) {
        taskOverTimeMapperManual.updateTaskOverTimeStatusByTaskIdAndCreatTime(taskId,createTime);
    }


}
