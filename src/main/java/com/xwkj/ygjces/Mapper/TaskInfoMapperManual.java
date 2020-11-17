package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.NameAndCount;
import com.xwkj.ygjces.model.TaskInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskInfoMapperManual {

    /**
     * 有条件的获取任务列表
     * @param taskInfo
     * @return
     * @author zyh
     */
    List<TaskInfo> getTaskInfoListSelective(TaskInfo taskInfo);


    /**
     * 根据部门编号列表获取审批通过但是没有完成的任务列表
     * @param deptIds
     * @return
     * @author zyh
     */
    List<TaskInfo> getNotCompletedTaskInfoListByDeptIds(List<Long> deptIds);

    /**
     * 根据部门编号列表获取已完成的任务列表
     * @param deptIds
     * @return
     * @author zyh
     */
    List<TaskInfo> getCompletedTaskInfoListByDeptIds(List<Long> deptIds);

    /**
     * 获取超时完成的任务排行列表
     * @return
     * @author zyh
     */
    List<NameAndCount> getOverTimeTaskRankingList();

    /**
     * 获取提前完成的任务排行列表
     * @return
     * @author zyh
     */
    List<NameAndCount> getAdvanceTaskRankingList();

    /**
     * @Description : 根据部门编号获取任务排行榜中超时完成任务的列表
     * @methodName : getOverTimeTaskInfoByDeptId
     * @param deptId : 
     * @return : java.util.List<com.xwkj.ygjces.model.TaskInfo>
     * @exception : 
     * @author : zyh
     */
    List<TaskInfo> getOverTimeTaskInfoByDeptId(@Param("deptId") String deptId);
    
    /**
     * @Description : 根据部门编号获取任务排行榜中提前完成任务的列表
     * @methodName : getAdvanceTaskInfoByDeptId
     * @param deptId : 
     * @return : java.util.List<com.xwkj.ygjces.model.TaskInfo>
     * @exception : 
     * @author : zyh
     */
    List<TaskInfo> getAdvanceTaskInfoByDeptId(@Param("deptId") String deptId);

}