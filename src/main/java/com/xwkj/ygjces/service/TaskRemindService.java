package com.xwkj.ygjces.service;

import com.xwkj.ygjces.model.TaskRemind;

import java.util.Date;
import java.util.List;

public interface TaskRemindService {

    /**
     * 根据负责部门列表和日期获取任务提醒列表
     * @param depts
     * @param date
     * @return
     * @author zyh
     */
    List<TaskRemind> getTaskRemindListByByDeptsAndDate(List<String> depts ,  Date date);

    /**
     * 根据任务编号和创建时间修改任务提醒信息的状态
     * @param taskId
     * @param createTime
     * @author zyh
     */
    void updateTaskRemindStatusByTaskIdAndCreatTime(Long taskId , Date createTime);

}
