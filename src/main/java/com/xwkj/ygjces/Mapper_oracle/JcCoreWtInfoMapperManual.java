package com.xwkj.ygjces.Mapper_oracle;

import com.xwkj.ygjces.controller.vo.JcCoreWtInfoVo;
import com.xwkj.ygjces.model.DateAndCount;
import com.xwkj.ygjces.model.JcCoreWtInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JcCoreWtInfoMapperManual {

    /**
     *根据城市区域时间查询委托单详情DAO实现
     * @param city
     * @param area
     * @param startTime
     * @param endTime
     * @return
     */
    List<JcCoreWtInfoVo> getMapCityAreaWtInfo(@Param("wt_num") String wt_num,@Param("city") String city, @Param("area") String area, @Param("startTime") Date startTime, @Param("endTime") Date endTime );

    /**
     * @param city
     * 根据城市区域按照区域统计委托单DAO接口
     * @param area
     * @return
     */
    Integer getMapCityAreaWtCount(@Param("city") String city, @Param("area") String area,@Param("startTime") Date startTime,@Param("endTime") Date endTime );

   /**
    * 根据项目编号查询委托单时间和总量接口
    * @param contractCode
    * @return
    */
   List<DateAndCount> getJcCoreWtInfoByContractCode( @Param("contractCode") String contractCode);
    /**
     * 根据合同编号查找对应委托单列表
     * @param contractCode 合同编号
     * @return
     * @author zyh
     */
   List<JcCoreWtInfo> getJcCoreWtInfoListByContractCode(String contractCode);

    /**
     * 根据合同编号查找对应已结算委托单的数量
     * @param contractCode 合同编号
     * @return
     * @author zyh
     */
   Long getJcCoreWtInfoJsAmountByContractCode(String contractCode);

    /**
     * 根据合同编号查找对应未结算委托单的数量
     * @param contractCode 合同编号
     * @return
     * @author zyh
     */
    Long getJcCoreWtInfoWjsAmountByContractCode(String contractCode);

    /**
     * 获取今年的有合同的委托单统计信息
     * @return
     * @author zyh
     */
    List<DateAndCount> getJcCoreWtInfoWithContractInThisYear();


    /**
     * 获取今年的无合同的委托单统计信息
     * @return
     * @author zyh
     */
    List<DateAndCount> getJcCoreWtInfoWithoutContractInThisYear();


    /**
     * 根据日期查询对应有合同的委托单数量
     * @param date
     * @return
     * @author zyh
     */
    Long getJcCoreWtInfoCountWithContractByDate(Date date);

    /**
     * 根据日期查询对应无合同的委托单数量
     * @param date
     * @return
     * @author zyh
     */
    Long getJcCoreWtInfoCountWithoutContractByDate(Date date);

    /**
     * 根据日期查找对应的委托单信息列表
     * @param jcCoreWtInfo 委托单信息
     * @return
     * @author zyh
     */
    List<JcCoreWtInfo> getJcCoreWtListInfoByDate(JcCoreWtInfo jcCoreWtInfo);

    /**
     * 查询当天委托单金额
     * @return
     * @author wanglei
     */
    public Integer selectWtRealMoneyWithOneDay();

    /**
     * 查询当月委托单金额
     * @return
     * @author wanglei
     */
    public Integer selectWtRealMoneyWithOneMonth();

    /**
     * 查询当天委托单数量
     * @return
     * @author wanglei
     */
    public Integer selectWtCountWithOneDay();

    /**
     * 查询当月委托单数量
     * @return
     * @author wanglei
     */
    public Integer selectWtCountWithOneMonth();

}