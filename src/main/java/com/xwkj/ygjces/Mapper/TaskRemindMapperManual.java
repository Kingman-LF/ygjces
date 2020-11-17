package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.TaskRemind;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRemindMapperManual {

    /**
     * 根据负责部门列表和日期获取任务提醒列表
     * @param depts
     * @param date
     * @return
     * @author zyh
     */
    List<TaskRemind> getTaskRemindListByByDeptsAndDate(@Param("list") List<String> depts , @Param("createTime") Date date);

    /**
     * 根据任务编号和创建时间修改任务提醒信息的状态
     * @param taskId
     * @param createTime
     * @author zyh
     */
    void updateTaskRemindStatusByTaskIdAndCreatTime(@Param("taskId") Long taskId , @Param("createTime") Date createTime);


}