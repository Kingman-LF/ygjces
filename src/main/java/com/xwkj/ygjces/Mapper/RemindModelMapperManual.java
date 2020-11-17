package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.RemindModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RemindModelMapperManual {

    /**
     * 批量插入提醒信息
     * @param list
     * @author zyh
     */
    public void insertRemindModelBatch(List<RemindModel> list);

    /**
     * 根据创建时间和阶段获取提醒信息的列表
     * @param createTime
     * @param stage
     * @return
     * @author zyh
     */
    public List<RemindModel> getRemindModelListByCreateTimeAndStage(@Param("createTime") Date createTime,@Param("stage") Integer stage);

}