package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.ApproverLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproverLogMapperManual {

    /**
     * 根据任务编号和状态获取审批日志信息
     * @param id
     * @param status
     * @return
     * @author zyh
     */
    ApproverLog getApproverLogByTaskIdAndStatus(@Param("id") Long id , @Param("status") Integer status);

    /**
     * @Description : 根据任务编号删除日志信息
     * @methodName : deleteApproverLogByTaskId
     * @param id :
     * @return : void
     * @exception :
     * @author : zyh
     */
    void deleteApproverLogByTaskId(Long id);

}