package com.xwkj.ygjces.service;

import com.xwkj.ygjces.model.TaskOverTime;

import java.util.Date;
import java.util.List;

public interface TaskOverTimeService {
    /**
     * 根据负责部门列表和日期获取任务超时列表
     * @param depts
     * @param date
     * @return
     * @author zyh
     */
    List<TaskOverTime> getTaskOverTimeListByDeptsAndDate(List<String> depts , Date date);

    /**
     * 根据任务编号和创建时间修改任务超时信息的状态
     * @param taskId
     * @param createTime
     * @author zyh
     */
    void updateTaskOverTimeStatusByTaskIdAndCreatTime(Long taskId, Date createTime);
}
