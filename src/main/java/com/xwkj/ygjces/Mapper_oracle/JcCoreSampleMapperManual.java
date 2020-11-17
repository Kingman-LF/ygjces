package com.xwkj.ygjces.Mapper_oracle;

import com.xwkj.ygjces.controller.vo.JcXmCategoryAmountVo;
import com.xwkj.ygjces.controller.vo.JcXtcsXmWithSampleVo;
import com.xwkj.ygjces.controller.vo.WtArrearsDetailsVo;
import com.xwkj.ygjces.controller.vo.WtUnitAmountVo;
import com.xwkj.ygjces.model.JcCoreSample;
import com.xwkj.ygjces.model.OrgItemIntermediate;
import com.xwkj.ygjces.model.SampleInfo;
import com.xwkj.ygjces.model.StageTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JcCoreSampleMapperManual {

    /**
     * 返回委托单位欠费情况
     * @param wtUnit
     * @param startTime
     * @param endTime
     * @return
     */
    List<WtArrearsDetailsVo> selectWtArrearsDetails(@Param("wtUnit") String wtUnit , @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    /**
     * 获取没有与当前部门绑定的检测项目列表
     * @return
     * @author zyh
     */
    List<OrgItemIntermediate> getItemsThatNotBoundToTheOrg(List<String> itemIds);

    /**
     * 根据项目id列表获取检测样品信息
     * @param startTime
     * @param endTime
     * @param itemIds
     * @return
     * @author zyh
     */
    List<SampleInfo> getSampleInfoByItemIds(@Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("list")List<String> itemIds);

    /**
     * 根据项目id获取该项目规定的合格时间
     * @param itemId
     * @return
     * @author zyh
     */
    String getProvisionalTimeByItemId(@Param("itemId") String itemId);

    /**
     * 根据筛选年份统计检测项数量
     * @param categoryId
     * @param currentYear
     * @return
     */
    public Integer getJcXtcsXmCount(@Param("categoryId") String categoryId, @Param("currentYear") Object currentYear);

    /**
     * 按照筛选时间查询检测行名称和统计数量
     * @param defaultDate
     * @param xmName
     * @param start
     * @param end
     * @return
     */
    public List<JcXtcsXmWithSampleVo> getJcXtcsXmNameAndCount(@Param("defaultDate") String defaultDate, @Param("xmName") String xmName, @Param("start")Date start, @Param("end")Date end);


    /**
     * 获取所有第一阶段的样品列表
     * @return
     * @author zyh
     */
    public List<SampleInfo> getFirstStageSampleInfoList();

    /**
     * 获取所有第二阶段的样品列表
     * @return
     * @author zyh
     */
    public List<SampleInfo> getSecondStageSampleInfoList();

    /**
     * 获取所有第三阶段的样品列表
     * @return
     * @author zyh
     */
    public List<SampleInfo> getThirdStageSampleInfoList();

    /**
     * 根据项目编号获取该项目在各个阶段所需要消耗的时间
     * @param itemId
     * @return
     * @author zyh
     */
    StageTime getStageTimeByItemId(@Param("itemId") String itemId);

    /**
     * 当月报告数量
     * @return
     * @author wanglei
     */
    public Integer selectReportCountByMonth(String currYearMonth);

    /**
     * 当月打印报告数量
     * @return
     * @author wanglei
     */
    public Integer selectPrintReportCountByMonth(String currYearMonth);

    /**
     *查询当天打印报告量
     * @author wanglei
     */
    public Integer selectPrintReportCountWithOneDay();

    /**
     *查询当月打印报告量
     * @author wanglei
     */
    public Integer selectPrintReportCountWithOneMonth();


    /**
     * @Description : 获取所有已完成的样品列表
     * @methodName : getOvertimeList
     * @return : java.util.List<com.xwkj.ygjces.model.SampleInfo>
     * @exception :
     * @author : zyh
     */
    List<SampleInfo> getFinishedList();

    /**
     * @Description : 按照条件获取所有已完成的样品列表
     * @methodName : getFinishedListByCondition
     * @param startTime :
     * @param endTime :
     * @param itemName :
     * @return : java.util.List<com.xwkj.ygjces.model.SampleInfo>
     * @exception :
     * @author : zyh
     */
    List<SampleInfo> getFinishedListByCondition(@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("itemName") String itemName);

    /**
     * @Description : 按照条件获取所有第一阶段的样品列表
     * @methodName : getFirstStageSampleInfoList
     * @param startTime :
     * @param endTime :
     * @param itemName :
     * @return : java.util.List<com.xwkj.ygjces.model.SampleInfo>
     * @exception :
     * @author : zyh
     */
    List<SampleInfo> getFirstStageSampleInfoListByCondition(@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("itemName") String itemName);

    /**
     * @Description : 按照条件获取所有第二阶段的样品列表
     * @methodName : getSecondStageSampleInfoList
     * @param startTime :
     * @param endTime :
     * @param itemName :
     * @return : java.util.List<com.xwkj.ygjces.model.SampleInfo>
     * @exception :
     * @author : zyh
     */
    List<SampleInfo> getSecondStageSampleInfoListByCondition(@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("itemName") String itemName);

    /**
     * @Description : 按照条件获取所有第三阶段的样品列表
     * @methodName : getThirdStageSampleInfoList
     * @param startTime :
     * @param endTime :
     * @param itemName :
     * @return : java.util.List<com.xwkj.ygjces.model.SampleInfo>
     * @exception :
     * @author : zyh
     */
    List<SampleInfo> getThirdStageSampleInfoListByCondition(@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("itemName") String itemName);

    /**
     * @Description : 根据样品编号列表后去样品列表
     * @methodName : getSampleListBySyNums
     * @param syNums :
     * @return : java.util.List<com.xwkj.ygjces.model.SampleInfo>
     * @exception :
     * @author : zyh
     */
    List<SampleInfo> getSampleListBySyNums(@Param("syNums") String syNums);

    Integer getJcXtcsXmCountWithMonth(@Param("categoryId") String categoryId, @Param("currentDate") Object currentDate);

    List<JcXtcsXmWithSampleVo> getJcXtcsXmNameAndCountWithMonth(@Param("defaultDate") String defaultDate, @Param("xmName") String xmName, @Param("start")Date start, @Param("end")Date end);

    List<WtUnitAmountVo> getWtUnitWithOneMonth(@Param("currYearMonth") String currYearMonth,@Param("wtUnit") String wtUnit,@Param("sfStatus") String sfStatus,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    WtUnitAmountVo getWtUnitAmount(@Param("gcName") String gcName,@Param("wtUnit") String wtUnit,@Param("sfStatus") String sfStatus,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    JcXmCategoryAmountVo getCountJcXtcsXmAmount(@Param("categoryId")String categoryId,@Param("currYearMonth") String currYearMonth,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    List<JcCoreSample> getAll(@Param("wtUnit") String wtUnit,@Param("startTime") Date startTime,@Param("endTime") Date endTime);
}
