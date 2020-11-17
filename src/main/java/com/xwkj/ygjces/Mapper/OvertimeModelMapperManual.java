package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.NameAndCount;
import com.xwkj.ygjces.model.OvertimeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Repository
public interface OvertimeModelMapperManual {

    /**
     * 批量插入超时的样品信息
     * @param list
     * @author zyh
     */
    public void insertOvertimeModelBatch(List<OvertimeModel> list);

    /**
     * 根据样品编号和阶段号查找超时的样品信息
     * @param syNum
     * @param stage
     * @return
     * @author zyh
     */
    public OvertimeModel getOvertimeModelBySynumAndStage(@Param("syNum") String syNum,@Param("stage") Integer stage);

    /**
     * 根据更新时间和阶段查找超时的样品信息列表
     * @param updateDate
     * @param stage
     * @return
     * @author zyh
     */
    List<OvertimeModel> getOvertimeModelListByUpdateDateAndStage(@Param("updateDate") Date updateDate,@Param("stage") Integer stage);


    /**
     * 获取超时排名数据列表
     * @return
     * @author zyh
     */
    List<NameAndCount> getOverTimeRankingDataList();
}